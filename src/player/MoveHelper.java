package player;

/**
 *
 * @author Paymon
 */
public class MoveHelper {
    
    private static final MoveHelper INSTANCE = new MoveHelper();
    
    public static MoveHelper getInstance() {
        return INSTANCE;
    }
    
    public static boolean isValid(Move m) {
        return true;
    }
    
    public static MoveSequence getMoveSequence(Move m) {
        return new MoveSequence();
    }
    
    
}
