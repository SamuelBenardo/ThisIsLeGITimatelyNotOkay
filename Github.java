import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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
    private static File[] createDirList() {
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
        String contents = readFileContent(f);
        String hash = hashFile(contents);
        File blob = new File(dir + hash);

        if ((new File(dir)).exists() && !blob.exists()) {
            try {
                blob.createNewFile();
                transferContents(contents, blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 

    }

    private static void transferContents(String contents, File nFile) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(nFile));
            bw.write(contents);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    // reads the file contents and returns as a string
    public static String readFileContent(File f) {
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
}

/* QUESTIONS
 * Do you have to close BW?? need while loop??/
 * Errors anything special???
 * 
 * 
 */
