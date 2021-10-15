/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Huyen Nguyen
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moveLimit = DEFAULT_MOVE_LIMIT;
        _moves.clear();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Square p = Square.sq(row, col);
                this.set(p, contents[col][row], side);
            }
        }
        _turn = side;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
        _subsetsInitialized = false;
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }

        for (int i = 0; i < this._board.length; i++) {
            this._board[i] = board._board[i];
        }
        for (int i = 0; i < board._moves.size(); i++) {
            this._moves.add(i, board._moves.get(i));
        }
        _turn = board._turn;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        this._board[sq.index()] = v;
        if (next != null) {
            this._turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moves.add(move);
        if (get(move.getFrom()) == get(move.getTo()).opposite()) {
            move = Move.mv(move.getFrom(), move.getTo(), true);
        }
        _replaced = get(move.getTo());
        movedPiece = get(move.getFrom());
        set(move.getTo(), get(move.getFrom()));
        set(move.getFrom(), EMP);
        if (piecesContiguous(turn())) {
            _winner = turn();
            _winnerKnown = true;
            return;
        }
        if (piecesContiguous(turn().opposite())) {
            _winnerKnown = true;
            _winner = turn().opposite();
            return;
        }
        if (_moves.size() == _moveLimit) {
            winner();
            return;
        }
        _winnerKnown = false;
        _turn = _turn.opposite();
        _subsetsInitialized = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;

        Move move = _moves.remove(_moves.size() - 1);
        if (move.isCapture()) {
            move = Move.mv(move.getFrom(), move.getTo(), false);
        }
        set(move.getTo(), _replaced);
        set(move.getFrom(), movedPiece);
        _turn = _turn.opposite();
        _subsetsInitialized = false;
        _winnerKnown = false;
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (_moves.size() < _moveLimit && get(from)
                != EMP && to != null && from != null) {
            return (!blocked(from, to));
        }
        return false;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> sequenceOfMove = new ArrayList<>();
        Piece cur = this._turn;
        if (cur == EMP) {
            return sequenceOfMove;
        }
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Square sq = Square.sq(col, row);
                if (_board[sq.index()] == cur) {
                    for (int dir = 0; dir < 8; dir++) {
                        int stepOfMove = countPieces(sq, dir);
                        if (isLegal(sq, sq.moveDest(dir, stepOfMove))) {
                            sequenceOfMove.add(Move.mv(sq,
                                    sq.moveDest(dir, stepOfMove)));
                        }

                    }
                }
            }
        }
        return sequenceOfMove;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!piecesContiguous(turn()) && !piecesContiguous(turn().opposite())
                && _moves.size() < _moveLimit) {
            _winner = null;
            _winnerKnown = false;
        }
        if (this.piecesContiguous(_turn)) {
            _winnerKnown = true;
            _winner = _turn;
        }
        if (this.piecesContiguous(_turn.opposite())) {
            _winnerKnown = true;
            _winner = _turn.opposite();
        }
        if (_moves.size() == _moveLimit
                && (!piecesContiguous(WP) && (!piecesContiguous(BP)))) {
            _winner = EMP;
            _winnerKnown = false;
        }

        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {

        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        if (!inBound(from) || !inBound(to)) {
            return true;
        }
        String p = get(from).fullName();
        if (get(from) == EMP) {
            return true;
        }
        int direction = from.direction(to);
        int len = countPieces(from, from.direction(to));
        if (_moves.size() == _moveLimit) {
            return true;
        }
        for (int i = 1; i < len; i++) {
            if (get(from.moveDest(direction, i)) == get(from).opposite()
                    || from.distance(to) < len || from.distance(to) > len) {
                return true;
            }
        }
        return get(from.moveDest(direction, len)) == get(from);
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (_board[sq.index()] != p || p == EMP) {
            return 0;
        }
        int count = 1;
        if (visited[sq.row()][sq.col()]) {
            count = 0;
        }
        visited[sq.row()][sq.col()] = true;
        for (Square to : sq.adjacent()) {
            if (!(visited[to.row()][to.col()]) && get(to) == p) {
                count += numContig(to, visited, p);
            }
        }
        return count;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _subsetsInitialized = true;
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        traverse = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Square sq = Square.sq(row, col);
                String p = _board[sq.index()].fullName();
                int contig = numContig(sq, traverse, get(sq));
                if (get(sq) == WP && contig != 0) {
                    _whiteRegionSizes.add(contig);
                } else if (get(sq) == BP && contig != 0) {
                    _blackRegionSizes.add(contig);
                }
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** Count number of piece on the specific direction.
     * @param s is Piece.
     * @param direction is dir of s.
     * @return number of Piece.
     * */
    public int countPieces(Square s, int direction) {
        int oppositeDirection;
        switch (direction) {
        case 0:
            oppositeDirection = 4;
            break;
        case 1:
            oppositeDirection = 5;
            break;
        case 2:
            oppositeDirection = 6;
            break;
        case 3:
            oppositeDirection = 7;
            break;
        case 4:
            oppositeDirection = 0;
            break;
        case 5:
            oppositeDirection = 1;
            break;
        case 6:
            oppositeDirection = 2;
            break;
        default:
            oppositeDirection = 3;
        }
        int count = 1;
        Square sq = s.moveDest(direction, 1);
        while (sq != null) {
            if (get(sq) != EMP) {
                count++;
            }
            sq = sq.moveDest(direction, 1);
        }
        Square sb = s.moveDest(oppositeDirection,  1);
        while (sb != null) {
            if (get(sb) != EMP) {
                count++;
            }
            sb = sb.moveDest(oppositeDirection, 1);
        }
        return count;
    }
    /** Funcion check a square is in bound or not.
     * @param sq is current square.
     * @return true or false.
     * */
    public boolean inBound(Square sq) {
        return sq.row() >= 0 && sq.row() < BOARD_SIZE && sq.col() >= 0
                && sq.col() < BOARD_SIZE;
    }
    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };
    /** Store the replaced piece. */
    private Piece _replaced;
    /** Store the movedPiece. */
    private Piece movedPiece;
    /** This is count number of Contig*/
    /**Store the position of node which was traverse. */
    private boolean[][] traverse;
    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown = false;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
    /** get white node.
     * @return arraylist.
     * */
    public ArrayList<Integer> getWhite() {
        return _whiteRegionSizes;
    }
    /** get black node.
     * @return arraylist.
     * */
    public ArrayList<Integer> getblack() {
        return _blackRegionSizes;
    }
    /**
     * This make score for each depth.
     * @param team is curr Piece.
     * @return score.
     */
    public int score(Piece team) {
        this.computeRegions();
        if (_whiteRegionSizes.size() == 0
                || _blackRegionSizes.size() == 0) {
            return 0;
        }
        if (_blackRegionSizes.get(team.ordinal()) == 0
                || _whiteRegionSizes.get(team.ordinal()) == 0) {
            return 0;
        }

        int c = 0;
        int netScore = 0;
        traverse = new boolean[BOARD_SIZE][];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.println(Square.sq(turn().fullName()).row());
                System.out.println(this.numContig(
                        Square.sq(turn().fullName()), traverse, team));
                if (team == WP) {

                    netScore += this.numContig(
                            Square.sq(turn().fullName()), traverse, team);
                    c++;
                } else {
                    netScore += (_blackRegionSizes.size());
                    c++;
                }
            }

        }

        return (netScore + (int) Math.random()) / c;

    }

    /**
     * find distance between 2 points.
     * @param from is from point.
     * @param to is to point.
     * @return distance
     */
    public double distane(Square from, Square to) {
        int dx = Math.abs(from.row() - to.row());
        int dy = Math.abs(from.col() - to.col());
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Find sum of distance of white or black.
     * @param p is piece
     * @return sum of distance.
     */
    public int sumDistance(Piece p) {
        findRegion();
        int sum = 0;
        int xB = blackR.get(0);
        int yB = blackC.get(0);
        int xW = whiteR.get(0);
        int yW = whiteC.get(0);
        if (p == WP) {
            for (int i = 1; i < whiteR.size(); i++) {
                int x = whiteR.get(i);
                int y = whiteC.get(i);
                sum += distane(Square.sq(xW, yW), Square.sq(x, y));
            }
        } else {
            for (int i = 1; i < blackR.size(); i++) {
                int x = blackR.get(i);
                int y = blackC.get(i);
                sum += distane(Square.sq(xB, yB), Square.sq(x, y));
            }
        }
        return sum;
    }

    /**
     * store all points that are same color.
     */
    void findRegion() {
        blackC = new ArrayList<>();
        blackR = new ArrayList<>();
        whiteC = new ArrayList<>();
        whiteR = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Square sq = Square.sq(col, row);
                if (_board[sq.index()] == WP) {
                    whiteR.add(row);
                    whiteC.add(col);
                } else if (_board[sq.index()] == BP) {
                    blackR.add(row);
                    blackC.add(col);
                }
            }
        }
    }

    /** all point that the same color. */
    private ArrayList<Integer> blackR, whiteR, blackC, whiteC;
}
