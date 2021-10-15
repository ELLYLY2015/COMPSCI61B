package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Huyen Nguyen
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new Rotor[allRotors.size()];
        int count = 0;
        for (Rotor i: allRotors) {
            _allRotors[count] = i;
            count++;
        }
    }

    /** Return the number of rotor slots I have. */
    public int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    public int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    public void insertRotors(String[] rotors) {
        _rotor = new ArrayList<>();
        ArrayList<String> mameOfRotor = new ArrayList<>();
        for (String i: rotors) {
            for (Rotor j : _allRotors) {
                String x = i;
                String y = j.name();
                if (x.toUpperCase().compareTo(y.toUpperCase()) == 0) {
                    j.set(0);
                    _rotor.add(j);
                    if (mameOfRotor.contains(i)) {
                        throw new EnigmaException(
                                "Duplicate Rotor names.");
                    } else {
                        mameOfRotor.add(i);
                    }
                }
            }
        }
        if (_rotor.size() != rotors.length) {
            throw new EnigmaException("bad rotor name");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    public void setRotors(String setting) {
        if (setting.length() != (numRotors() - 1)) {
            throw new EnigmaException("Settings is too short");
        }
        if (!_rotor.get(0).reflecting()) {
            throw new EnigmaException("Reflector is missing.");
        } else {
            if (setting.length() == (numRotors() - 1)) {
                for (int i = 1; i < _rotor.size(); i++) {
                    if (i < numRotors() - numPawls()) {
                        if (!_rotor.get(i).reflecting()
                                && !_rotor.get(i).rotates()) {
                            _rotor.get(i).set(_alphabet.toInt
                                    (setting.charAt(i - 1)));
                        } else {
                            throw new EnigmaException(" slots mismatched.");
                        }
                    } else if (i >= numRotors() - numPawls()) {
                        if (_rotor.get(i).rotates()) {
                            _rotor.get(i).set(_alphabet.toInt
                                    (setting.charAt(i - 1)));
                        } else {
                            throw new EnigmaException(" slots mismatched.");
                        }
                    }
                }
            } else {
                throw new EnigmaException("Length of settings is not correct.");
            }
        }
    }


    /** Set the plugboard to PLUGBOARD. */
    public void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    public int convert(int c) {
        advanceRotors();
        c = c % _alphabet.size();
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        for (int i = _rotor.size() - 1; i >= 0; i--) {
            Rotor forward = _rotor.get(i);
            c = forward.convertForward(c);
        }
        for (int j = 1; j < _rotor.size(); j++) {
            Rotor backward = _rotor.get(j);
            c = backward.convertBackward(c);
        }
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        return c;
    }

    /** Helper function for advance rotors and double-stepping.
     */
    public void advanceRotors() {
        ArrayList<Rotor> moving = new ArrayList<>();
        for (int i = numRotors() - numPawls(); i < numRotors(); i++) {
            Rotor current = _rotor.get(i);
            if (i == (numRotors() - 1)) {
                moving.add(current);
            } else if (_rotor.get(i + 1).atNotch()
                    || moving.contains(_rotor.get(i - 1))) {
                if (!moving.contains(current)) {
                    moving.add(current);
                }
                if (_rotor.get(i).atNotch()) {
                    if (!moving.contains(_rotor.get(i - 1))) {
                        moving.add(_rotor.get(i - 1));
                    }
                }
            }
        }
        for (Rotor r: moving) {
            r.advance();
        }
    }
    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    public String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            result +=
                    _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /**
     * Common alphabet of all rotors.
     */
    private int _numRotors;
    /**
     * rotor pawls.
     */
    private int _pawls;
    /**
     * iniitializing setting of rotor.
     */
    private Permutation _plugboard;
    /**
     * Arraylist of rotors.
     */
    private ArrayList<Rotor> _rotor;
    /**
     * array of all rotors.
     */
    private Rotor[] _allRotors;
}
