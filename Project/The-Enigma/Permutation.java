package enigma;

import java.util.ArrayList;



/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Huyen Nguyen
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        list = new ArrayList<Integer>();
        for (int i = 0; i < _alphabet.size(); i++) {
            list.add(i);
        }
        String[] arrayOfStrings;
        cycles = cycles.replaceAll("[)(]", "");
        arrayOfStrings = cycles.split(" ");
        for (String c: arrayOfStrings) {
            addCycle(c);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            char c = cycle.charAt(i);
            if (cycle.length() <= 0) {
                list.set(_alphabet.toInt(c),
                        _alphabet.toInt(cycle.charAt(i)));
            } else if (i != (cycle.length() - 1)) {
                list.set(_alphabet.toInt(c),
                        alphabet().toInt(cycle.charAt(i + 1)));
            } else {
                list.set(_alphabet.toInt(c),
                        alphabet().toInt(cycle.charAt(0)));
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return list.get(wrap(p));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return list.indexOf(wrap(c));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int key = _alphabet.toInt(p);
        return _alphabet.toChar(permute(key));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int key = _alphabet.toInt(c);
        return _alphabet.toChar(invert(key));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i: list) {
            if (i == list.indexOf(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Store permute list. */
    private ArrayList<Integer> list;
}
