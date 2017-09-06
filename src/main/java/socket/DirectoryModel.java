package socket;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryModel {
    List<FileModel> files = new ArrayList<>();
    List<DirectoryModel> directorys = new ArrayList<>();
    String name;

    public List<FileModel> getFiles() {
        return files;
    }

    public List<DirectoryModel> getDirectorys() {
        return directorys;
    }

    public void setDirectorys(List<DirectoryModel> directorys) {
        this.directorys = directorys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "DirectoryModel{" +
                "files=" + files +
                ", directorys=" + directorys +
                ", name='" + name + '\'' +
                '}';
    }
}
