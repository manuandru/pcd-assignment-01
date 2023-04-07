package pcd.assignment01.model.task;

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
