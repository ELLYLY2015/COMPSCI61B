package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Huyen Nguyen
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        String settings, ringsetting, line = "";
        boolean setting;
        while (_input.hasNextLine()) {
            if (!_input.hasNext("\\s*[*].*")) {
                throw new EnigmaException("Config does not have *");
            } else {
                ringsetting = "";
                settings = "";
                setting = false;

                while (!setting) {
                    line = _input.nextLine();
                    if (line.matches("[*].+")) {
                        setting = true;
                    } else {
                        printMessageLine(line);
                    }
                }
                int i = 0;
                Scanner newLine = new Scanner(line);
                String read = "";
                while ((i < (M.numRotors() + 2))
                        || newLine.hasNext("[(].+[)]")) {
                    if (!newLine.hasNext()) {
                        throw new EnigmaException("Do Not enough rotors.");
                    }
                    read = newLine.next().replaceAll("[*]", "* ") + " ";
                    settings += read;
                    i++;
                }
                if (newLine.hasNext()) {
                    ringsetting += settings.substring(0, settings.length() - 5)
                            + newLine.next().replaceAll("[*]", "* ") + " ";
                }
                setUp(M, settings.substring(0, settings.length() - 1));

                while (!_input.hasNext("[*]") && _input.hasNextLine()) {
                    String s = _input.nextLine().replaceAll("\\s+", "")
                            .toUpperCase();
                    printMessageLine(M.convert(s));
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _allrotors = new ArrayList<>();
            if (_config.hasNext("\\S+")) {
                String readfirst = _config.next();
                if (!(readfirst.startsWith("*"))) {
                    _alphabet = new Alphabet(readfirst);
                }
                if (_config.hasNextInt()) {
                    _rotors = _config.nextInt();
                    if (_config.hasNextInt()) {
                        _pawls = _config.nextInt();
                        while (_config.hasNext(".+")) {
                            _allrotors.add(readRotor());
                        }
                    } else {
                        throw new EnigmaException(""
                                + "Number of pawls did not pass.");
                    }
                } else {
                    throw new EnigmaException(
                            "Number of rotors did not pass.");
                }
            }
            return new Machine(_alphabet, _rotors, _pawls, _allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        String notches;
        String namesOfRotor;
        String cycles;
        String read;
        String s;
        try {
            namesOfRotor = _config.next().toUpperCase();
            read = _config.next();
            cycles = "";
            notches = "";
            if (read.charAt(0) == 'M') {
                notches += read.substring(1, read.length());
                while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                    s = _config.next().replaceAll("[)][(]", ") (");
                    cycles += s + " ";
                }
                return new MovingRotor(namesOfRotor,
                        new Permutation(cycles, _alphabet), notches);
            } else if (read.charAt(0) == 'N') {
                while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                    s = _config.next().replaceAll("[)][(]", ") (");
                    cycles += s + " ";
                }
                return new FixedRotor(namesOfRotor,
                        new Permutation(cycles, _alphabet));
            } else if (read.charAt(0) == 'R') {
                while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                    s = _config.next().replaceAll("[)][(]", ") (");
                    cycles += s + " ";
                }
                return new Reflector(namesOfRotor,
                        new Permutation(cycles, _alphabet));
            } else {
                throw new EnigmaException("Wrong type of rotor.");
            }
        }  catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] listOfRotors = new String[M.numRotors()];
        Scanner read = new Scanner(settings);
        String plugBoard = "";
        if (read.hasNext("[*]")) {
            read.next();
            for (int i = 0; i < M.numRotors(); i++) {
                listOfRotors[i] = read.next();
            }
            M.insertRotors(listOfRotors);
            if (read.hasNext("\\w{" + (M.numRotors() - 1) + "}")) {
                M.setRotors(read.next());
            }
            while (read.hasNext()) {
                plugBoard += read.next() + " ";
            }
            if (plugBoard.length() > 0) {
                M.setPlugboard(new Permutation(plugBoard.
                        substring(0, plugBoard.length() - 1),
                        _alphabet));
            }
        }
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String message = msg;
        if (message.length() == 0) {
            _output.println();
        } else {
            while (message.length() > 0) {
                if (message.length() <= 5) {
                    _output.println(message);
                    message = "";
                } else {
                    _output.print(message.substring(0, 5) + " ");
                    message = message.substring
                            (5, message.length());
                }
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /**
     * All rotors.
     */
    private Collection<Rotor> _allrotors;
    /**
     * pawls.
     */
    private int _pawls;
    /**
     * num rotors.
     */
    private int _rotors;

}
