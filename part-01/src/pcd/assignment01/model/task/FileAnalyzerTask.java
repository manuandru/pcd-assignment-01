package pcd.assignment01.model.task;

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
