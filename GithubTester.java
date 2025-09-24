import java.io.File;

public class GithubTester {
    public static void main(String[] args) {
        testDirExistence();
        resetDirectories();
        testDirExistence();
    }


    // tests if the directory exists
    public static boolean testDirExistence() {
        File[] files = createDirList();
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

    public static File[] createDirList() {
        String dir = "./git";
        String[] subFiles = new String[]{"", "/objects", "/index", "/HEAD"};
        File[] files = new File[4];

        for (int i = 0; i < 4; i++) {
            subFiles[i] = dir + subFiles[i];
            files[i] = new File(subFiles[i]);
        }

        return files;
    }

}
