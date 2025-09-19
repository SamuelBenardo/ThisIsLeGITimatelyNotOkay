import java.io.File;
import java.io.IOException;

public class Github {
    public static void main(String[] args) throws IOException {
        initializeDirs();
    }

    // Creates the directories required for the github
    public static void initializeDirs() throws IOException {

        // File names
        String git = "./git";
        String[] files = new String[]{"/objects", "/index", "/HEAD"};
        boolean isCreated = false;

        File dir = new File(git);

        if (!dir.exists()) {
            dir.mkdir();
            isCreated = true;

            // Cycles through list and turns strings into files (if they do not exist).
            for (int i = 0; i < files.length; i++) {

                files[i] = git + files[i];
                File f = new File(files[i]);

                if (!f.exists()) {
                    f.createNewFile();
                } else {
                    isCreated = false;
                }
            }
        }

        // prints confirmation method
        if (isCreated) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }

    }

    // makes a directory based on file name. Returns true if created, false if not.
    public static boolean createDir(String name) {
        File dir = new File(name);
        if (!dir.exists()) {
            dir.mkdir();
            return true;
        } 
        return false;
    }



    public static String readFile(File file) {
        return "";
    }

    public static String hash() {
        return "";
    }
}