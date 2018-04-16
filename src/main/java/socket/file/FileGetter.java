package socket.file;

import com.qr.order.FileOutCommandImpl;
import socket.file.model.simglefile.FileList;
import socket.file.model.simglefile.NewFile;
import socket.file.model.simglefile.WantFile;
import util.LocalIp;
import util.PathBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * @author qianrui
 */
public class FileGetter {

    public static void wantFileList(FileList fileList) {
        System.out.println("开始下载文件");
        List<NewFile> files = fileList.getFiles();
        String sourceIp = fileList.getSourceIp();
        String sourcePath = fileList.getSourcePath();
        int noticePort = fileList.getNoticePort();
        for (NewFile file : files) {
            final File localFile = file.getFile();
            final String name = localFile.getName();
            if (localFile.length() < file.getLength()) {
                System.out.println((localFile.length() == 0 ? "需要下载文件>>>" : "需要断点续传文件>>") + name);
                try {
                    int getPort=5767;
                    wantFile(file, LocalIp.getIP(), getPort, sourceIp, sourcePath, noticePort);
                    boolean success = getFileInt(file, getPort);
                    System.out.println((success ? "文件下载成功<<" : "文件下载失败><") + name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("跳过已经下载完毕文件>" + name);
            }
        }
        System.out.println("下载结束！！！");
    }

    private static void wantFile(NewFile newFile, String localIp, int getPort, String sourceIp, String sourcePath, int noticePort) throws IOException {
        String realPath = PathBuilder.addFileName(sourcePath, newFile.getRemoteName());
        System.out.println("请求文件资源 : " + sourceIp + "://" + realPath);
        FileOutCommandImpl.INSTANCE.wantFile(wantFile(newFile, localIp, getPort, realPath), sourceIp, noticePort);
    }

    private static WantFile wantFile(NewFile newFile, String localIp, int getPort, String realPath) throws UnsupportedEncodingException {
        WantFile wantFile = new WantFile();
        wantFile.setPath(realPath);
        wantFile.setWantIp(localIp);
        wantFile.setPort(getPort);
        wantFile.setBeginLength(newFile.getFile().length());
        return wantFile;
    }

    /**
     * 接受文件流,成功返回true，失败返回false
     */
    private static boolean getFileInt(NewFile newFile, int getPort) throws IOException {
        File path = newFile.getFile();
        long nowLength = path.length();
        long allLength = newFile.getLength();
        try (ServerSocket serverSocket = new ServerSocket(getPort); Socket socket = serverSocket.accept(); FileOutputStream fileOutputStream = new FileOutputStream(path, true); InputStream inputStream = socket.getInputStream()) {
            byte[] b = new byte[1 << 16];
            int read;
            while ((read = inputStream.read(b)) > -1) {
                nowLength += read;
                System.out.println(read + "(" + nowLength + "," + allLength + ")");
                fileOutputStream.write(b, 0, read);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
