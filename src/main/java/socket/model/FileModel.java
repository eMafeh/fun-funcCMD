package socket.model;

import java.io.File;

/**
 * 文件模型，包含文件名，文件长度
 */
public class FileModel {
    private String name;
    private long length;

    public FileModel(File file) {
        if (file == null) throw new NullPointerException();
        name = file.getName();
        length = file.length();
    }

    public FileModel() {
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
