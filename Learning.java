/**
 * Helper class for creating state,action, state action pairs, and search funtion. Contains the common code between Qlearning and value iteration.
 */
import java.util.ArrayList;

public class Learning {
    //based on the size of the course, create all neccessary states
    public static ArrayList<CarState> enumerateAllStates(char[][] course) {
        ArrayList<CarState> states = new ArrayList<>();
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                for (int k = -5; k < 6; k++) {
                    for (int l = -5; l < 6; l++) {
                        states.add(new CarState(i, j, k, l));
                    }
                }
            }
        }
        return states;
    }
    //create all possible actions
    public static ArrayList<Action> enumerateAllActions(char[][] course) {
        ArrayList<Action> actions = new ArrayList<>();
        // enumerates all action
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                actions.add(new Action(i, j));
            }
        }
        return actions;
    }
    //based on the previous declarations of the states and actions, create another table relating to the relation between those
    public static ArrayList<StateActionPair> enumerateAllStateActionPairs(ArrayList<CarState> states,
            ArrayList<Action> actions) {
        ArrayList<StateActionPair> stateActionPairs = new ArrayList<>();
        for (CarState state : states) {
            for (Action action : actions) {
                stateActionPairs.add(new StateActionPair(state, action));
            }
        }
        return stateActionPairs;
    }

    // find the best index to go towards based on the current state
    public static int searchQtable(int stateActionPairIndex, ArrayList<Action> actions,
            ArrayList<StateActionPair> stateActionPairs, double[] qtableValues) {
        int indexOfBestAction = stateActionPairIndex;
        int indexOfValue = indexOfBestAction;
        Double current_best_value = qtableValues[indexOfBestAction];
        for (Action action : actions) {
            Double current_value = qtableValues[indexOfValue];
            if (current_value > current_best_value) {
                current_best_value = current_value;
                indexOfBestAction = indexOfValue;
            }
            indexOfValue++;
        }
        return indexOfBestAction;
    }
}