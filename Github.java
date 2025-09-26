import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Github {
    public static boolean isCompressed;

    public static void main(String[] args) throws IOException {

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

    public static void fileWriter(String toWrite, File f) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
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

    public static void updateIndex(String sha1, String fileName) throws FileNotFoundException {
        File index = new File("./git/index");
        String toWrite = sha1 + " " + (new File(fileName)).getPath();
        fileWriter(toWrite, index);
    }

}