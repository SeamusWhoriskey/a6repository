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

import a4.Heap;
import game.FindState;
import game.FleeState;
import game.NodeStatus;
import game.SewerDiver;
import graph.Edge;
import graph.LabeledEdge;
import student.GraphAlgorithms.Path;
import game.Node;


@SuppressWarnings("unused")
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
		
		lessDumbFind(state);
		
	}
	
	
	/** lessDumbFind uses a dfs walk algorithm,
	 * but explores the highest priority (shortest distance to 
	 * the ring) neighbor first. */
	public void lessDumbFind(FindState state) {
		
		// curr_id holds the current location 
		long curr_id = state.currentLocation();
		
		// sorted_neighbors hold the neighbors of the current node.
		List<NodeStatus> sorted_neighbors = sortNeighbors(state);
		
		// ends the search when Min is on the ring.
		if (state.distanceToRing() == 0) {
			return;
		}
		
		// for each neighbor nbr, sorted by priority
		for (NodeStatus nbr : sorted_neighbors) {
			// if the node has not yet been visited,
			if (!visited.contains(nbr.getId())) {
				
				// add nbr to the visited set
				visited.add(nbr.getId());
				// move Min to nbr
				state.moveTo(nbr.getId());
				// recursively call the method.
				lessDumbFind(state);
				// ends the search when Min is on the ring.
				if (state.distanceToRing() == 0) {
					return;
				}
				// Move back to the current node if the ring was
				// not in the current path.
				state.moveTo(curr_id);
			}
		}
	}
	
	/** sortNeighbors sorts the neighbors of the current node in order of
	 * least to most distance. */
	private List<NodeStatus> sortNeighbors(FindState state) {
		
		// sorted_neighbors will hold the neighbors of the current node as a min heap.
		Heap<NodeStatus, Integer> sorted_neighbors = new Heap<NodeStatus, Integer>(Comparator.reverseOrder());
		
		// This is here for that extra 0.01 score multiplier lol
		List<NodeStatus> neighbors = new ArrayList<NodeStatus>(state.neighbors());
		Collections.reverse(neighbors);
		
		// for each neighbor of the current node
		for (NodeStatus nbr : neighbors) {
			// add the neighbor with its distance to the ring as the heap
			sorted_neighbors.add(nbr, nbr.getDistanceToTarget());
		}
		
		// Turn the min_heap to a list, so that it can be iterated over.
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
		
		getBestCoinFlee(state);

		
		return;
 	}
	

	/** Moves Min along the path given to it, used as a helper function in the flee functions. */
	private void moveFlee(FleeState state, List<Node> path) {
		// Removes the starting node
		path.remove(0);
		// For each remaining node in the path, 
		for (Node n : path) {
			// move to the next node.
			state.moveTo(n);
		}
		
	}
		
	/** getBestCoinFlee finds the shortest path to the "highest scoring"
	 * node -- the scoring of each tile is given as 
	 * (value of coin on tile)/(shortest distance to tile) */
	private void getBestCoinFlee(FleeState state) {
		
		// Initialization of useful variables:
		// curr_node stores the current Node.
		Node curr_node = state.currentNode();
		// exit stores the exit node.
		Node exit = state.getExit();
		// max_val stores the highest value of (value of coin on tile)/(shortest distance to tile)
		double max_val = -1.0;
		// curr_score holds the current score of a Node n
		double curr_score;
		// curr_coins holds the number of coins on the current tile.
		double curr_coins;
		// dist_to holds the distance from the current node to a Node n
		double dist_to;
		// dist_exit holds the shortest distance from a node n to the exit
		double dist_exit = GraphAlgorithms.shortestPath(curr_node, exit).size();
		// best_node holds the node at which max_val occurs.
		Node best_node = exit;

		List<Node> all_nodes = new ArrayList<Node>(state.allNodes());
		all_nodes.remove(curr_node);
		all_nodes.remove(exit);
		// For each node that is not the exit or current node, 
		// determine their score and find the max score.
		for (Node n : all_nodes) {
			// set curr_coins, dist_to, dist_exit, and curr_score
			curr_coins = n.getTile().coins();
			dist_to = GraphAlgorithms.shortestPath(curr_node, n).size();
			dist_exit = GraphAlgorithms.shortestPath(n, exit).size();
			curr_score = curr_coins/(dist_to);
			// if the current score is better than max_val
			if (curr_score > max_val) {
				// If there is a path from curr_node to n to exit
				// that is less than the number of steps left,
				if (dist_to + dist_exit < state.stepsLeft()/7 - 1) {
					// set max_val to the current score
					max_val = curr_score;
					// set the best node to n
					best_node = n;
				}
			}
		}
		
		// move to the best overall node,
		moveFlee(state, GraphAlgorithms.shortestPath(curr_node, best_node));
		
		// if the best node is not the exit, call this method again.
		if (best_node != exit) {
			getBestCoinFlee(state);
		}
	}
	
}