package pcd.assignment01.model.stats;

import java.util.ArrayList;
import java.util.List;

public class Interval {

    private final List<Pair<String, Integer>> files = new ArrayList<>();
    private int fileCount = 0;

    public List<Pair<String, Integer>> getFiles() {
        return files;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void add(String file, int lines) {
        fileCount++;
        files.add(new Pair<>(file, lines));
    }
}
