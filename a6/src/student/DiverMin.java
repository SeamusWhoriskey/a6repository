package student;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import a5.GraphAlgorithms;
import game.FindState;
import game.FleeState;
import game.NodeStatus;
import game.SewerDiver;
import game.Node;

import common.NotImplementedError;

public class DiverMin implements SewerDiver {

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
		//Get distance to ring
		int DIST = state.distanceToRing();
		while (DIST > 0) {
			//Get current location
			long HERE_I_AM = state.currentLocation();
			//Get places to move to
			Collection<NodeStatus> ADJACENTS = state.neighbors();
			Object[] A = ADJACENTS.toArray();
			//Initialize variable MINDIST, and loop over adjacent tiles to find the one with the most
			long MINDIST = HERE_I_AM;
			for(NodeStatus z : ADJACENTS) {
				if (z.compareTo(MINDIST) <= 0) {
					MINDIST = z;
				}
			}
			state.
			state.moveTo(MINDIST);
		}
		return;
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
		//Get current location
		Node HERE_I_AM = state.currentNode();
		//Get exit node, to save computing time on the loops
		Node EXIT = state.getExit();
		//Use Dijkstra's algorithm to get the minimum distance to the exit
		
		//
		
		//While the current position is not the exit
		while (state.currentNode() != EXIT) {
			//Use Dijkstra's Algorithm to find the shortest path to the exit
			int STEPS_TO_EXIT = null;
			//Flee once the length of the shortest path approaches the steps left 
			if(state.stepsLeft() < STEPS_TO_EXIT+5) {
				//Get a list of the nodes in the shortest path
				List<Node> HOME_RUN = GraphAlgorithms.shortestPath(state.currentNode(), EXIT);
				for(Node a : HOME_RUN) {
					state.moveTo(a);
				}
			}
			//Else, chart a path towards the most coins possible
			
		}
		return;
 	}

}