public class fileInformationObject {

    private int fileDepth;
    private static String fileEntryLineInIndex;
    private String fileParentName;

    public fileInformationObject(String fileEntryLineInIndex) {
        this.fileEntryLineInIndex = fileEntryLineInIndex;
        this.fileDepth = findFileDepth();
        this.fileParentName = findFileParentName();
    }

    public int getfileDepth() {
        return findFileDepth();
    }

    public static int findFileDepth() {
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

    public String getfileParentName() {
        return fileParentName;
    }

    public String findFileParentName() {
        int indexOflastSlash = fileEntryLineInIndex.lastIndexOf('\\');
        String fileEntryLineInIndexUpToIndexOfLastSlash = fileEntryLineInIndex.substring(0, indexOflastSlash);
        int indexOfSecondToLastSlash = fileEntryLineInIndexUpToIndexOfLastSlash.lastIndexOf('\\');
        String fileParentName = fileEntryLineInIndex.substring(indexOfSecondToLastSlash + 1, indexOflastSlash);
        return fileParentName;
    }

}
