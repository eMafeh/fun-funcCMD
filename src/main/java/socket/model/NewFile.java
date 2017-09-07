package socket.model;

import java.io.File;

public class NewFile {
    private File file;
    private long length;

    @Override
    public String toString() {
        return "NewFile{" +
                "file=" + file +
                ", length=" + length +
                '}';
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
