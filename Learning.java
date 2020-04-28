import java.util.ArrayList;

public class Learning {
    public static ArrayList<State> enumerateAllStates(char[][] course) {
        ArrayList<State> states = new ArrayList<>();
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                for (int k = -5; k < 6; k++) {
                    for (int l = -5; l < 6; l++) {
                        states.add(new State(i, j, k, l));
                    }
                }
            }
        }
        return states;
    }

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

    public static ArrayList<StateActionPair> enumerateAllStateActionPairs(ArrayList<State> states,
            ArrayList<Action> actions) {
        ArrayList<StateActionPair> stateActionPairs = new ArrayList<>();
        for (State state : states) {
            for (Action action : actions) {
                stateActionPairs.add(new StateActionPair(state, action));
            }
        }
        return stateActionPairs;
    }

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