package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import game.FindState;
import game.FleeState;
import game.NodeStatus;
import game.SewerDiver;
import game.Node;


public class DiverMin implements SewerDiver {
	
	
	List<Long> visited = new ArrayList<Long>();

	/** Get to the ring in as few steps as possible. Once you get there, <br>
	 * you must return from this function in order to pick<br>
	 * it up. If you continue to move after finding the ring rather <br>
	 * than returning, it will not count.<br>
	 * If you return from this function while not standing on top of the ring, <br>
	 * it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you know only your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * currentLocation(), neighbors(), and distanceToRing() in state.<br>
	 * You know you are standing on the ring when distanceToRing() is 0.
	 *
	 * Use function moveTo(long id) in state to move to a neighboring<br>
	 * tile by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the ring, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
	 * Some modification is necessary to make the search better, in general. */
	@Override
	public void find(FindState state) {
		
		// Dumb find method
//		dumbFind(state);
		
		// Less dumb find method
		lessDumbFind(state);
		
	}
	
	
	public void dumbFind(FindState state) {
		
		long curr_id = state.currentLocation();
		
		if (state.distanceToRing() == 0) {
			return;
		}
		
		for(NodeStatus z : state.neighbors()) {
			if (!visited.contains(z.getId())) {
				visited.add(z.getId());
				state.moveTo(z.getId());
				dumbFind(state);
				if (state.distanceToRing() == 0) {
					return;
				}
				state.moveTo(curr_id);
			}
		}
	}
	
	public void lessDumbFind(FindState state) {
		long curr_id = state.currentLocation();
		
		List<NodeStatus> sorted_neighbors = sortNeighbors(state);
		
		if (state.distanceToRing() == 0) {
			return;
		}
		
		
		for (NodeStatus nbr : sorted_neighbors) {
			if (!visited.contains(nbr.getId())) {
				visited.add(nbr.getId());
				state.moveTo(nbr.getId());
				lessDumbFind(state);
				if (state.distanceToRing() == 0) {
					return;
				}
				state.moveTo(curr_id);
				
			}
		}
	}
	
	private List<NodeStatus> sortNeighbors(FindState state) {
		
		Heap<NodeStatus, Integer> sorted_neighbors = new Heap<NodeStatus, Integer>(Comparator.reverseOrder());
		
		for (NodeStatus nbr : state.neighbors()) {
			int d = nbr.getDistanceToTarget();
			sorted_neighbors.add(nbr, d);
		}
		
		List<NodeStatus> out_list = new ArrayList<NodeStatus>();
		
		while (sorted_neighbors.size() > 0) {
			out_list.add(sorted_neighbors.poll());
		}
		
		return out_list;
	}

	
	/** Flee the sewer system before the steps are all used, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS <br>
	 * get out before the steps are all used, and this should be prioritized above<br>
	 * collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() will return Node objects<br>
	 * of interest, and getNodes() will return a collection of all nodes on the graph.
	 *
	 * You have to get out of the sewer system in the number of steps given by<br>
	 * getStepsRemaining(); for each move along an edge, this number is <br>
	 * decremented by the weight of the edge taken.
	 *
	 * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
	 * When n is moved-to, coins on node n are automatically picked up.
	 *
	 * You must return from this function while standing at the exit. Failing <br>
	 * to do so before steps run out or returning from the wrong node will be<br>
	 * considered a failed run.
	 *
	 * Initially, there are enough steps to get from the starting point to the<br>
	 * exit using the shortest path, although this will not collect many coins.<br>
	 * For this reason, a good starting solution is to use the shortest path to<br>
	 * the exit. */
	@Override
	public void flee(FleeState state) {
		
		// Dumb shortest path version
		
//		dumbFlee(state);
		
//		randomFlee(state);
		
//		getBestCoinFlee(state);
		
		getBestCoinFlee(state);
		
//		highestValPath(state);
		
//		maxPath(state);
		
		
		
		
		
		
//		//Get current location
//		Node HERE_I_AM = state.currentNode();
//		//Get exit node, to save computing time on the loops
//		Node EXIT = state.getExit();
//		//Use Dijkstra's algorithm to get the minimum distance to the exit
//	
//		int STEPS_TO_EXIT = 0;
//		
//		//While the current position is not the exit
//		while (state.currentNode() != EXIT) {
//			
//			//Flee once the length of the shortest path approaches the steps left 
//			if(state.stepsLeft() < STEPS_TO_EXIT+5) {
//				//Get a list of the nodes in the shortest path
//				List<Node> HOME_RUN = GraphAlgorithms.shortestPath(state.currentNode(), EXIT);
//				for(Node a : HOME_RUN) {
//					state.moveTo(a);
//				}
//			}
//			//Else, chart a path towards the most coins possible
//			
//		}
//		return;
		return;
 	}
	

	/** Moves Min along the path given to it, used as a helper function in the flee functions. */
	private static void moveFlee(FleeState state, List<Node> path) {
		path.remove(0);
		for (Node n : path) {
			state.moveTo(n);
		}
		
	}
	
	/** Flees using the least amount of steps possible, does not take score
	 * into account. */
	private void dumbFlee(FleeState state) {
		List<Node> shortest_path = GraphAlgorithms.shortestPath(state.currentNode(), state.getExit());
		moveFlee(state, shortest_path);
		
	}
	
	
	
	
	private void randomFlee(FleeState state) {
		

		List<Node> random_path = new ArrayList<Node>();
		Node curr_node = state.currentNode();
		Node exit = state.getExit();
		int shortest_dist = GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
		
		while (7*shortest_dist < state.stepsLeft()) {
			
			random_path.add(curr_node);
			
			List<Node> nbrs = new ArrayList<Node>(curr_node.getNeighbors());
			Collections.shuffle(nbrs);
			curr_node = nbrs.remove(0);
			
			state.moveTo(curr_node);
			shortest_dist = GraphAlgorithms.shortestPath(state.currentNode(), state.getExit()).size();
		}
		
		for (Node n : GraphAlgorithms.shortestPath(curr_node, exit)) {
			if (!n.equals(curr_node)) {
				state.moveTo(n);
			}
			
		}
		
	}
	
	
	private void getBestCoinFlee(FleeState state) {
		
		Node curr_node = state.currentNode();
		Node exit = state.getExit();
		int max_val = -1;
		int curr_coins;
		int dist_to;
		int dist_exit = GraphAlgorithms.shortestPath(curr_node, exit).size();
		Node best_node = exit;
		
		for (Node n : state.allNodes()) {
			curr_coins = n.getTile().coins();
			dist_to = GraphAlgorithms.shortestPath(curr_node, n).size();
			dist_exit = GraphAlgorithms.shortestPath(n, exit).size();
			if (curr_coins/dist_to > max_val) {
				if (dist_to + dist_exit < state.stepsLeft()/7 - 1) {
					max_val = curr_coins/dist_to;
					best_node = n;
				}
			}
		}
		moveFlee(state, GraphAlgorithms.shortestPath(curr_node, best_node));
		if (best_node != exit) {
			getBestCoinFlee(state);
		}
		
		
		
	}
	
	

	

	
	public static 
	void highestValPath(FleeState state) {
		
		// Set unvisited contains all the unvisited nodes.
		Set<Node> unvisited = new HashSet<Node>(state.allNodes());
		
		
		// parents stores the parent of a Node n as a Node.
		HashMap<Node, Node> 		parents 		= new HashMap<Node, Node>();
		
		// dist is a Heap that will contain the distance 
		// from the start node to each other node.
		Heap<Node, Double> 	dist 	= new Heap<Node, Double>(Comparator.naturalOrder());

		// Initializations of values for dist and parents.
		for (Node neighbor : unvisited) {
				dist.add(neighbor, Double.NEGATIVE_INFINITY);
			parents.put(neighbor, null);	
		}
		// set the distance of the start node to itself as 0.
		dist.changePriority(state.currentNode(),  0.0);

		// While there are still unvisited nodes, 
		while (!unvisited.isEmpty()) {				

			// node_curr denotes the current node of Dijkstra's algorithm.
			Node node_curr = dist.peek();			
			Double dist_curr = dist.priority(node_curr);
			dist.poll();
			
			// If the current node's closest neighbor is not connected, break
			if (dist_curr == Double.NEGATIVE_INFINITY) {
				break;
			}

			// Remove the current node from the unvisited set, as we are now visiting it
			unvisited.remove(node_curr);
			
			// For each neighbor of the current node,
			for (Node neighbor : node_curr.outgoing().keySet()) {
				// If neighbor is unvisited,
				if (unvisited.contains(neighbor)) {
					// Set the variable new_weight to equal the current distance from
					// start to node_curr plus the edge weight from the current node
					// to neighbor.
					double new_weight = dist_curr 
										+ node_curr.outgoing().get(neighbor).label();
					// If new weight is less than the current weight of the neighbor
					// in dist,
					if (new_weight > dist.priority(neighbor))  {
						// Set the weight of neighbor to be new_weight
						dist.changePriority(neighbor, new_weight);
						// Set the parent of neighbor to node_curr
						parents.put(neighbor, node_curr);
					}

				}
			}
		}
		// Use helper method to convert parents to list, return the list made.
		List<Node> optimal_path = parentsToList(parents, state.currentNode(), state.getExit());
		moveFlee(state, optimal_path);
	}
	
	/** Helper method parentsToList converts a hashmap of parents into a list
	 * of nodes with a path from start to end. */ 
	private static
	List<Node> parentsToList(HashMap<Node, Node> parents, Node start, Node end) {
		// Initialization of out_list, the list that is to be returned.
		List<Node> out_list = new ArrayList<Node>();
		out_list.add(end);
		Node last_added = end;
		// While the out_list does not contain start,
		while (!out_list.contains(start)) {
			// add the parent of the last added node to the list,
			// and change variable last_added to be the parent of
			// the last added node in the list.
			last_added = parents.get(last_added);
			out_list.add(last_added);
		}
		// Since the list is backwards, flip it.
		Collections.reverse(out_list);
		return out_list;
	}

	
	
	
	
	/**  This ones gonna take a long fucking time to run O(4^num steps) to be exact
	 *   Less dumb method:
	 *	 When walking back, we want to find all paths from the ring to 
	 *	 the origin with length <= n (n = num steps remaining), and find
	 *	 which one has most coins. One way to do this would be a HashMap
	 *	 With k = path, v = # coins in path.
	 * */
	private List<Long> maxPath(FleeState state){
		
//		int high_score = -1;
		
		// best_path will store the IDs of each node visited in the 
		// optimal path.
		List<Long> 	best_path = null;
		
		List<Node> visited = new ArrayList<Node>();
		visited.add(state.currentNode());
		allPaths(state.currentNode(), state.getExit(), state.stepsLeft(), visited);
		
		
		
		
		return best_path;
		
	}
	
	
	private List<List<Node>> allPaths(Node node_from, 
			Node exit, int steps_left, 
			List<Node> visited) {
		
		// Using a depth first search, traverse every possible path
		// from start to end that takes steps_left steps or less.
		
		if (node_from == exit) {
			// Add the path to something
		}
		
		if (steps_left <= 0) {
			
			return null;
		}
		
		for (Node neighbor : node_from.getNeighbors()) {
			visited.add(neighbor);
			allPaths(neighbor, exit, steps_left-1, visited);
			
		}

		
		return null;
	}
	
	
}
