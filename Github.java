import java.io.File;
import java.io.IOException;

public class Github {
    public static void main(String[] args) throws IOException {
        initializeDirs();
    }

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

    public static void sha1Hash(File f) {
        String 
    }

    public static String readFile(File f) {
        return "";
    }

    public static String hash(String fileContent) {
        return "";
    }
}