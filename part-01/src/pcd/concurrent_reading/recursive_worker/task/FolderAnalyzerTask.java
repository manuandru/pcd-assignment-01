package pcd.concurrent_reading.recursive_worker.task;

public class FolderAnalyzerTask implements Task {

    private final String folderPath;

    public FolderAnalyzerTask(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public String path() {
        return this.folderPath;
    }
}
