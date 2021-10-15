package gitlet;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author huyen nguyen
 */
public class Commit implements Serializable {
    /** ID of a file. */
    private String fID;
    /** message. */
    private String message;
    /** time at commit. */
    private String timeStamp;
    /** Commmit files. */
    private HashMap<String, String> files;
    /** the former commit. */
    private Commit parent;

    /** Constructor with argument.
     * @param stage is Stage object.
     * @param mes is message with commit.
     */
    Commit(StagingArea stage, String mes) {
        if (stage != null) {
            this.files = stage.getFiles();
            this.parent = stage.getLastCom();
        }

        this.message = mes;
        Date date = new Date();
        SimpleDateFormat var4 = new SimpleDateFormat("EEE "
                + "MMM dd kk:mm:ss yyyy Z");
        this.timeStamp = var4.format(date);
        this.saveFile();
    }

    /**
     * First commit.
     * @param msg is message.
     */
    Commit(String msg) {
        Date date = new Date();
        date.setTime(0);
        SimpleDateFormat time = new SimpleDateFormat("EEE "
                + "MMM dd kk:mm:ss yyyy Z");
        this.timeStamp = time.format(date);
        this.files = new HashMap<>();
        this.parent = null;
        this.message = msg;
        this.saveFile();
    }
    /** get file ID.
     * @return a file ID.
     */
    String getfID() {
        return this.fID;
    }
    /** get message.
     * @return a message.
     */
    String getMessage() {
        return this.message;
    }
    /** get current time.
     * @return current time.
     */
    String getTimeStamp() {
        return this.timeStamp;
    }
    /** get former commit.
     * @return a former commit.
     */
    Commit getParent() {
        return this.parent;
    }
    /** get all files.
     * @return all file.
     */
    HashMap<String, String> getFiles() {
        return this.files;
    }
    /** set ID. */
    void setfID(String id) {
        this.fID = id;
    }

    /**Save file. */
    private void saveFile() {
        if (this.files != null) {
            Iterator s = this.files.keySet().iterator();

            while (s.hasNext()) {
                String part = (String) s.next();
                File var3 = new File(part);
                String var10000 = File.separator;
                String path = ".gitlet" + var10000
                        + (String) this.files.get(part);
                File file = new File(path);
                Utils.writeContents(file,
                        new Object[]{Utils.readContents(var3)});
            }

        }
    }
    /** get File.
     * @param file is a file.
     * @return a file.
     */
    File getFileInFiles(String file) {
        String name = (String) this.files.get(file);
        String gettingFile = name + file;
        return new File(gettingFile);
    }

    /** Restore function.
     * @param name is name of file need to be restore.
     */
    void restoreFile(String name) {
        File file = new File(name);
        String getback = (String) this.files.get(name);
        File back = new File(".gitlet" + File.separator + getback);
        Utils.writeContents(file, new Object[]{Utils.readContents(back)});
    }
    /** Restore all file that were removed. */
    void restoreAllFile() {
        Set<String> name = this.files.keySet();
        Iterator f = name.iterator();

        while (f.hasNext()) {
            String v = (String) f.next();
            this.restoreFile(v);
        }

    }

    /** Modifiled function.
     * @param com is commit.
     * @param name is name of file.
     * @return false or true.
     */
    boolean modified(Commit com, String name) {
        return this.getFileInFiles(name).lastModified()
                > com.getFileInFiles(name).lastModified();
    }
    /** print plog. */
    void printLog() {
        System.out.println("===");
        System.out.println("commit " + this.getfID());
        System.out.println("Date: " + this.timeStamp);
        System.out.println(this.message);
    }

    /**
     * contain function.
     * @param name is file that is contained.
     * @return true or false.
     */
    boolean contain(String name) {
        if (files == null) {
            return false;
        }
        return this.files.containsKey(name);
    }
}
