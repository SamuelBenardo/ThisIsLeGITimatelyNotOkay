import java.io.File;
import java.io.IOException;

public class GithubTester {
    public static void main(String[] args) {

    }



    // DIRECTORY TESTERS

    // tests if the directory exists
    public static boolean testDirExistence() {
        File[] files = Github.createDirList();
        boolean[] doesExist = new boolean[4];
        boolean conclusion = true;

        for (int i = 0; i < 4; i++) {
            doesExist[i] = files[i].exists();
            conclusion = conclusion && files[i].exists();
        }

        
        System.out.println("~ Existing Directories ~\nGit: " + doesExist[0] + "\nObjects: " + doesExist[1] + "\nIndex: " + doesExist[2] + "\nHead: " + doesExist[3]);

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

    // creates and deletes ./git dirs the number of times indicated by the argument.
    public static void dirTest(int numCycles) {
        File git = new File("./git");
        if (git.exists()) {
            resetDirectories();
        }

        for (int i = 0; i < numCycles; i++) {
            try {
                System.out.println("Trial " + (i + 1) + ": ");
                Github.initializeDirs();
                resetDirectories();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    // BLOB

    // creates a blob using file data input, returns true if worked and false if not. resets Blob file after.
    public static boolean doesFileBLOB(File f) {
        if (!f.exists() ) {
            Github.createBLOBfile(f);

            String dir = "./git/objects/";
            String contents = Github.readFileContent(f);
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

}
