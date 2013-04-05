package player;

/**
 *
 * @author Paymon
 */
public class MoveHelper {
    
    private static final MoveHelper INSTANCE = new MoveHelper();
    
    /**
     * Creates an instance of MoveHelper()
     * @return returns that instance
     */
    public static MoveHelper getInstance() {
        return INSTANCE;
    }
    
    /**
     * Method that tells you if the Move is valid.
     * @param m The move to be checked
     * @return boolean true
     */
    public static boolean isValid(Move m) {
        return true;
    }
    
    /**
     * Gets the Move sequence of Move m 
     * @param m The move to get the move sequence of
     * @return an instance of the move sequence
     */
    public static MoveSequence getMoveSequence(Move m) {
        return new MoveSequence();
    }
    
    
}