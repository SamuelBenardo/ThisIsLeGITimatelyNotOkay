public class fileInformationObject {

    private int fileDepth;
    private String fileEntryLineInIndex;
    private String fileEntryLineInATreeInObjects;
    private String fileParentName;
    private String fileParentPath;

    public fileInformationObject(String fileEntryLineInIndex) {
        this.fileEntryLineInIndex = fileEntryLineInIndex;
        this.fileDepth = findFileDepth(fileEntryLineInIndex);
        this.fileParentPath = findFileParentPath();
        this.fileParentName = findFileParentName();
        this.fileEntryLineInATreeInObjects = findFileEntryLineInATreeInObjects();
    }

    public fileInformationObject(String fileEntryLineInIndex, int fileDepth) {
        this.fileEntryLineInIndex = fileEntryLineInIndex;
        this.fileDepth = fileDepth;
        this.fileParentPath = findFileParentPath();
        this.fileParentName = findFileParentName();
        this.fileEntryLineInATreeInObjects = findFileEntryLineInATreeInObjects();
    }

    public int getfileDepth() {
        return fileDepth;
    }

    public static int findFileDepth(String fileEntryLineInIndex) {
        int fileDepth = 0;
        for (int i = 0; i < fileEntryLineInIndex.length(); i++) {
            if (fileEntryLineInIndex.charAt(i) == '\\') {
                fileDepth++;
            }
        }
        return fileDepth;
    }

    public String getfileEntryLineInIndex() {
        return fileEntryLineInIndex;
    }

    public String getfileEntryLineInATreeInObjects() {
        return fileEntryLineInATreeInObjects;
    }

    public String getfileParentName() {
        return fileParentName;
    }

    public String getfileParentPath() {
        return fileParentPath;
    }

    public String findFileParentPath() {
        String fileParentPath = "";
        int indexOfSecondSpaceInEntryLine = fileEntryLineInIndex.lastIndexOf(' ');
        String filePath = fileEntryLineInIndex.substring(indexOfSecondSpaceInEntryLine + 1);
        int indexOfLastSlashInFilePath = filePath.lastIndexOf('\\');
        if (indexOfLastSlashInFilePath == -1) {
            fileParentPath = ".";
        } else {
            fileParentPath = filePath.substring(0, indexOfLastSlashInFilePath);
        }
        return fileParentPath;
    }

    public String findFileParentName() {
        String fileParentName = "";
        int indexOfLastSlashInFileParentPath = this.fileParentPath.lastIndexOf('\\');
        if (fileParentPath.equals(".")) {
            fileParentName = ".";
        } else {
            fileParentName = this.fileParentPath.substring(indexOfLastSlashInFileParentPath + 1);
        }
        return fileParentName;
    }

    public String findFileEntryLineInATreeInObjects() {
        int indexOfSecondSpace = fileEntryLineInIndex.lastIndexOf(' ');
        int indexOfLastSlash = fileEntryLineInIndex.lastIndexOf('\\');
        String fileName = fileEntryLineInIndex.substring(indexOfLastSlash + 1);
        String fileEntryLineInATreeInObjects = fileEntryLineInIndex.substring(0, indexOfSecondSpace) + " " + fileName;
        return fileEntryLineInATreeInObjects;
    }
}
