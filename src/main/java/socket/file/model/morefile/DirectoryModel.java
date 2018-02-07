package socket.file.model.morefile;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录模型，包含目录名，子目录，子文件夹
 * @author qianrui
 */
public class DirectoryModel {
    private List<FileModel> files = new ArrayList<>();
    private List<DirectoryModel> directorys = new ArrayList<>();
    private String name;

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
