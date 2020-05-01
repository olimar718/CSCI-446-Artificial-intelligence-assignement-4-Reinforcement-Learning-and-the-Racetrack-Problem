import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class ValueIteration {
    // contains the index of the action to take for each state
    int[] policy;

    public ValueIteration(char[][] course, double epsilon, double discountFactor, Boolean badCrash) {
        ArrayList<CarState> states = Learning.enumerateAllStates(course);
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        ArrayList<StateActionPair> stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        policy = new int[states.size()];
        double[] previousValueFunction = new double[states.size()];
        double[] currentValueFunction = new double[states.size()];
        double[] qtableValues = new double[stateActionPairs.size()];
        ArrayList<Double> imediateRewards = new ArrayList<>();
        ArrayList<Integer> stateIndexAccelerated=new ArrayList<>();
        ArrayList<Integer> stateIndexDidNotAccelerate=new ArrayList<>();
        for (StateActionPair stateActionPair : stateActionPairs) {// fill out the imediate rewards
            Racecar racecar = new Racecar(course);
            racecar.state = (CarState) stateActionPair.state.clone();
            imediateRewards.add(computeImediateReward(racecar, stateActionPair.action, course, badCrash));
            stateIndexAccelerated.add(computeDiscountedRewardStateIndex(states, racecar, stateActionPair.action, course, Boolean.TRUE, badCrash));
            stateIndexDidNotAccelerate.add(computeDiscountedRewardStateIndex(states, racecar, stateActionPair.action, course, Boolean.FALSE, badCrash));
        }

        int iteration_count = 0;
        while (Boolean.TRUE) {
            int qtableIndex = 0;
            int stateIndex=0;
            for (CarState state : states) {

                int qtableInitIndex = qtableIndex;
                for (Action action : actions) {
                    double reward = imediateRewards.get(qtableIndex);
                    double discountedReward = 0.8*previousValueFunction[stateIndexAccelerated.get(qtableIndex)]+0.2*previousValueFunction[stateIndexDidNotAccelerate.get(qtableIndex)];
                    qtableValues[qtableIndex] = reward + (discountFactor * discountedReward);
                    qtableIndex++;
                }
                
                int actionIndex = actions.indexOf(stateActionPairs
                        .get(Learning.searchQtable(qtableInitIndex, actions, stateActionPairs, qtableValues)).action);
                policy[stateIndex] = actionIndex;
                currentValueFunction[stateIndex] = qtableValues[qtableInitIndex + actionIndex];
                stateIndex++;

            }
            if (thresholdCheck(epsilon, states, previousValueFunction, currentValueFunction)) {
                raceApplyingPolicy(policy, course, states, actions, badCrash);
                break;

            }
            previousValueFunction = currentValueFunction.clone();
            System.out.println(iteration_count);
            iteration_count++;
            // max number of tries per itteration
            if (iteration_count == 500) {
                break;
            }
        }
    }

    public double computeImediateReward(Racecar racecar, Action action, char[][] course, Boolean badCrash) {
        CarState stateBackup = (CarState) racecar.state.clone();
        int acceleratedReward = racecar.apply_action(action, course, Boolean.TRUE, badCrash);
        racecar.state = (CarState) stateBackup.clone();
        int didNotAccelerateReward = racecar.apply_action(action, course, Boolean.FALSE, badCrash);

        return acceleratedReward * 0.8 + didNotAccelerateReward * 0.2;
    }


    public int computeDiscountedRewardStateIndex(ArrayList<CarState> states, Racecar racecar,
            Action action, char[][] course, Boolean accelerated,Boolean badCrash) {

        racecar.apply_action(action, course, accelerated, badCrash);

        return states.indexOf(racecar.state);
    }

    public Boolean thresholdCheck(double epsilon, ArrayList<CarState> states, double[] previousValueFunction,
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

    public void raceApplyingPolicy(int[] policy, char course[][], ArrayList<CarState> states, ArrayList<Action> actions,
            Boolean badCrash) {
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