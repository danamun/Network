package player;

import player.list.List;
import player.list.MoveSequence;

/**
 *
 * @author Paymon
 */
public class GameBoard {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    
    private int playerColor;
    protected List<Move> playerMoves, oppMoves;
    // allows for constant lookup time on moves
    // row - x | col - y
    private int[][] moves;
    
    /**
     * This is a constructor of the GameBoard class which stores an empty list of the current player's moves 
     * and the opponent player's moves.
     * @param playerColor This is the player's Color
     **/
    
    public GameBoard(int playerColor) {
        this(new List<Move>(), new List<Move>(), playerColor);
    }
    
    /**
     * This is also a constructor for the GameBoard clss. It creates an 8*8 board then adds the moves to the corresponding list.
     * Creates a board where the current player and the opponent player has made some moves already.
     * @param pMove Current player's moves.
     * @param oMove Opponent player's moves.
     * @param playerColor the current player's color.
     **/
     
    public GameBoard(List<Move> pMove, List<Move> oMove, int playerColor) {
        this.playerColor = playerColor;
        this.playerMoves = pMove;
        this.oppMoves = oMove;
        
        moves = new int[8][8];
        
        for (int x = 0; x < moves.length; x++) {
            for (int y = 0; y < moves[0].length; y++) {
                moves[x][y] = -1;
            }
        }
        
        for (Move m : pMove) { 
            addMove(m, playerColor);
        }
        for (Move m : oMove) {
            addMove(m, 1 - playerColor);
        }
    }
    
    /*
     * helper method that adds a move to the moves array
     *@param m The Move that your going to add.
     *@param color The integer representing the player. 
     */
    private void addMove(Move m, int color) {
        moves[m.x1][m.y1] = color;
    }
    
    /** 
     * This method checks if the current player's Move is being put in the right place in the edges of the board.
     * The black player can only put moves where y = 0 or 7, and white player can only put moves where x = 0 or 7.
     * @param m The Move with the x and y coordinates.
     * @retun boolean Returns whether the Moves is in it's own edge or not.
     * */
    public boolean isEdgeMove(Move m) {
        if ((m.y1 == 0 || m.y1 == 7) || (m.x1 == 0 || m.x1 == 7)) {
            return true;
        }
        return false;
    }
    
    /*
     * Adds a move to playerMoves
     * If the move already has been played, 
     * then it will not be added
     * returns true if the m is succesfully added
     *@param m The Move it's going to add the player's list
     *@return boolean Returns true if Move m has been added and returns false otherwise
     */
    public boolean addMove(Move m) {
        if (isValid(m)) {
            if (m.moveKind == Move.STEP) {
                moves[m.x2][m.y2] = -1;
                int index = 0;
                for (Move m1 : playerMoves) {
                    if (movesEqual(m1, m)) {
                        playerMoves.remove(index);
                    }
                    index++;
                }
            }
            playerMoves.insertEnd(m);
            moves[m.x1][m.y1] = playerColor;
            return true;
        }
        return false;
    }
    
    /**
     * see addMove() 
     * same as that, except for the opponent
     * @param m The Move your going to add to the opponent's list.
     * @return boolean Alse returns true if the Move has been added successfully and false otherwise
     **/
    public boolean addOpponentMove(Move m) {
        if (isValid(m)) {
            if (m.moveKind == Move.STEP) {
                moves[m.x2][m.y2] = -1;
                int index = 0;
                for (Move m1 : oppMoves) {
                    if (movesEqual(m1, m)) {
                        oppMoves.remove(index);
                    }
                    index++;
                }           
            }
            oppMoves.insertEnd(m);
            moves[m.x1][m.y1] = 1 - playerColor;
            return true;
        }
        return false;
    }
    
    /**
     * removes a move made by the current player
     * @param m - the move to remove
     */
    public void removeMove(Move m) { 
        moves[m.x1][m.y1] = -1;
        int index = 0;
        for (Move m1 : oppMoves) {
            if (movesEqual(m1, m)) {
                oppMoves.remove(index);
            }
            index++;
        }
    }
    
    /**
     * removes a move made by the opponent player
     * @param m - the move to remove
     */
    public void removeOppMove(Move m) { 
        moves[m.x1][m.y1] = -1;
        int index = 0;
        for (Move m1 : playerMoves) {
            if (movesEqual(m1, m)) {
                playerMoves.remove(index);
            }
            index++;
        }
    }
    
    
    public boolean isValid(Move m) {
        return true;
    }
    
    /**
     * constructs and returns a List 
     * with the different MoveSequences that m is in
     * m MUST be an edge move
     * @param m The list is constructed based on this move given.
     * @return List Returns a list of all the MoveSequences made.
     */
    public List<MoveSequence> getMoveSequences(Move m) {
        return getMoveSequences(new MoveSequence(playerColor), m);
    }
    
    /**
     * *helper method*
     * constructs and returns a List
     * with the different MoveSequences that m is in
     * m does not need to be an edge move
     * this relies on a MoveSequence parent
     * and will be ignored in the creation of the movesequences
     * @param parent 
     * @param m The List is constructed based on this move.
     * @return List Returns a list of all the MoveSequence. 
     */
    private List<MoveSequence> getMoveSequences(MoveSequence parent, Move m) {
        List<MoveSequence> sequence = new List<MoveSequence>();
        
        parent.insertEnd(m);
        
        List<Move> neighbors = getNeighbors(m, parent.getColor());
        
        for (int i = 0; i < neighbors.size; i++) {
            if (movesEqual(neighbors.get(i), parent.backItem())) {
                continue;
            }
            List<MoveSequence> ms = getMoveSequences(parent, neighbors.get(i));
            sequence.insertListEnd(ms);
        }
        
        return sequence;
    }
    
    /**
     * This method gets a List of all the networks that Move m can make with chips with the same color.
     * Checks for all the Moves in the GameBoard with the same chip color, make sure nothing gets in the way 
     * of making the network, checks if it goes diagonally or horizontally, and if all the invariants are
     * satisfied, it stores that other move in a list and returns it. 
     * @param m The main Move where your trying to find the neighbors of. 
     * @param pColor the color of the chips. 
     * @return List Returns a List of all the neighbors of Move m
     */
    public List<Move> getNeighbors(Move m, int pColor) {
        List<Move> neighbors = new List<Move>();
        
        for (int x = 0; x < moves.length; x++) {
            for (int y = 0; y < moves[0].length; y++) {
                if (moves[x][y] != pColor) {
                    continue;
                }
                // check if the playerMove is horizontally or diagonally related
                if ((x == m.x1 ^
                    y == m.y1) ||
                    Math.abs(x - m.x1) == Math.abs(y - m.y1)) {

                    boolean occupied = false;
                    for (int x2 = m.x1; x2 < x; x2++) {
                        for (int y2 = m.y1; y2 < y; y2++) {
                            if (isOccupied(x2, y2)) {
                                occupied = true;
                                break;
                            }
                        }
                        if (occupied) {
                            break;
                        }
                    }

                    if (!occupied) {
                        neighbors.insertEnd(new Move(x, y));
                    }
                }
            }
        }
        
        return neighbors;
    }
    
    /**
     * A method that checks if the grid is occupied. 
     * @param x, y The place of the grid
     * @return boolean Returns true if the cell at x, y is occupied and false otherwise. 
     */
    public boolean isOccupied(int x, int y) {
        return moves[x][y] >= 0;
    }
    
    /**
     * evaluate the grid, giving it a score 
     * on how good the next move is.
     * If a current player has a network it returns 10000000, and -10000000 if the opponent has a network.
     * @return int This is the evaluation score of the GameBoard. 
     */
    public int evaluate() {
        int score = 0;

        for (Move m : playerMoves) {
            List<Move> nlist = getNeighbors(m, playerColor);
            score += nlist.size;
            
            if (isEdgeMove(m)) {
                List<MoveSequence> sequences = getMoveSequences(m);
                for (MoveSequence ms : sequences) {
                    if (ms.isNetwork()) {
                        score += 100000000;
                    }
                    score += ms.size * 10000;
                }
            }
        }
        
        for (Move m : oppMoves) {
            List<Move> nlist = getNeighbors(m, 1 - playerColor);
            score -= nlist.size;
            
            if (isEdgeMove(m)) {
                List<MoveSequence> sequences = getMoveSequences(m);
                for (MoveSequence ms : sequences) {
                    if (ms.isNetwork()) {
                        score -= 100000000;
                    }
                    score -= ms.size * 10000;
                }
            }
        }
        
        return score;
    }
    
    /**
     * normally this would be implemented as the equals() method in Move,
     * however we are not allowed to edit the Move class
     * 
     * @param m1 - the first move to be compared 
     * @param m2 - the second move to be compared
     */
    public boolean movesEqual(Move m1, Move m2) {
        return m1.x1 == m2.x1 && m1.y1 == m2.y1;
    }
    
    /**
     * returns the playerColor
     */
    public int getPlayerColor() {
        return playerColor;
    }
    
    /**
     * This Method checks if the Grid is full, meaning both current player and opponent player has each made ten moves.
     * @return true if the grid is full and false other wise. 
     */
    public boolean isFullGrid() {
        return playerMoves.size + oppMoves.size >= 20;
    }
}
