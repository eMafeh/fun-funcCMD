package socket.file.model.simglefile;

import java.io.File;

/**
 * 新文件模型
 * @author qianrui
 */
public class NewFile {
    private File file;
    private long length;
    private String remoteName;

    public NewFile() {
    }

    public NewFile(File file) {

        this.file = file;
    }

    @Override
    public String toString() {
        return "NewFile{" +
                "file=" + file +
                ", length=" + length +
                ", remoteName='" + remoteName + '\'' +
                '}';
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
