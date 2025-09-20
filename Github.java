import java.io.File;
import java.io.IOException;

public class Github {
    public static void main(String[] args) throws IOException {
        initializeDirs();
    }

    // Creates the directories required for the github
    public static void initializeDirs() throws IOException {

        // File names
        File[] files = createDirList();
        boolean isCreated = false;

        if (!files[0].exists()) {
            files[0].mkdir();
            isCreated = true;
        }

        // Cycles through list and turns strings into files (if they do not exist).
        for (int i = 1; i < files.length; i++) {
            if (!files[i].exists()) {
                files[i].createNewFile();
            } else {
                isCreated = false;
            }
        }

        // prints confirmation method
        if (isCreated) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
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



    public static String readFile(File file) {
        return "";
    }

    public static String hash() {
        return "";
    }
}