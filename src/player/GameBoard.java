package player;

/**
 *
 * @author Paymon
 */
public class GameBoard {
    private int playerColor;
    
    private List<Move> playerMoves, oppMoves;
    
    public GameBoard(List<Move> pMove, List<Move> oMove, int playerColor) {
        this.playerColor = playerColor;
        this.playerMoves = pMove;
        this.oppMoves = oMove;
    }
    
    public boolean addMove(Move m) {
        return true;
    }
    
    
    public Move addOpponentMove(Move m) {
        return new Move();
    }
    
    public int evaluate() {
        return 0;
    }
}
