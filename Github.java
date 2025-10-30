import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPOutputStream;
import java.util.Formatter;
import java.nio.file.*;
import java.util.*;

public class Github {
    public static boolean isCompressed;
    public static ArrayList<fileInformationObject> fileInformationObjects;

    // Creates the directories required for the github
    public static void initializeDirs() throws IOException {

        // Initializes File names + make git dir
        File[] files = createDirList();
        boolean isCreated = false;

        if (!files[0].exists()) {
            files[0].mkdir();
            isCreated = true;
            if (!files[1].exists()) {
                files[1].mkdir();
            } else {
                isCreated = false;
            }
        }

        // Cycles through list and turns strings into files (if they do not already
        // exist).
        for (int i = 2; i < files.length; i++) {
            if (!files[i].exists()) {
                files[i].createNewFile();
            } else {
                isCreated = false;
            }
        }

        // Prints confirmation method
        if (isCreated) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }

    }

    // initializes a list of all files to be created
    public static File[] createDirList() {
        String dir = "./git";
        String[] subFiles = new String[] { "", "/objects", "/index", "/HEAD" };
        File[] files = new File[4];

        for (int i = 0; i < 4; i++) {
            subFiles[i] = dir + subFiles[i];
            files[i] = new File(subFiles[i]);
        }

        return files;
    }

    // takes a file and maps it to a SHA1 file in the object dir
    public static void createBLOBfile(File f) {
        String dir = "./git/objects/";
        String contents = readFile(f);
        String hash = hashFile(contents);
        File blob = new File(dir + hash);

        if ((new File(dir)).exists() && !blob.exists()) {
            try {
                blob.createNewFile();
                fileWriter(contents, blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void fileWriter(String toWrite, File f) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            if (!isEmpty(f)) {
                bw.newLine();
            }
            bw.write(toWrite);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // checks if empty, WILL NOT WORK FOR ENCODED -- i don't think
    public static boolean isEmpty(File f) throws IOException {
        BufferedReader bw = new BufferedReader(new FileReader(f));
        if (bw.readLine() == null) {
            bw.close();
            return true;
        }
        bw.close();
        return false;
    }

    // reads the file contents and returns as a string
    public static String readFile(File f) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready()) {
                sb.append(br.readLine());
            }
            br.close();
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // hashes the given contents into sha-1
    public static String hashFile(String contents) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(contents.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sha1;
    }

    // converts to bytes
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    // compresses given string
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    public static void updateIndex(String sha1, String fileName) throws IOException, FileNotFoundException {
        File file = new File(fileName);
        File index = new File("./git/index");
        String toWrite = "blob " + sha1 + " " + file.getPath();
        fileWriter(toWrite, index);
    }

    public static void storeFileSystemInObjects() throws IOException {
        initializeWorkingList();
        if (fileInformationObjects.isEmpty()) {
            handleEdgeCaseWhereThereAreNoFiles();
            return;
        }
        createTrees();
    }

    public static void handleEdgeCaseWhereThereAreNoFiles() throws IOException {
        String hashOfEmptyString = hashFile("");
        File rootTree = new File("git/objects/" + hashOfEmptyString);
        rootTree.createNewFile();
    }

    public static void initializeWorkingList() throws IOException {
        Path indexPath = Paths.get("git/index");
        Path workingListPath = Paths.get("git/objects/workingList");
        Files.copy(indexPath, workingListPath);
        sortWorkingList();
    }

    public static void sortWorkingList() throws IOException {
        createArrayListOfFileInformationObjects();
        sortArrayListOfFileInformationObjects();
        createSortedWorkingListOffOfArrayListOfFileInformationObjects();
    }

    public static void sortArrayListOfFileInformationObjects() throws IOException {
        fileInformationObjects.sort(
                Comparator.comparingInt(fileInformationObject::getfileDepth)
                        .reversed()
                        .thenComparing(fileInformationObject::getfileParentName));
    }

    public static void createArrayListOfFileInformationObjects() throws IOException {
        ArrayList<fileInformationObject> unsortedFileInformationObjects = new ArrayList<>();
        BufferedReader workingListReader = new BufferedReader(new FileReader("git/objects/workingList"));
        while (workingListReader.ready()) {
            String fileEntryLineInIndex = workingListReader.readLine();
            fileInformationObject fileInformationObject = new fileInformationObject(fileEntryLineInIndex);
            unsortedFileInformationObjects.add(fileInformationObject);
        }
        workingListReader.close();
        fileInformationObjects = unsortedFileInformationObjects;
    }

    public static void createSortedWorkingListOffOfArrayListOfFileInformationObjects() throws IOException {
        File workingList = new File("git/objects/workingList");
        workingList.delete();
        StringBuilder workingListContents = new StringBuilder();
        for (fileInformationObject fileInformationObject : fileInformationObjects) {
            String fileEntryLineInIndex = fileInformationObject.getfileEntryLineInIndex();
            workingListContents.append(fileEntryLineInIndex);
            workingListContents.append("\n");
        }
        BufferedWriter workingListWriter = new BufferedWriter(new FileWriter("git/objects/workingList"));
        String workingListContentsString = workingListContents.toString();
        workingListContentsString.trim();
        workingListWriter.write(workingListContentsString);
        workingListWriter.close();
    }

    public static void createTrees() throws IOException {
        while (fileInformationObjects.size() > 1) {
            createTree();
        }
        // !fileInformationObjects.get(0).getfileParentName().equals(".")
    }

    public static void createTree() throws IOException {
        boolean isLastTreeCreated = false;
        fileInformationObject firstFile = fileInformationObjects.get(0);
        String firstFileParentName = firstFile.getfileParentName();
        String firstFileParentPath = firstFile.getfileParentPath();
        int firstFileDepth = firstFile.getfileDepth();

        StringBuilder newTreeContents = new StringBuilder();
        String firstFileEntryLineIntoIndex = firstFile.getfileEntryLineInIndex();
        newTreeContents.append(firstFileEntryLineIntoIndex);
        fileInformationObjects.remove(0);

        fileInformationObject nextFile = fileInformationObjects.get(0);
        String nextFileParentName = nextFile.getfileParentName();
        int nextFileDepth = nextFile.getfileDepth();

        while (!isLastTreeCreated && nextFileParentName.equals(firstFileParentName) && nextFileDepth == firstFileDepth) {
            newTreeContents.append("\n");
            newTreeContents.append(nextFile.getfileEntryLineInIndex());
            fileInformationObjects.remove(0);
            if (!fileInformationObjects.isEmpty()) {
                nextFile = fileInformationObjects.get(0);
                nextFileParentName = nextFile.getfileParentName();
                nextFileDepth = nextFile.getfileDepth();
            } else {
                isLastTreeCreated = true;
            }
        }
        String newTreeContentsString = newTreeContents.toString();
        String treeFileInformationObjectHash = hashFile(newTreeContentsString);
        String newTreeEntryLine = "tree " + treeFileInformationObjectHash + " " + firstFileParentPath;
        fileInformationObject newTreeFileInformationObject = new fileInformationObject(newTreeEntryLine,
                firstFileDepth - 1);
        fileInformationObjects.add(0, newTreeFileInformationObject);
        File newTree = new File("git/objects/" + treeFileInformationObjectHash);
        newTree.createNewFile();
        FileWriter newTreeWriter = new FileWriter(newTree);
        newTreeWriter.write(newTreeContentsString);
        newTreeWriter.close();
        sortArrayListOfFileInformationObjects();
        createSortedWorkingListOffOfArrayListOfFileInformationObjects();
    }

    public static String hashIndexFile() throws IOException {
        File file = new File("./git/index");
        String contents = readFile(file);
        return hashFile(contents);
    }
}