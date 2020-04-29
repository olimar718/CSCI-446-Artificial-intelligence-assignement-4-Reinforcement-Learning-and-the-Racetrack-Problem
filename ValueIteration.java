import java.util.ArrayList;
import java.util.Arrays;
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

        int iteration_count = 0;
        while (Boolean.TRUE) {
            int splitNumber = 10;

            // splitting states arraylist, previousValueFunction, and currentValueFuntion
            // for
            // threads
            CarState[] arr = new CarState[states.size()];
            arr = states.toArray(arr);
            int chunk = arr.length / splitNumber;
            CarState[][] splitState;
            double[][] splitCurrValue;
            double[][] splitPrevValue;
            Boolean divided;
            if (arr.length % splitNumber == 0) {
                divided = Boolean.TRUE;
                splitState = new CarState[splitNumber][chunk];
                splitCurrValue = new double[splitNumber][chunk];
                splitPrevValue = new double[splitNumber][chunk];
            } else {
                divided = Boolean.FALSE;
                splitState = new CarState[splitNumber + 1][chunk];
                splitCurrValue = new double[splitNumber + 1][chunk];
                splitPrevValue = new double[splitNumber + 1][chunk];
            }

            int j = 0;
            for (int i = 0; i < arr.length; i += chunk) {
                splitState[j] = Arrays.copyOfRange(arr, i, Math.min(arr.length, i + chunk));
                splitCurrValue[j] = Arrays.copyOfRange(currentValueFunction, i,
                        Math.min(currentValueFunction.length, i + chunk));
                splitPrevValue[j] = Arrays.copyOfRange(previousValueFunction, i,
                        Math.min(previousValueFunction.length, i + chunk));
                j++;
            }

            // splitting qtableValues and stateActionPairs for threads
            // StateActionPair saArr = new StateActionPair[stateActionPairs.size()];
            // saArr = stateActionPairs.toArray(saArr);
            // double[][] splitQTable;
            // chunk = qtableValues.length / splitNumber;
            // if (qtableValues.length % splitNumber == 0) {
            //     divided = true;
            //     splitQTable = new double[splitNumber][chunk];
            // } else {
            //     splitQTable = new double[splitNumber + 1][chunk];
            // }
            // j = 0;
            // for (int i = 0; i < qtableValues.length; i++) {
            //     splitQTable[j] = Arrays.copyOfRange(qtableValues, i, Math.min(qtableValues.length, i + chunk));
            //     j++;
            // }

            // splitting

            int threadCount;
            if (divided) {
                threadCount = splitNumber;
            } else {
                threadCount = splitNumber + 1;
            }
            ArrayList<ValueIterationThread> threads = new ArrayList<>();
            for (int i = 0; i < threadCount; i++) {
                ValueIterationThread thread = new ValueIterationThread(course, epsilon, discountFactor, badCrash,
                        splitCurrValue[i], splitCurrValue[i], qtableValues, new ArrayList<CarState>(Arrays.asList(splitState[i])), actions, stateActionPairs);
                threads.add(thread);
                thread.start();
            }
            int currentValueFunctionIndex = 0;
            for (ValueIterationThread valueIterationThread : threads) {
                try {
                    valueIterationThread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.err.println("Thread Execption");
                    e.printStackTrace();
                }
                int i;
                for (i=currentValueFunctionIndex; i<valueIterationThread.getValueFunctionChunck().length+currentValueFunctionIndex; i++) {{
                    
                }
                currentValueFunctionIndex=i;
                    
                }
            }

            for (CarState state : states) {

                int qtableIndex = stateActionPairs.indexOf(new StateActionPair(state, new Action(-1, -1)));
                int qtableInitIndex = qtableIndex;
                for (Action action : actions) {

                    Racecar racecar = new Racecar(course);
                    racecar.state = (CarState) state.clone();
                    double reward = computeImediateReward(racecar, action, course, badCrash);
                    racecar.state = (CarState) state.clone();
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

    public double computeDiscountedReward(double[] previousValueFunction, ArrayList<CarState> states, Racecar racecar,
            Action action, char[][] course, Boolean badCrash) {
        CarState stateBackup = (CarState) racecar.state.clone();
        racecar.apply_action(action, course, Boolean.TRUE, badCrash);
        CarState accelerated = (CarState) racecar.state.clone();
        racecar.state = (CarState) stateBackup.clone();
        racecar.apply_action(action, course, Boolean.FALSE, badCrash);
        CarState didNotAccelerate = (CarState) racecar.state.clone();
        return previousValueFunction[states.indexOf(accelerated)] * 0.8
                + previousValueFunction[states.indexOf(didNotAccelerate)] * 0.2;
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