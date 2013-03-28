package player;

import dict.HashTableChained;
import simp.SimpleBoard;
import list.DList;
import list.InvalidNodeException;
import list.ListNode;

public class GameTree {
	private int playerColor;
	private final static int BLACK = 0;
	private final static int WHITE = 1;

	
/**
 * This is where the alpha beta algorithm happens. chooseMove should return the BestMove with a move variable.
 * It goes through all the possible move within five seconds, unless given a certain searchDepth.
 * @param game, this is the GameBoard that your inspecting
 * @param side, white or black 
 * @param alpha, your score
 * @param beta, opponent's score
 * @param Depth chooseMove also needs to consider how far down the alpha beta algorithm your going down
 * @return, returns the BestMove. (Type Move, BestMove has variables move, and score)
 */
	public Best chooseMove(GameBoard game, int side, int alpha, int beta){
		return chooseMove(game, side, alpha, beta, -1);
	}
	
	public Best chooseMove(GameBoard game, int side, int alpha, int beta, int Depth){
		playerColor = game.playerColor;
		
		Best myBest = new Best();
		Best reply = new Best(); 
		
		SimpleBoard simp = getBoard(game);
		HashTableChained hash = new HashTableChained();
		
		int eval = evalFunction(game);
		if(eval == 5 || eval == -5){
			if(eval == 5){
				myBest.score = 5;
				return myBest;
			}
			if (eval == -5){
				reply.score = -5;
				return reply;
			}
		}
		if(side == playerColor){
			myBest.score = alpha;
		}else{
			myBest.score = beta;
		}
		
		try{
		DList<Move> legal = getLegalMoves(game);
		ListNode m = legal.front();
		for(int i = 0; i < legal.length(); i++){
			if (Depth == -1){
				long start = System.currentTimeMillis();
				long end = start + 5*1000; // 60 seconds * 1000 ms/sec
				while (System.currentTimeMillis() < end){
				    game.addMove((Move)m.item());
				    hash.insert(simp.hashCode(), evalFunction(game));
				    reply = chooseMove(game, oppColor(game), alpha, beta);
				    game.removeMove((Move)m.item());                  //create remove move method in gameboard class
				    if(side == playerColor && (reply.score >= myBest.score)){
				    	myBest.move = (Move)m.item();
				    	myBest.score = reply.score;
				    	alpha = (int) reply.score;
				    }else if (side == oppColor(game) && (reply.score <= myBest.score)){
				    	myBest.move = (Move)m.item();
				    	myBest.score = reply.score;
				    	beta = (int) reply.score;
				    }
				    if(alpha >= beta){
				    	return myBest;
				    }
				}
			}
			else{
				while(Depth > 0){
					game.addMove((Move)m.item());
				    reply = chooseMove(game, oppColor(game), alpha, beta);
				    game.removeMove((Move)m.item());                  //create remove move method in gameboard class
				    if(side == playerColor && (reply.score >= myBest.score)){
				    	myBest.move = (Move)m.item();
				    	myBest.score = reply.score;
				    	alpha = (int) reply.score;
				    }else if (side == oppColor(game) && (reply.score <= myBest.score)){
				    	myBest.move = (Move)m.item();
				    	myBest.score = reply.score;
				    	beta = (int) reply.score;
				    }
				    if(alpha >= beta){
				    	return myBest;
				    }
					
					Depth--; 
				}
			}
			m = m.next();
		}
		}catch(InvalidNodeException ee){}
		return myBest;
	}
	
	/**
	 * 
	 * @param game
	 * @return
	 */
	
	public SimpleBoard getBoard(GameBoard game){
		SimpleBoard simp = new SimpleBoard();
		ListNode p = game.playerMoves.front();
		ListNode o = game.oppMoves.front();
		try{
			for(int i = 0; i < game.playerMoves.length(); i++){
				for(int j = 0; j < game.oppMoves.length(); j++){
					Move one = (Move)p.item();
					Move two = (Move)o.item();
					simp.setElementAt(one.x1, one.y1, game.playerColor);
					simp.setElementAt(two.x1, two.y1, oppColor(game));
				}
			}
		}catch(InvalidNodeException ee){}
		return simp;
	}
	
/**Go through the list of the playersMoves.
 * getting all the legal moves, all boxes are possible except for:
 * 1. if the box is already occupied.
 * 2. if the box is on the goal of the other opponent.
 * 3. if the putting it in the box forms a cluster of three. 
 * 
 * if there are no moves made by the player at the time, all boxes except the other goals are legal.
 *   but also should check if the box is occupied. 
 * @return a list of all the legal moves. 
 */
    public DList<Move> getLegalMoves(GameBoard game){
    	//if the player hasn't made any moves yet, you just check all the boxes
    	// and insert ones that aren't occupied, isn't a corner, and isn't in the other Goal. 
    	DList<Move> legalMoves = new DList<Move>();
    	if(game.playerMoves.length() == 0){
    		for (int i = 0; i < 8; i++){
    			for(int j = 0; i < 8; i++){
    				Move m = new Move(i,j);
    				if(!isOtherGoal(game, m) && !isOccupied(game, m) && !isCorners(m)){
    					legalMoves.insertFront(m);
    					}
    				}
    			}
    		return legalMoves;
    		}
    	//if some progress has been made in the game you want to check all the above, plus the cluster invariant.  
    	else{
    		for (int i = 0; i < 8; i++){
    			for(int j = 0; i < 8; i++){
    				Move m = new Move(i,j);
    				if(!isOtherGoal(game, m) && !isOccupied(game, m) && !isCorners(m) && notCluster(game, m)){
    					legalMoves.insertFront(m);
    					}
    				}
    			}
    		return legalMoves;
    	}
    }
    
    
    public boolean isOccupied(GameBoard game, Move moves){
    	ListNode list1 = game.playerMoves.front();
    	ListNode list2 = game.oppMoves.front();
    	try{
    	for(int i = 0; i < game.playerMoves.length(); i++){
    		for(int j = 0; j < game.oppMoves.length(); j++){
    			if(moves.equals(list1.item())){
    				return true;
    			}
    			if(moves.equals(list2.item())){
    				return true;
    			}
    			list1 = list1.next();
    			list2 = list2.next();
    		}
    	}
    	}
    	catch(InvalidNodeException e){
    		}
    	return false;
    	}
    
    /**
     * Checks if the move given is the other person's goal place or not.
     * @param m
     * @return boolean which tell you if the move is a place goal for the current player or not.
     */
    public boolean isOtherGoal(GameBoard game, Move m){
    	if (game.playerColor == WHITE){
    		//checks the fist row, excluding the corners.
    		for(int i = 1; i < 7; i++){
    			Move row1 = new Move(i, 0);
    			if (m.equals(row1))
    				return true;
    		}
    		//checks the last row, excluding the corners.
    		for (int i2 = 1; i2 < 7; i2++ ){
    			Move row7 = new Move(i2, 7);
    			if (m.equals(row7))
    				return true;
    		}
    	}
    	if (game.playerColor == BLACK){
    		//checks the first column, excluding the corners.
    		for (int j = 1; j < 7; j++){
    			Move column1 = new Move(0, j);
    			if(m.equals(column1))
    				return true;
    			}
    		//checks the last column, excluding the corners.
    		for (int j2 = 1; j2 < 7; j2++){
    			Move column2 = new Move(7, j2);
    				if (m.equals(column2))
    					return true;
    			}
    		}
    	return false;
    
    		}
   /**
    * @param m
    * @return Boolean which checks if the move is a corner or not. 
    */
    public boolean isCorners(Move m){
    	Move one = new Move(0,0);
    	Move two = new Move(0,7);
    	Move three = new Move(7,0);
    	Move four = new Move(7,7);
    	if(m.equals(one) || m.equals(two) || m.equals(three) || m.equals(four)){
    		return true;
    	}
    	return false;
    	
    }
    
  /**
   * Check the surroundings of the given Move m.
   * 	If there is two occupied, then you return false.
   * 	If there is one occupied , then you want to check it's surrounding and if there is one found, then return false.
   * Otherwise, it means there is none, so return true.
   * 
   * @param m
   * @return
   */
    public boolean notCluster(GameBoard game, Move m){
    	int w = m.x1;
    	int h = m.y1;
    	int x = 0;
    	int y = 0;
    	int number = 0; 
    	DList<Move> s = new DList<Move>();
    		  
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
    			if (isOccupied(game, k)){
    				number++;
    				s.insertFront(k);
    			}
    	}
		if(number >= 2){
    		return false;
    	}
		if(number == 1){
			try{
				Move hello = (Move) (s.front().item());
				return notCluster(game, hello); 
			}
			catch(InvalidNodeException e){}
		}
		return true;
    	
    }
    
    /**This is the evaluation function used for the chooseMove() method in this class.
     * The scored will vary from -5 <= x <= 5.
     * If you are the side that the chooseMove() passes in, then you want to get the maximum score.
     * If you are on the opponent side, then you want to get the minimum score. 
     *  	side == WHITE or BLACK.
     * To actually implement this method:
     *  1.If you already has a network, then the max score possible is assigned to the board, 5.
     *    However if the opponent has a winning network, then the lowest possible score is assigned to the board, -5.
     *  2.Otherwise, (count how many pairs of Networks you have) - (count how many pairs of network the opponent has).
     *  	If the number is negative, means opponent is winning. Bigger the negative, lower the evaluation score.
     *  	The biggest gap that can occur is -5, meaning you have formed a 0 network, while they have a network of 5,
     *  		that is right before they win.  
     * 		If the number is positive, means you are winning. Bigger the positive, higher the evaluation score.
     * 		The biggest gap here is also 5. 
     *  3. Whatever you get form the subtractions is the score the board gets. 
     * @param game The GameBoard that you want to evaluate. 
     * @param side The side that you are on. 
     * @return return a score of the board. If you are doing good, then its positive, if they are doing good then negative.
     */
    public int evalFunction(GameBoard game){
    	MoveSequence network = new MoveSequence(game);
    	DList<Move> yourGoal = isGoal(game.playerMoves, game.playerColor);
    	DList<Move> oppGoal = isGoal(game.oppMoves, oppColor(game));
    	
    	try{
    	DList<Move> YourMoves =  network.FormNetwork(game.playerMoves, (Move)yourGoal.front().item());
    	DList<Move> OppMoves =  network.FormNetwork(game.oppMoves, (Move)oppGoal.front().item());
 
    	
    	int difference = YourMoves.length() - OppMoves.length();
    	
    	if (network.isNetwork(YourMoves)){
    		return 5;
    	}
    	if (network.isNetwork(OppMoves)){
    		return -5;
    	}
    	return difference; 
    }catch(InvalidNodeException ee){}
    	return 0; 
    }
    
    
    /**This method insures that when your forming your network, the list that you pass in is always starting
     * from one of the goals.
     * 
     * @return an object move which is in the goal.
     */
    public DList<Move> isGoal(DList<Move> G, int side){
    	DList<Move> Goals = new DList<Move>();
    	ListNode front = G.front();
    	try{
    		for (int i = 0; i < G.length(); i++){   
    			if (side == WHITE){
    				for (int j = 1; j < 7; j++){
    					Move m = new Move(0,j);
    	    			Move n = new Move(7,j);
    	    			if(m.equals(front.item()) || n.equals(front.item())){
    	    				Goals.insertFront((Move)front.item());
    	    			}else{
    	    				front = front.next();
    	    			}
    				}
    				return Goals;
    			}
    			if(side == BLACK){
    				for(int j = 1; j < 7; j++){
        				Move m = new Move(j,0);
        				Move n = new Move(j,7);
        				if (m.equals(front.item()) || n.equals(front.item())){
        					Goals.insertFront((Move)front.item());
        				}else{
        					front = front.next();
        				}
    				}
    				return Goals;
    			}
    		}
    	}catch(InvalidNodeException ee){}
    	return Goals;
    }
    
    public int oppColor(GameBoard game){
    	int color = game.playerColor;
    	if(color == WHITE){
    		return BLACK;
    	}else {
    		return WHITE;
    	}
    }


//class bracket
}

  
 
