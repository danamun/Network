package player.list;

import player.GameBoard;
import player.Move;

/*
 *
 * @author Paymon
 */
public class MoveSequence extends List<Move> {
    
    private int playerColor;
    
    /**
     * This is a constructor for the Move Sequence class. 
     * @param playerColor sets the player color 
     **/
    public MoveSequence(int playerColor) {
        super();
        this.playerColor = playerColor;
    }
    
    /**
     * Gets the player's color of the current Board.
     **/
    public int getColor() {
        return playerColor;
    }
    
    /**
     * evaluates a MoveSequence based on:
     *  - the length of the sequence
     *  - whether or not it is a network
     * @return int returns the evaluation score.
     */
    public int evaluate(){
        int score = 0;
        
        if (isNetwork()) {
            score += 100;
        }
        
        score += size;
        
        return score;
    }
    
    /**
     * Checks if the Board has a network.
     * @return boolean Returns false if there is no network and returns true if there is. 
     **/
    public boolean isNetwork() {
        if (size < 6) {
            return false;
        }
        if (playerColor == GameBoard.BLACK) { // black
            if (frontItem().y1 == 0 && backItem().y1 == 7) {
                return true;
            }
        }
        else if (playerColor == GameBoard.WHITE) { // white
            if (frontItem().x1 == 0 && backItem().x1 == 7) {
                return true;
            }
        }
        return false;
    }
}
