import java.util.ArrayList;

public class ValueIterationThread extends Thread {
    int[] policy;
    char[][] course;
    double epsilon;
    double discountFactor;
    Boolean badCrash;
    ArrayList<CarState> states;
    ArrayList<Action> actions;
    ArrayList<StateActionPair> stateActionPairs;
    double[] previousValueFunction;
    double[] currentValueFunction;
    double[] qtableValues;

    public ValueIterationThread(char[][] course, double epsilon, double discountFactor, Boolean badCrash, double[] previousValueFunction, double[] currentValueFunction, double[] qtableValues, ArrayList<CarState> states, ArrayList<Action> actions, ArrayList<StateActionPair> stateActionPairs) {
        this.course=course;
        this.epsilon=epsilon;
        this.discountFactor=discountFactor;
        this.badCrash=badCrash;
        this.states=states;
        this.stateActionPairs=stateActionPairs;
        this.actions=actions;
        this.previousValueFunction=previousValueFunction;
        this.currentValueFunction=currentValueFunction;
        this.qtableValues=qtableValues;
    }


    @Override
    public void run() {
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
        // TODO Auto-generated method stub
    }

    public double[] getValueFunctionChunck() {
        return this.currentValueFunction;
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

}