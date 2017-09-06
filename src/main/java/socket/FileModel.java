package socket;

import java.io.File;

public class FileModel {
    String name;
    long length;

    public FileModel(File file) {
        if (file == null) throw new NullPointerException();
        name = file.getName();
        length = file.length();
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
