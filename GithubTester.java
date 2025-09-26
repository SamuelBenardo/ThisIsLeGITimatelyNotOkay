import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GithubTester {
    public static void main(String[] args) throws IOException {
        deleteTestFiles();
    }

    // tests if the directory exists
    public static boolean testDirExistence() {
        File[] files = Github.createDirList();
        boolean[] doesExist = new boolean[4];
        boolean conclusion = true;

        for (int i = 0; i < 4; i++) {
            doesExist[i] = files[i].exists();
            conclusion = conclusion && files[i].exists();
        }

        System.out.println("~ Existing Directories ~\nGit: " + doesExist[0] + "\nObjects: " + doesExist[1] + "\nIndex: "
                + doesExist[2] + "\nHead: " + doesExist[3]);

        return conclusion;
    }

    // resets all directories
    public static void resetDirectories() {
        File dir = new File("./git");
        if (dir.exists()) {
            deleteAllFiles(dir);
            System.out.println("Files sucessfully deleted.");
            dir.delete();
        } else {
            System.out.println("Main directory could not be located.");
        }
    }

    // recursively deletes files
    public static void deleteAllFiles(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                deleteAllFiles(f);
            }
            f.delete();
        }
    }



    // BLOB

    // creates a blob using file data input, returns true if worked and false if not. resets Blob file after.
    public static boolean doesFileBLOB(File f) {
        if (!f.exists() ) {
            Github.createBLOBfile(f);

            String dir = "./git/objects/";
            String contents = Github.readFile(f);
            String hash = Github.hashFile(contents);
            File blob = new File(dir + hash);

            if (blob.exists()) {
                resetBlob();
                return true;
            } else {
                resetBlob();
                return false;
            }
        }
        System.out.println("File already exists in Blob. Please reset.");
        return false;

    }  

    // resets the index and clears git/objects folder
    public static void resetBlob() {

        // reset index
        File index = new File("./git/index");
        if (index.exists()) {
            if (index.delete()) {
                try {
                    index.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // reset BLOB files
        File objs = new File("./git/objects");
        if (objs.exists()) {
            deleteAllFiles(objs);
        }
    }


    // INDEX

    // creates 4 files & checks if the blobbing works correctly and the indexing works (using the noncompressed version of SHA1)
    public static void testIndexing() throws IOException {

        // creating test files
        String dir = "./git/";
        String[] files = new String[]{"f1.txt", "f2.txt", "f3.txt", "f4.txt"};
        String[] contents = new String[]{"eeny", "meeny", "miny", "moe"};
        String[] sha1 = new String[]{"4140d3efad5acc01cc34b58ee88fc6c21568262d", "42a9a4ef58698cb708e9fefba06bd49ad02e05e1", "03516b717bbb8ca16a73ec06f236d0bc7d4120cb", "63e885ca488b7659504b5878d017e2a196f4475e"};

        for (int i = 0; i < 4; i++) {
            File f = new File(dir + files[i]);
            Github.fileWriter(contents[i], f);
            Github.createBLOBfile(f);
            Github.updateIndex(Github.hashFile(contents[i]), files[i]);
        }

        // checking validity
        dir = "./git/objects/";
        System.out.println("Was correct SHA-1 file blobbed in objects?");
        for (int i = 0; i < 4; i++) {
            System.out.println(files[i] + ": " + (new File(dir + sha1)).exists());
        }

        System.out.println();
        System.out.println("Was index updated correctly?");
        BufferedReader br = new BufferedReader(new FileReader(new File("./git/index")));
        while (br.ready()) {
            for (int i = 0; i < 4; i++) {
                String index = br.readLine();
                String fileName = index.substring((index.length() - 7));
                String hash = index.substring(0, index.length() - 6);
                System.out.println(files[i] + ":");
                System.out.println("SHA-1: " + hash.equals(sha1[i]));
                System.out.println("File name " + fileName.equals(files[i]));
                System.out.println();
            }
        }
        br.close();


    }

    // deletes non-essential files in git folder (not within objs)
    public static void deleteTestFiles() {
        String dir = "./git";
        ArrayList<String> basicGitDirs = new ArrayList<>(3);
        basicGitDirs.add("objects");
        basicGitDirs.add("HEAD");
        basicGitDirs.add("index");
        String[] currentFiles = (new File(dir)).list();

        for (String file : currentFiles) {
            if (!basicGitDirs.contains(file)) {
                (new File(dir + "/" + file)).delete();
            }
        }
    }

    public static void resetToMyStandardsAKABareMinimum() {
        resetBlob();
        deleteTestFiles();
    }

}
