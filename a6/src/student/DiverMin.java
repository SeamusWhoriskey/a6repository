package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import GraphAlgorithms;
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
		//int DIST = state.distanceToRing();
		//Create list of visited nodes
		List<NodeStatus> BEEN_THERE_DONE_THAT = new ArrayList<NodeStatus> ();
		//state.neighbors().remove(state.currentLocation());
		while (state.distanceToRing() > 0) {
			//Initialize variable MINDIST, and loop over adjacent tiles to find the one with the most
			NodeStatus MINDIST = new NodeStatus(state.currentLocation(), 10000);
			NodeStatus HERE_I_AM = new NodeStatus(state.currentLocation(), state.distanceToRing());
			BEEN_THERE_DONE_THAT.add(HERE_I_AM);
			Collection <NodeStatus> c = state.neighbors();
			c.removeAll(BEEN_THERE_DONE_THAT);
			for(NodeStatus z : c) {
				if (z.compareTo(MINDIST) <= 0) {
					MINDIST = z;
				};
			}
			
			state.moveTo(MINDIST.getId());
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
		//Initialize the distance to the exit
		Double STEPS_TO_EXIT = Double.POSITIVE_INFINITY;
		//While the current position is not the exit
		while (state.currentNode() != EXIT) {
			System.out.println("Still good");
			//Flee once the length of the shortest path approaches the steps left 
			if(state.stepsLeft() < STEPS_TO_EXIT) {
				//Get a list of the nodes in the shortest path
				List<Node> HOME_RUN = GraphAlgorithms.shortestPath(state.currentNode(), EXIT);
				for(Node a : HOME_RUN) {
					System.out.println(HOME_RUN);
					state.moveTo(a);
					System.out.println("good");
				}
			}
			//Else, chart a path towards the most coins possible
			
		}
		return;
 	}

}
