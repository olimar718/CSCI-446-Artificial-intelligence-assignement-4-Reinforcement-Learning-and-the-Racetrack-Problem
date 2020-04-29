import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueIteration {
    // contains the index of the action to take for each state
    int[] policy;

    public ValueIteration(char[][] course, double epsilon, double discountFactor, Boolean badCrash) {
        ArrayList<State> states = Learning.enumerateAllStates(course);
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        ArrayList<StateActionPair> stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        policy = new int[states.size()];
        double[] previousValueFunction = new double[states.size()];
        double[] currentValueFunction = new double[states.size()];
        double[] qtableValues = new double[stateActionPairs.size()];

        int iteration_count = 0;
        while (Boolean.TRUE) {

            for (State state : states) {

                int qtableIndex = stateActionPairs.indexOf(new StateActionPair(state, new Action(-1, -1)));
                int qtableInitIndex = qtableIndex;
                for (Action action : actions) {

                    Racecar racecar = new Racecar(course);
                    racecar.state = (State) state.clone();
                    double reward = computeImediateReward(racecar, action, course, badCrash);
                    racecar.state = (State) state.clone();
                    double discountedReward = computeDiscountedReward(previousValueFunction, states, racecar, action,
                            course, badCrash);
                    qtableValues[qtableIndex] = reward + (discountFactor * discountedReward);
                    qtableIndex++;
                }
                int stateIndex = states.indexOf(state);
                int actionIndex = actions.indexOf(stateActionPairs
                        .get(Learning.searchQtable(qtableInitIndex, actions, stateActionPairs, qtableValues)).action);
                policy[stateIndex] = actionIndex;
                currentValueFunction[stateIndex] = qtableValues[qtableInitIndex + actionIndex];

            }
            if (thresholdCheck(epsilon, states, previousValueFunction, currentValueFunction)) {
                raceApplyingPolicy(policy, course, states, actions, badCrash);
                break;

            }
            previousValueFunction = currentValueFunction.clone();
            System.out.println(iteration_count);
            iteration_count++;
        }
    }

    public double computeImediateReward(Racecar racecar, Action action, char[][] course, Boolean badCrash) {
        State stateBackup = (State) racecar.state.clone();
        int acceleratedReward = racecar.apply_action(action, course, Boolean.TRUE, badCrash);
        racecar.state = (State) stateBackup.clone();
        int didNotAccelerateReward = racecar.apply_action(action, course, Boolean.FALSE, badCrash);

        return acceleratedReward * 0.8 + didNotAccelerateReward * 0.2;
    }

    public double computeDiscountedReward(double[] previousValueFunction, ArrayList<State> states, Racecar racecar,
            Action action, char[][] course, Boolean badCrash) {
        State stateBackup = (State) racecar.state.clone();
        racecar.apply_action(action, course, Boolean.TRUE, badCrash);
        State accelerated = (State) racecar.state.clone();
        racecar.state = (State) stateBackup.clone();
        racecar.apply_action(action, course, Boolean.FALSE, badCrash);
        State didNotAccelerate = (State) racecar.state.clone();
        return previousValueFunction[states.indexOf(accelerated)] * 0.8
                + previousValueFunction[states.indexOf(didNotAccelerate)] * 0.2;
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
        System.out.println("Current Max difference :" + maxDifference);
        return thresoldReached;
    }

    public void raceApplyingPolicy(int[] policy, char course[][], ArrayList<State> states, ArrayList<Action> actions, Boolean badCrash) {
        Racecar r = new Racecar(course);
        while (Boolean.TRUE) {
            int actionIndex = policy[states.indexOf(r.state)];
            int reward = r.apply_action(actions.get(actionIndex), course, null, badCrash);

            r.printCarPosition(course);
            if (reward == Integer.MAX_VALUE) {
                break;
            }
        }
    }
}