package pcd.assignment01.coordination.agent.task;

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
