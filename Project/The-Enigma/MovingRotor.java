package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Huyen Nguyen
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    /**
     * get the notch.
     * @return a notch
     */
    String getMyNotches() {
        return _notches;
    }
    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        char c = permutation().alphabet().toChar(setting());
        int index = getnotches().indexOf(c);
        return  index != -1;
    }
    @Override
    void advance() {

        set(permutation().wrap(setting() + 1));
    }

    /**
     * notches.
     * @return notches
     */
    public String getnotches() {
        return _notches;
    }
    /**
     * notches.
     */
    private String _notches;
    /**
     * permutation.
     */
    private Permutation _permutation;
}
