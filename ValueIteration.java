import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueIteration {
    // contains the index of the action to take for each state
    int[] policy;

    public ValueIteration(char[][] course, double epsilon, double discountFactor) {
        ArrayList<State> states = Learning.enumerateAllStates(course);
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        ArrayList<StateActionPair> stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        policy = new int[states.size()];
        double[] previousValueFunction = new double[states.size()];
        double[] currentValueFunction = new double[states.size()];
        Double[] qtableValues = new Double[stateActionPairs.size()];

        while (Boolean.TRUE) {

            for (State state : states) {
                for (Action action : actions) {

                    Racecar racecar = new Racecar(course);
                    racecar.state = (State) state.clone();
                    int reward = racecar.apply_action(action, course, null);
                    racecar.state = (State) state.clone();
                    double discountedReward = computeDiscountedReward(previousValueFunction, states, racecar, action,
                            course);
                    qtableValues[stateActionPairs.indexOf(new StateActionPair(state, action))] = reward
                            + discountFactor * discountedReward;
                }
                int stateIndex = states.indexOf(state);
                int actionIndex = actions.indexOf(stateActionPairs.get(Learning.searchQtable(state, actions,
                        stateActionPairs, new ArrayList<Double>(Arrays.asList(qtableValues)))).action);
                policy[stateIndex] = actionIndex;
                currentValueFunction[stateIndex] = qtableValues[stateActionPairs
                        .indexOf(new StateActionPair(state, actions.get(actionIndex)))];
            }
            if (thresholdCheck(epsilon, states, previousValueFunction, currentValueFunction)) {
                break;
            }
        }
    }

    public Boolean thresholdCheck(double epsilon, ArrayList<State> states, double[] previousValueFunction,
            double[] currentValueFunction) {
        Boolean thresoldReached;
        Double maxDifference = 0.0;
        for (int i = 0; i < states.size(); i++) {
            Double currentDifference = (Math.abs(previousValueFunction[i] - currentValueFunction[i]));
            if (currentDifference > maxDifference) {
                maxDifference = currentDifference;
            }
        }
        if (maxDifference > epsilon) {
            thresoldReached = Boolean.FALSE;
        } else {
            thresoldReached = Boolean.TRUE;
        }
        return thresoldReached;
    }

    public double computeDiscountedReward(double[] previousValueFunction, ArrayList<State> states, Racecar racecar,
            Action action, char[][] course) {// hard not complete yet
        State stateBackup = (State) racecar.state.clone();
        racecar.apply_action(action, course, Boolean.TRUE);
        State accelerated = (State) racecar.state.clone();
        racecar.state = (State) stateBackup.clone();
        racecar.apply_action(action, course, Boolean.FALSE);
        State didNotAccelerate = (State) racecar.state.clone();
        return previousValueFunction[states.indexOf(accelerated)] * 0.8
                + previousValueFunction[states.indexOf(didNotAccelerate)] * 0.2;
    }

    public void raceApplyingPolicy() {

    }
}