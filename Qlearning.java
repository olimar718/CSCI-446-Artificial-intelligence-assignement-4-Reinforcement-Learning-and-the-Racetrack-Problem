import java.util.ArrayList;

public class Qlearning {
    double discountFactor;
    double learningRate;
    // ArrayList<Double> qtableValues = new ArrayList<>();
    double qtableValues[];
    ArrayList<StateActionPair> stateActionPairs = new ArrayList<>();

    public Qlearning(Racecar racecar, char[][] course, double discountFactor, double learningRate,  Boolean badCrash) {
        this.discountFactor = discountFactor;
        this.learningRate = learningRate;
        // enumerate all states
        ArrayList<CarState> states = Learning.enumerateAllStates(course);
        // enumerate all actions
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        // initialize Qtable randomly, based on states action pair
        this.stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        this.qtableValues = new double[this.stateActionPairs.size()];
        for (int i = 0; i < stateActionPairs.size(); i++) {
            this.qtableValues[i] = Math.random() * 10;
        }
        // Number of races
        for (int i = 0; i < 1000000; i++) {
            // repeat this until reaches finish line
            int number_of_action = 0;
            Action current_best_Action = new Action();
            while (Boolean.TRUE) {
                number_of_action++;
                int qtableStartIndex = this.stateActionPairs
                        .indexOf(new StateActionPair(racecar.state, new Action(-1, -1)));
                int currentStateActionPairIndex = Learning.searchQtable(qtableStartIndex, actions, stateActionPairs,
                        qtableValues);
                current_best_Action = stateActionPairs.get(currentStateActionPairIndex).action;
                // start picking an action acording to current Qtable
                int reward = racecar.apply_action(current_best_Action, course, null, badCrash);
                // racecar.printCarPosition(course);

                // update Qtable for the state action pair according to reward + discount factor
                // and step size
                int qtableIndex = currentStateActionPairIndex;

                int updatedReward = reward + (number_of_action * -5);
                int newQValueIndexInit = this.stateActionPairs
                        .indexOf(new StateActionPair(racecar.state, new Action(-1, -1)));
                Double newQtablevalue = updateQtable(qtableIndex, updatedReward, newQValueIndexInit, actions);
                this.qtableValues[qtableIndex] = newQtablevalue;
                if (reward == Integer.MAX_VALUE) {// reached the finish line
                    break;
                }

                // max number of tries per itteration
                if (number_of_action == 500) {
                    break;
                }
            }
            System.out.println(number_of_action);
            racecar.initCarPositon(course);
        }
    }

    private Double updateQtable(int qValueIndex, int reward, int newQValueIndexInit, ArrayList<Action> actions) {
        Double newvalue = 0.0;
        Double maxQValueForNextState = this.qtableValues[Learning.searchQtable(newQValueIndexInit, actions,
                stateActionPairs, qtableValues)];
        newvalue = (1 - this.learningRate) * qtableValues[qValueIndex]
                + this.learningRate * (reward + (this.discountFactor * maxQValueForNextState));
        return newvalue;
    }
}