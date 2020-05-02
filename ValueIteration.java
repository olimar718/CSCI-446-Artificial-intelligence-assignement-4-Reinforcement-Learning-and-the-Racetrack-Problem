/* This class is the implementation of ValueIteration
*/


import java.util.ArrayList;

public class ValueIteration {
    int[] policy;

    public ValueIteration(char[][] course, double epsilon, double discountFactor, Boolean badCrash) {//This constructor will start value iteration
        ArrayList<CarState> states = Learning.enumerateAllStates(course);
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        ArrayList<StateActionPair> stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        //Hold the policy. The policy is simply an array. The index corresponds to the index of the state in the state array, the value at this index correspond to the index of the action in the action array.
        policy = new int[states.size()];
        //holds the previous value function
        double[] previousValueFunction = new double[states.size()];
        //holds the current value function
        double[] currentValueFunction = new double[states.size()];
        //holds the qtable
        double[] qtableValues = new double[stateActionPairs.size()];
        //Holds the imediate rewards (wheighted average of when the car accelerates (80%) and when it does not (20%), that are constant  so they are computed only a the first sweep of value iteration.
        ArrayList<Double> imediateRewards = new ArrayList<>();
        //Holds the Index of the state reached by performing a action in a state. So one for each state action pair. One for when the action was actually performed, one for when it was not. Used for the the discounted reward part reward
        ArrayList<Integer> stateIndexAccelerated=new ArrayList<>();
        ArrayList<Integer> stateIndexDidNotAccelerate=new ArrayList<>();
        for (StateActionPair stateActionPair : stateActionPairs) {// Compute constant term of the variable elimination algorithm (reward, state transition), store them in memory
            Racecar racecar = new Racecar(course);
            racecar.state = (CarState) stateActionPair.state.clone();
            imediateRewards.add(computeImediateReward(racecar, stateActionPair.action, course, badCrash));
            stateIndexAccelerated.add(computeDiscountedRewardStateIndex(states, racecar, stateActionPair.action, course, Boolean.TRUE, badCrash));
            stateIndexDidNotAccelerate.add(computeDiscountedRewardStateIndex(states, racecar, stateActionPair.action, course, Boolean.FALSE, badCrash));
        }

        int iteration_count = 0;
        while (Boolean.TRUE) {// Main loop of value iteration, here we sweep over the state action paris many time. At the end we use threshold check the maximum difference between previous value function and current value function
            int qtableIndex = 0;
            int stateIndex=0;
            for (CarState state : states) {

                int qtableInitIndex = qtableIndex;
                for (Action action : actions) {
                    double reward = imediateRewards.get(qtableIndex);
                    double discountedReward = 0.8*previousValueFunction[stateIndexAccelerated.get(qtableIndex)]+0.2*previousValueFunction[stateIndexDidNotAccelerate.get(qtableIndex)];// here we multiply by the corresponding probability for the discounted reward
                    qtableValues[qtableIndex] = reward + (discountFactor * discountedReward);//Here is the formula for the Value function. This is stored for each state action pair (qtable)
                    qtableIndex++;
                }
                
                int actionIndex = actions.indexOf(stateActionPairs
                        .get(Learning.searchQtable(qtableInitIndex, actions, stateActionPairs, qtableValues)).action);
                policy[stateIndex] = actionIndex;
                currentValueFunction[stateIndex] = qtableValues[qtableInitIndex + actionIndex];//To find the actual value function form the qtable we just perform the action that our policy gives us.
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

    public double computeImediateReward(Racecar racecar, Action action, char[][] course, Boolean badCrash) {// This is for the left part of the Value function equation (R(s,a)). This is actually an weighted average of the case where the car moved (0.8) and where it did not (0.2).
        CarState stateBackup = (CarState) racecar.state.clone();
        int acceleratedReward = racecar.apply_action(action, course, Boolean.TRUE, badCrash);
        racecar.state = (CarState) stateBackup.clone();
        int didNotAccelerateReward = racecar.apply_action(action, course, Boolean.FALSE, badCrash);

        return acceleratedReward * 0.8 + didNotAccelerateReward * 0.2;
    }


    public int computeDiscountedRewardStateIndex(ArrayList<CarState> states, Racecar racecar,
            Action action, char[][] course, Boolean accelerated,Boolean badCrash) {// This is for the right part of the Value Function equation (which uses the previous value function). This is to get the index of the state that will be reached by performing the action

        racecar.apply_action(action, course, accelerated, badCrash);

        return states.indexOf(racecar.state);
    }

    public Boolean thresholdCheck(double epsilon, ArrayList<CarState> states, double[] previousValueFunction,
            double[] currentValueFunction) {//This checks the maximum difference between the two value function. If it is bigger than epsilon (the threshold) then it will return false and the Value iteration algorithm will continue.
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
            Boolean badCrash) {//This is simply to apply the policy that was learned with value iteration.
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