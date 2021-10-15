package gitlet;



import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author huyen nguyen
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String...args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        boolean unchange = false;
        Command command = new Command();
        File file = new File(".gitlet"
                + File.separator + "gitletMain.ser");
        if (file.exists()) {
            command = Utils.readObject(file, Command.class);
        }
        switch (args[0]) {
        case "init":
            File dir = new File(".gitlet");
            if (dir.exists()) {
                System.out.println("A gitlet version control system "
                        + "already exists in the current directory");
                System.exit(0);
            }
            dir.mkdir();
            command = Command.init(); break;
        case "add":
            command.add(args[1]); break;
        case "commit":
            if (args.length == 1) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            } else if (args[1].isEmpty()) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            }
            command.commit(args[1]); break;
        case "find":
            command.find(args[1]); unchange = true; break;
        case "log": command.log(); break;
        case "global-log":
            command.logGlobal(); break;
        case "checkout":
            Command.checkout(command, args); break;
        case "rm": command.rm(args[1]); break;
        case "status":
            command.status(); unchange = true; break;
        case "branch":
            command.addNewBranch(args[1]); break;
        case "rm-branch":
            command.removeBranch(args[1]); break;
        case "reset":
            command.reset(args[1]); break;
        case "merge": command.merge(args[1]); break;
        default:
            System.out.println("No command with that name exists.");
        }
        if (unchange) {
            return;
        }
        File save = new File(".gitlet"
                + File.separator + "gitletMain.ser");
        Utils.writeObject(save, command);
    }
}
