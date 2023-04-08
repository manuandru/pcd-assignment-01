package pcd.concurrent_reading.recursive_worker.task;

public class FileAnalyzerTask implements Task {

    private final String filePath;

    public FileAnalyzerTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String path() {
        return this.filePath;
    }
}
