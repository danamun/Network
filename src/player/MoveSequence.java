package player;

import list.DList;

import list.InvalidNodeException;
import list.ListNode;
import dict.HashTableChained;


public class MoveSequence{
	private GameBoard game;
	private int side;
	private final int BLACK = 0;
	private final int WHITE = 1;

	MoveSequence(GameBoard current){
		game = current;
		side = game.playerColor;
	}
	
/**Takes in a list of all the moves, a vertex. 
 * Forms Network based on the algorithm in Goodrich and Tamassia's book.
 * 
 * Here we will use a HashTable to store the "visited" attributes of a certain position. 
 * 		The HashCode will be implemented by using the hastable which has a comp function.
 *      
 * Invariants:
 * 1. Must have only two chips in the opposite side of the goals (inGoals)
 * 2. Cannot pass through the same chip  twice. (Decorator with key and boolean value)
 * 3. A minimum of six chips must form a network. (check if length is six)
 * 4. Can go diagonally, or straight(left, right, up, down). (GetDirection)
 * 5. Cannot have the opponent's chip blocking the network. (pathFree)
 * 6. cannot pass a chip without turning a corner. (GetDirection & pathFree)
 * 
 * 
 * for all the edges given a vertex, you want to check the direction. 
 * then keep recursively all 
 */
	public DList<Move> FormNetwork(DList<Move> G, Move v){
		HashTableChained hash = new HashTableChained();  //a hash table to store all the visited edges. 
		DList<Move> edges = Edges(G, v);           //gets a list of all the possible connections from v.
		DList<Move> network = new DList<Move>();   //creates a new DList
		network.insertFront(v);                    //inserts the v
		
		
		
		try{
		
		for (int i = 0; i < edges.length(); i++){  //try for all the edges 
			
			ListNode e = edges.front();                
			Move e1 = (Move)e.item();                 //gets the first item in the edges
			int d = GetDirection(v, e1);				//then gets the direction from v to that edge e.
			
			for (int j = 0; j < edges.length(); j++){
				DList<Move> wedges = Edges(G, e1);     //gets a list of all the possible connections from edge e1.
				ListNode w = wedges.front();			//gets the first item
				Move w1 = (Move)w.item();			 
			
				int d1 = GetDirection(e1, w1);        //get the direction from e to w
				if(d != d1){						//if the direction is different, recursively call network from w 
					FormNetwork(G, w1);
					e = e.next();					
				}
			}
			e = e.next();						// if direction is same then checks the next edge in the for loop.
		}
			
		}catch(InvalidNodeException ee){}
	}
	
/**
 * @return returns true if it is a boolean.
 */
	public boolean isNetwork(DList<Move> m){
		if(m.length() >= 6 && inGoals(m) == true){
			return true;
		}else{
		return false;
		}
	}

	
	/**
	 * This method is used in FormNetwork to check if the direction of the next chip changes.
	 * @param the first chips coordinates, and the second chip's coordinates. 
	 * @return the direction represented as an int. 
	 */

	public int GetDirection(Move move1, Move move2){
		int firstx = move1.x1;
        int firsty = move2.y1;
		int secondx = move2.x1;
		int secondy = move2.y1;
		int direction = 0;
		for (int i = 1; i <= 8; i++){
    		switch(i) {
    			case(1):
    				if (secondx < firstx && secondy > firsty);
    			  	break;
    			case(2):
    				if(secondx == firstx && secondy > firsty);
    			    break;
    			case(3):
    				if(secondx > firstx && secondy > firsty);
    			  	break;
    			case(4):
    				if(secondx > firstx && secondy == firsty);
    			  	break;
    			case(5):
    				if(secondx > firstx && secondy < firsty);
    			  	break;
    			case(6):
    				if(secondx == firstx && secondy < firsty);
    			  	break;
    			case(7):
    				if(secondx < firstx && secondy < firsty);
    			  	break;
    			case(8):
    				if(secondx < firstx && secondy == firsty);
    			  	break;
    			default:
    				i = -1;
    				break;
    			}
    		direction = i;
		}
		return direction; 
	}
    
    
	
	
	
/**
 * Checks for the invariant in the formingNetwork method where, in order to be a network, there must be two chips
 * in the opposite goals. 
 * @param networklist this is the list formed by the forming network method. 
 * @return a boolean of whether two in the front and back are in the opposite side or not. 
 */
	public boolean inGoals(DList<Move> networklist){
		ListNode front = networklist.front();
		ListNode back = networklist.back();
		
		try{
		//don't have to get the item just do .equals
		if (side == WHITE){
			for(int j = 1; j < 7; j++){
				Move m = new Move(0,j);
				Move n = new Move(7,j);
				if (m.equals(front.item()) && n.equals(back.item())){
					return true;
				}
			}
		}
		if(side == BLACK){
			for(int j = 1; j < 7; j++){
				Move m = new Move(j,0);
				Move n = new Move(j,7);
				if (m.equals(front.item()) && n.equals(back.item())){
					return true;
				}
			}
				
		}
		}catch(InvalidNodeException e){}
		
		return false;
	}
	
	
	/**
	 * This methods checks while forming a network, if there is another chip, form the opposing side, in the path.
	 * If there is, a network can't be made between the two chips and this method returns false.
	 * If there isn't then this method returns true. 
	 * This method also checks if a chip is turning a corner after every connecting chip has been connected.
	 * @param x1  The start.
	 * @param y1
	 * @param x2  The end, where you want to connect to. 
	 * @param y2
	 * @return
	 */
	public boolean rightPath(Move one, Move two){
		int direction = GetDirection(one, two);
		int x = one.x1;
		int y = one.y1;
		switch(direction){
		case 1:
			Move m = new Move(x - 1, y + 1);
			while (!m.equals(two)){
				if(!isOccupied(m)){
					x = x-1;
					y = y+1;
					m = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		case 2:
			Move k = new Move(x, y + 1);
			while (!k.equals(two)){
				if(!isOccupied(k)){
					y = y+1;
					k = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		case 3:
			Move i = new Move(x + 1, y + 1);
			while (!i.equals(two)){
				if(!isOccupied(i)){
					x = x+1;
					y = y+1;
					i = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			 break;
		case 4:
			Move r = new Move(x + 1, y + 1);
			while (!r.equals(two)){
				if(!isOccupied(r)){
					x = x+1;
					r = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		case 5:
			Move w = new Move(x + 1, y - 1);
			while (!w.equals(two)){
				if(!isOccupied(w)){
					x = x+1;
					y = y-1;
					w = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			 break;
		case 6:
			Move f = new Move(x, y - 1);
			while (!f.equals(two)){
				if(!isOccupied(f)){
					y = y-1;
					f = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		case 7:
			Move u = new Move(x - 1, y - 1);
			while (!u.equals(two)){
				if(!isOccupied(u)){
					x = x-1;
					y = y-1;
					u = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		case 8:
			Move h = new Move(x - 1, y);
			while (!h.equals(two)){
				if(!isOccupied(h)){
					x = x-1;
					h = new Move(x,y);
				}else{
					return false;
				}
			return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}
	
	
	/**
	 * This method is used to check whether a certain space is occupied or not. 
	 * @param moves
	 * @return a boolean of true is it is occupied, and false if it has not been occupied. 
	 */
    public boolean isOccupied(Move moves){
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
     * Gets all the incident edges of v from a list G.
     *
     * @param G The List
     * @param v the vertex
     * @return a list holding all the incident edges
     */
    public DList<Move> Edges(DList<Move> G, Move v){
    	DList<Move> e = new DList<Move>();
    	ListNode edge = G.front();
    	try{
    	for(int i = 0; i < G.length(); i++){
    		if(! edge.equals(v)){
    			if(rightPath(v, (Move)edge.item())){
    				e.insertFront(edge);
    				edge = edge.next();
    			}
    		}else{
    			edge = edge.next();
    		}
    	}
    	}catch(InvalidNodeException ee){}
    	
    	return e;
    }
    
	
	
//class bracket	
}
