package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/** StagingArea class.
 *  @author huyen nguyen
 */
public class StagingArea implements Serializable {
    /** last commit. */
    private Commit lastCom;
    /** New commit. */
    private ArrayList<String> newCom;
    /** Remark remove. */
    private ArrayList<String> rmRemove;
    /** remove arrayelist. */
    private ArrayList<String> rm;
    /** files in stage. */
    private HashMap<String, String> filesStage;

    /**
     * Constructor of this class.
     * @param com is a commit.
     */
    StagingArea(Commit com) {
        this.lastCom = com;
        this.newCom = new ArrayList<>();
        this.rmRemove = new ArrayList<>();
        this.filesStage = new HashMap<>();
        this.rm = new ArrayList<>();
        if (com.getFiles() != null) {
            this.filesStage.putAll(com.getFiles());
        }

    }

    /**
     * Get last commit.
     * @return a commit.
     */
    Commit getLastCom() {
        return this.lastCom;
    }

    /**
     * get new commit.
     * @return a rraylist.
     */
    ArrayList<String> getNewCom() {
        return this.newCom;
    }

    /**
     * get remove remark.
     * @return a arraylist.
     */
    ArrayList<String> getRmRemove() {
        return this.rmRemove;
    }

    /**
     * get files.
     * @return return a hasmap.
     */
    HashMap<String, String> getFiles() {
        return this.filesStage;
    }

    /**
     * get reomve file.
     * @return an arraylist.
     */
    ArrayList<String> getRm() {
        return this.rm;
    }

    /**
     * add new file in stage.
     * @param f is name of file.
     */
    void addFile(String f) {
        File file = new File(f);
        if (!file.exists() && !rm.contains(f)) {
            System.out.println("File does not exist.");
            return;
        }

        if (rm.contains(f)) {
            lastCom.restoreFile(f);
            rm.remove(f);
            rmRemove.remove(f);
            return;
        }
        String name = Utils.sha1(Utils.readContentsAsString(file));
        if (lastCom.contain(f)) {
            if (!lastCom.getfID().equals(name)) {

                if (!filesStage.containsKey(f)) {

                    newCom.add(f);
                }
                filesStage.put(f, name);
            } else {
                if (rm.contains(f)) {
                    lastCom.restoreFile(f);
                }
            }
        } else {

            if (!filesStage.containsKey(f)) {
                newCom.add(f);
            }
            filesStage.put(f, name);
        }
    }

    /**
     * remove function.
     * @param file is name of file.
     */
    void remove(String file) {
        if (filesStage.containsKey(file)) {
            filesStage.remove(file);
            newCom.remove(file);
            rmRemove.add(file);
        } else {
            System.out.println("No reason to remove the file.");
        }
    }

    /**
     * remove from stage.
     * @param file is name of file.
     */
    void removeFromStage(String file) {
        filesStage.remove(file);
    }
}
