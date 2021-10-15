package gitlet;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.util.PriorityQueue;

/** Command Class.
 *  @author huyen nguyen
 */
public class Command implements Serializable {
    /** list of commits. */
    private ArrayList<Commit> commits = new ArrayList<>();
    /**ID and commit .*/
    private HashMap<String, Commit> idAndcommits = new HashMap<>();
    /**current branch .*/
    private String currBranch;
    /**header of branches .*/
    private HashMap<String, Commit> headerBranch = new HashMap<>();
    /**Staging Area in Command .*/
    private HashMap<String, StagingArea> stageinCommand = new HashMap<>();
    /**List of all branch in order. */
    private PriorityQueue<String> lstBranch = new PriorityQueue<>();
    /** List of unstracking file. */
    private ArrayList<String> unstrack = new ArrayList<>();
    /**Contructor of Command class.*/
    Command() {
        this.currBranch = "master";
        Commit first = new Commit("initial commit");
        this.commits.add(first);
        StagingArea newstage = new StagingArea(first);
        this.stageinCommand.put(currBranch, newstage);
        this.headerBranch.put(currBranch, first);
        first.setfID(Utils.sha1(new Object[]{Utils.serialize(first)}));
        this.idAndcommits.put(first.getfID(), first);
        lstBranch.add(currBranch);
    }
    /** Init function.
     * @return Command.
     * */
    static Command init() {
        return new Command();
    }
    /** Add function.
     * @param name is name of file that is added.
     * */
    void add(String name) {

        StagingArea currentStage = stageinCommand.get(currBranch);
        currentStage.addFile(name);

    }
    /** Commit function.
     * @param message is message when we commit.
     * */
    void commit(String message) {
        StagingArea stage = stageinCommand.get(currBranch);
        for (String i: stage.getRmRemove()) {
            stage.removeFromStage(i);
        }
        if (stage.getFiles().isEmpty()) {
            System.out.println("No changes added to the commit");
            System.exit(0);
        }
        Commit head = headerBranch.get(currBranch);
        Commit temp = new Commit(stageinCommand.get(currBranch), message);
        StagingArea newStage = new StagingArea(temp);
        commits.add(temp);
        stageinCommand.put(currBranch, newStage);
        temp.setfID(Utils.sha1(Utils.serialize(temp)));
        idAndcommits.put(temp.getfID(), temp);
        headerBranch.put(currBranch, temp);
    }
    /** Log function.
     * */
    void log() {
        Commit now = headerBranch.get(currBranch);
        now.printLog();
        now = now.getParent();
        while (now != null) {
            System.out.println();
            now.printLog();
            now = now.getParent();
        }
    }
    /** Global log function. */
    void logGlobal() {
        if (commits.size() > 1) {
            commits.get(0).printLog();
        }
        for (int i = 1; i < commits.size(); i += 1) {
            commits.get(i).printLog();
        }
    }
    /** Find function.
     * @param message is commit message.
     * */
    void find(String message) {
        boolean found  = false;
        for (Commit c : commits) {

            if (c.getMessage().equals(message)) {
                found = true;
                System.out.println(c.getfID());

            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
            return;
        }
    }
    /** Status Function. */
    void status() {
        System.out.println("=== Branches ===");
        for (String b : lstBranch) {
            if (b.equals(currBranch)) {
                System.out.println("*" + b);
            } else {
                System.out.println(b);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String name: stageinCommand.get(currBranch).getNewCom()) {
            System.out.println(name);
        }

        System.out.println("\n=== Removed Files ===");
        for (String name: stageinCommand.get(currBranch).getRm()) {
            System.out.println(name);
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        for (String s : unstrack) {
            System.out.println(s);
        }
        System.out.println("\n=== Untracked Files ===\n");

    }
    /** Remove function.
     * @param name is name of file need to be removed.
     * */
    void rm(String name) {
        stageinCommand.get(currBranch).remove(name);
        boolean y = headerBranch.get(currBranch).contain(name);
        if (headerBranch.get(currBranch).contain(name)) {
            Utils.restrictedDelete(name);
            stageinCommand.get(currBranch).getRm().add(name);
        }
    }

    /** Checkout file.
     * @param file is file need to be checked out.
     */
    void checkout1(String file) {
        if (!headerBranch.get(currBranch).contain(file)) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            headerBranch.get(currBranch).restoreFile(file);
        }
    }
    /** Checkout 2.
     * @param file is file need to be checked out.
     * @param fID is Id of file.
     */
    void checkout2(String file, String fID) {
        if (!idAndcommits.containsKey(fID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit = idAndcommits.get(fID);
        if (!commit.contain(file)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        commit.restoreFile(file);
    }
    /** Checkout3.
     * @param key is id of file need to be checked out.
     */
    void checkout3(String key) {
        if (!headerBranch.containsKey(key)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (currBranch.equals(key)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        StagingArea current = stageinCommand.get(currBranch);
        currBranch = key;
        headerBranch.get(currBranch).restoreAllFile();
        Commit newC = headerBranch.get(currBranch);
        for (String file : current.getFiles().keySet()) {
            if (!newC.getFiles().containsKey(file)) {
                Utils.restrictedDelete(file);
            }
        }

        if (newC != null) {
            for (String name : newC.getFiles().keySet()) {
                if (unstrack.contains(name)) {
                    System.out.println("There is an untracked file in "
                            + "the way; delete it, or add "
                            + "and commit it first.");
                }
            }
        }
    }

    /** Checkout func.
     * @param args is passed in.
     * @param com is current command.
     */
    static void checkout(Command com, String...args) {
        if ((args.length == 3) && (args[1].equals("--"))) {
            com.checkout1(args[2]);
        } else if ((args.length == 4) && (args[2].equals("--"))) {
            com.checkout2(args[3], args[1]);
        } else if (args.length == 2) {
            com.checkout3(args[1]);
        } else {
            System.out.println("Incorrect operands.");
            return;
        }
    }
    /** Adding branch.
     * @param newBranch the new branch need to be add.
     */
    void addNewBranch(String newBranch) {
        lstBranch.add(newBranch);
        if (headerBranch.containsKey(newBranch)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Commit head = headerBranch.get(currBranch);
        StagingArea stage = new StagingArea(head);
        stageinCommand.put(newBranch, stage);
        headerBranch.put(newBranch, head);
    }
    /** removing branch.
     * @param branch the new branch need to be removed.
     */
    void removeBranch(String branch) {
        if (!headerBranch.containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (currBranch.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        headerBranch.remove(branch);
    }

    /** Reseting ID.
     * @param id is key need to reseted.
     */
    void reset(String id) {
        if (!idAndcommits.containsKey(id)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit gettingReset = idAndcommits.get(id);
        gettingReset.restoreAllFile();
        headerBranch.put(currBranch, gettingReset);
        stageinCommand.put(currBranch, new StagingArea(gettingReset));
    }
    /** Checking branch.
     * @param newBranch the new branch need to be merged.
     * @param nameOfBranch is name of branch.
     */
    void checkMerge(String nameOfBranch, Commit newBranch) {
        StagingArea cur = stageinCommand.get(currBranch);
        for (String file : newBranch.getFiles().keySet()) {
            File name = new File(file);
            if (name.exists()
                    && (!cur.getNewCom().contains(file))
                    &&  (!cur.getRmRemove().contains(file))) {
                System.out.println("There is an untracked file in the way; "
                        + " delete it, or add and commit it first.");
                System.exit(0);
            }
            if (!cur.getNewCom().isEmpty()
                    || !cur.getRmRemove().isEmpty()) {
                System.out.println("You have uncommitted changes.");
                System.exit(0);
            }
            if (!headerBranch.containsKey(nameOfBranch)) {
                System.out.println("A branch with that name does not exist.");
                System.exit(0);
            }
            if (nameOfBranch.equals(currBranch)) {
                System.out.println("Cannot merge a branch with itself.");
                System.exit(0);
            }
        }
    }
    /** Split function.
     * @param currentBranch is current commit.
     * @param newBranch is new commit.
     * @return A commit object.
     */
    Commit split(Commit currentBranch, Commit newBranch) {
        Commit cur = currentBranch;
        Commit newB = newBranch;
        int curlen = 0;
        while (cur != null) {
            curlen++;
            cur = cur.getParent();
        }
        int newlen = 0;
        while (newB != null) {
            newlen++;
            newB = newB.getParent();
        }
        if (curlen > newlen) {
            for (int i = 0; i < curlen - newlen; i += 1) {
                currentBranch = currentBranch.getParent();
            }
        } else {
            for (int i = 0; i < newlen - curlen; i += 1) {
                newBranch = newBranch.getParent();
            }
        }
        while (!currentBranch.equals(newBranch)) {
            currentBranch = currentBranch.getParent();
            newBranch = newBranch.getParent();
        }
        return currentBranch;
    }

    /**
     * Merge function.
     * @param nameOfBranch is name of branch.
     * @throws IOException is exceptions.
     */
    void merge(String nameOfBranch) throws IOException {
        Commit newBranch = headerBranch.get(nameOfBranch);
        Commit currentBranch = headerBranch.get(currBranch);
        checkMerge(nameOfBranch, newBranch);
        Commit split = split(currentBranch, newBranch);
        newBranch = headerBranch.get(nameOfBranch);
        currentBranch = headerBranch.get(currBranch);
        if (split.equals(currentBranch)) {
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (split.equals(newBranch)) {
            System.out.println("Given branch is "
                    + "an ancestor of the current branch.");
            return;
        }
        boolean did = false;
        for (String file : newBranch.getFiles().keySet()) {
            File name = new File(file);
            File rep = new File(".gitlet" + File.separator
                    + newBranch.getFiles().get(file));
            if (!currentBranch.contain(file)) {
                Utils.restrictedDelete(name);
                Utils.writeContents(name, Utils.readContentsAsString(rep));
            } else if (!currentBranch.modified(split, file)) {
                Utils.restrictedDelete(name);
                Utils.writeContents(name, Utils.readContentsAsString(rep));
            } else {
                did = true;
                Utils.restrictedDelete(name);
                if (name.isDirectory()) {
                    throw new IllegalArgumentException("Cannot overwrite "
                            + "directory.");
                }
                File save = new File(".gitlet" + File.separator
                        + currentBranch.getFiles().get(file));
                BufferedOutputStream string = new BufferedOutputStream(
                        Files.newOutputStream(name.toPath()));
                string.write("<<<<<<< HEAD\n".getBytes());
                string.write(Utils.readContents(save));
                string.write("=======\n".getBytes());
                string.write(Utils.readContents(rep));
                string.write(">>>>>>>\n".getBytes());
                string.close();
            }
        }
        if (did) {
            System.out.println("Encountered a merge conflict.");
        } else {
            commit("Merged" + nameOfBranch + " into " + currBranch);
        }
    }
}

