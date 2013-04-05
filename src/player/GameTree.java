package player;
 
import player.list.List;
 
public class GameTree {
    private GameBoard currentBoard;
    private int depth;
    boolean side;
    
    /**
    * A Constructor for the GamreTree class.
    * @param g The GameBoard to be used. 
    * @param Depth The Specified depth.
    */
    public GameTree(GameBoard g, int Depth){
            currentBoard = g;
            this.depth = Depth;
    }

    /**
    * This is where the alpha beta algorithm happens. chooseMove should return the BestMove with a move variable.
    * @param side, white or black
    * @param alpha, your score
    * @param beta, opponent's score
    * @return, returns the BestMove. (Type Move, BestMove has fields move and score)
    */
    public Best chooseMove(boolean side, int alpha, int beta){
        Best myBest = new Best();
        Best reply = new Best();

        int eval = currentBoard.evaluate();
        
        if (eval >= 10000000) {
            myBest.score = eval;
            return myBest;
        }
        if (eval <= -10000000) {
            reply.score = eval;
            return reply;
        }

        if (side) {
            myBest.score = alpha;
        } else {
            myBest.score = beta;
        }

        List<Move> legal = getLegalMoves(currentBoard);
        for (Move m: legal) {
            currentBoard.addMove(m);
            reply = chooseMove(!side, alpha, beta);
            currentBoard.removeMove(m);

            myBest.move = m;
            myBest.score = reply.score;
            if (side && (reply.score > myBest.score)) {
                alpha = (int) reply.score;
            }
            else if (!side && (reply.score < myBest.score)) {
                beta = (int) reply.score;
            }
            if (alpha >= beta) {
                return myBest;
            }
        }
        return myBest;
    }
       
    /**Go through the list of the playersMoves.
    * getting all the legal moves, all boxes are possible except for:
    * 1. if the box is already occupied.
    * 2. if the box is not on the goal of the other opponent.
    * 3. if putting it in the box forms a cluster of three.
    * 4. if its in one of the corners.
    *
    *Also gets the playerMoves and adds them as step moves if they are valid. 
    * @return a list of all the legal moves.
    */
    
    private List<Move> getLegalMoves(GameBoard game) {
        List<Move> legalMoves = new List<Move>();
        List<Move> stepMoves = new List<Move>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Move m = new Move(i,j);
                if (currentBoard.isEdgeMove(m) && 
                   !currentBoard.isOccupied(m.x1, m.y1) && 
                   !isCorners(m) && 
                   notCluster(m)) {
                    legalMoves.insertFront(m);
                }
                for(Move k : game.playerMoves){
                	Move oldMove = new Move(k.x1, k.x2);
                	currentBoard.removeMove(k);
                	if(currentBoard.isEdgeMove(oldMove) && 
                		!currentBoard.isOccupied(m.x1, m.y1) && 
                		!isCorners(oldMove) && 
                		notCluster(oldMove)){
                		Move h = new Move(m.x1, m.y1, oldMove.x1, oldMove.y1);
                		stepMoves.insertFront(h);
                	}
                	currentBoard.addMove(k);
                }
                
            }
        }
        legalMoves.insertListEnd(stepMoves);
        return legalMoves;
    }

                       
   
    /**
    * This method checks if the Move is in one of the corners or not. 
    * @param m The Move with x and y coordinates. 
    * @return Boolean which checks if the move is a corner or not.
    */
    private boolean isCorners(Move m){
        Move one = new Move(0,0);
        Move two = new Move(0,7);
        Move three = new Move(7,0);
        Move four = new Move(7,7);
        if (currentBoard.movesEqual(m, one) ||
            currentBoard.movesEqual(m, two) || 
            currentBoard.movesEqual(m, three) || 
            currentBoard.movesEqual(m, four) ) {
                return true;
        }
        return false;
    }
   
    /**
     * Check the surroundings of the given Move m.
     *    If there is two occupied, then you return false.
     *    If there is one occupied , then you want to check it's surrounding and if there is one found, then return false.
     * Otherwise, it means there is none, so return true.
     *
     * @param m The move you want to check its surroundings for. 
     * @return boolean Returns true if the move would not form a cluster and returns true otherwise. 
     */
    private boolean notCluster(Move m){
        int w = m.x1;
        int h = m.y1;
        int x = 0;
        int y = 0;
        int number = 0;
        List<Move> s = new List<Move>();
                 
        for (int i = 0; i < 8; i++){
            switch(i) {
                case(0):
                        x = w-1;
                        y = h-1;
                        break;
                case(1):
                        x = w;
                        y = h - 1;
                    break;
                case(2):
                        x = w + 1;
                        y = h - 1;
                        break;
                case(3):
                        x = w -1;
                    y = h;
                        break;
                case(4):
                        x = w + 1;
                        y = h;
                        break;
                case(5):
                        x = w - 1;
                        y = h + 1;
                        break;
                case(6):
                        x = w;
                        y = h +1;
                        break;
                case(7):
                        x = w + 1;
                        y = h + 1;
                        break;
                default:
                        System.out.println("Something wrong with checking the surroundings!");
                        System.exit(1);
                        break;
            }
            Move k = new Move(x,y);
            if (moveExists(k)) {
                if (currentBoard.isOccupied(k.x1, k.y1)){
                    number++;
                    s.insertFront(k);
                }
            }
        }
        if(number >= 2){
            return false;
        }
        if (number == 1) {
            Move hello = (Move) (s.get(0));
            return notCluster(hello);
        }
        return true;
    }
   
    /**
     * A method used in the cluster method, checks if the move is actually a move within the board.
     * @param m The Move to check
     * @return boolean Returns true if the Move Exists and false otherwise. 
     */
    private boolean moveExists(Move m){
        int x = m.x1;
        int y = m.y1;
        if (x > 7 || x < 0 || y > 7 || y < 0) {
                return false;
        }
        return true;
    }
}