package player;

import player.list.List;

/**
 *
 * @author Paymon
 */
public class GameBoard {
    private int playerColor;
    
    private List playerMoves, oppMoves;
    
    public GameBoard(int playerColor) {
        this(new List<Move>(), new List<Move>(), playerColor);
    }
    
    public GameBoard(List<Move> pMove, List<Move> oMove, int playerColor) {
        this.playerColor = playerColor;
        this.playerMoves = pMove;
        this.oppMoves = oMove;
    }
    
    /*
     * Adds a move to playerMoves
     * If the move already has been played, 
     * then it will not be added
     * returns true if the m is succesfully added
     */
    public boolean addMove(Move m) {
        if (playerMoves.contains(m)) {
            return false;
        }
        playerMoves.insertEnd(m);
        return true;
    }
    
    
    public Move addOpponentMove(Move m) {
        if (oppMoves.contains(m)) {
            return false;
        }
        oppMoves.insertEnd(m);
        return true;
    }
    
    public int evaluate() {
        return 0;
    }
}
