package enigma;


import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Huyen Nguyen
 */
class Alphabet {
    /**
     * String of Characters.
     */
    private String _chars;
    /**
     * Store characters with their indices.
     */
    private HashMap<Character, Integer> _index;
    /**
     * Store indices and their character.
     */
    private HashMap<Integer, Character> _char;
    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        if (getchars().length() == 0) {
            throw new EnigmaException("No alphabet inputs");
        }
        HashMap<Character, Integer> mapping = new HashMap<>();
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            if ((c != '*') && (!mapping.containsKey(c))) {
                mapping.put(c, i);
            }
        }
        _index = mapping;
        _char = new HashMap<>();
        for (char x : getindex().keySet()) {
            getchar().put(getindex().get(x), x);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * index from char.
     * @return char
     */
    public HashMap<Integer, Character> getchar() {
        return _char;
    }

    /**
     * char with indice.
     * @return _index
     */
    public HashMap<Character, Integer> getindex() {
        return _index;
    }

    /**
     * get chars.
     * @return chars
     */
    public String getchars() {
        return _chars;
    }
    /** Returns the size of the alphabet. */
    int size() {

        return _char.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _char.containsValue(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= 0 && index < size()) {
            return _char.get(index);
        } else {
            throw new EnigmaException("Index is out of bounds");
        }

    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (contains(ch)) {
            return _index.get(ch);
        } else {
            throw new EnigmaException("Character is not alphabet");
        }
    }
}
