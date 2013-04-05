/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
    
    private GameBoard board;
    private GameTree currentTree;

    private int playerColor;
    private int searchDepth;

    /** Creates a machine player with the given color.  Color is either 0 (black)
     * or 1 (white).  (White has the first move.)
     * @param color 
     */ 
    public MachinePlayer(int color) {
        playerColor = color;
        searchDepth = 2;
        
        board = new GameBoard(playerColor);
        currentTree = new GameTree(board, searchDepth);
      
    }

    /** Creates a machine player with the given color and search depth.  Color is
     * either 0 (black) or 1 (white).  (White has the first move.)
     * @param color
     * @param depth
     */
    public MachinePlayer(int color, int depth) {
        playerColor = color;
        searchDepth = depth;
        
        board = new GameBoard(playerColor);
        currentTree = new GameTree(board, searchDepth);
    }

    /** Returns a new move by "this" player.  Internally records the move (updates
     * the internal game board) as a move by "this" player.
     */
    public Move chooseMove() {
      return currentTree.chooseMove(true, 0, 1).move;
    } 

    // If the Move m is legal, records the move as a move by the opponent
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method allows your opponents to inform you of their moves.
    public boolean opponentMove(Move m) {
        return board.addOpponentMove(m);
    }

    // If the Move m is legal, records the move as a move by "this" player
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method is used to help set up "Network problems" for your
    // player to solve.
    public boolean forceMove(Move m) {
        return board.addMove(m);
    }

}
