package pcd.assignment01.model.stats;

import java.util.ArrayList;
import java.util.List;

public class Interval {

    private final List<String> files = new ArrayList<>();
    private int fileCount = 0;

    public List<String> getFiles() {
        return files;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void add(String file) {
        fileCount++;
        files.add(file);
    }
}
