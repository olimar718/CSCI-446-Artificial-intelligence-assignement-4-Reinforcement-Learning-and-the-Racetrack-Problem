
/**
 * This implements all of the qlearning algorithm
 */

import java.util.ArrayList;

public class Qlearning {
    double discountFactor;
    double learningRate;
    double qtableValues[];
    ArrayList<StateActionPair> stateActionPairs = new ArrayList<>();

    // primary constructor
    public Qlearning(Racecar racecar, char[][] course, double discountFactor, double learningRate, Boolean badCrash) {
        this.discountFactor = discountFactor;
        this.learningRate = learningRate;
        // enumerate all states
        ArrayList<CarState> states = Learning.enumerateAllStates(course);
        // enumerate all actions
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        // initialize Qtable randomly, based on states action pair
        this.stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        this.qtableValues = new double[this.stateActionPairs.size()];
        // assign arbitrary values to each cell in the q learning table, this value is
        // less than a reward it recieves from a positive action
        for (int i = 0; i < stateActionPairs.size(); i++) {
            this.qtableValues[i] = Math.random() * 10;
        }
        // Number of races
        for (int i = 0; i < 10000; i++) {
            // repeat this until reaches finish line
            int number_of_action = 0;
            // a way to store an action thatis better than the previous
            Action current_best_Action = new Action();
            // continue until the racecar reaches the finish line or 500 iterations are
            // reached
            while (Boolean.TRUE) {
                if (i == 10000) {
                    racecar.printCarPosition(course);
                }
                number_of_action++;
                // get the value at the first action at the state of the racecar
                int qtableStartIndex = this.stateActionPairs
                        .indexOf(new StateActionPair(racecar.state, new Action(-1, -1)));
                int currentStateActionPairIndex = Learning.searchQtable(qtableStartIndex, actions, stateActionPairs,
                        qtableValues);
                current_best_Action = stateActionPairs.get(currentStateActionPairIndex).action;
                // start picking an action acording to current Qtable
                int reward = racecar.apply_action(current_best_Action, course, null, badCrash);

                if (reward == Integer.MAX_VALUE) {// reached the finish line
                    break;
                }

                // update Qtable for the state action pair according to reward + discount factor
                // and step size
                int qtableIndex = currentStateActionPairIndex;

                // reassign the reward with a negative effct based on the number of actions
                // taken
                int updatedReward = reward + (number_of_action * -5);

                // get the value at the first action at the state of the racecar
                int newQValueIndexInit = this.stateActionPairs
                        .indexOf(new StateActionPair(racecar.state, new Action(-1, -1)));
                // from the information above udate the q table location to the new value
                Double newQtablevalue = updateQtable(qtableIndex, updatedReward, newQValueIndexInit, actions);
                this.qtableValues[qtableIndex] = newQtablevalue;

                // max number of tries per itteration
                if (number_of_action == 500) {
                    break;
                }
            }
            System.out.println(number_of_action);
            racecar.initCarPositon(course, 'S');
        }
    }

    // From all the new information, update the value of the current qtable position
    private Double updateQtable(int qValueIndex, int reward, int newQValueIndexInit, ArrayList<Action> actions) {
        Double newvalue = 0.0;
        Double maxQValueForNextState = this.qtableValues[Learning.searchQtable(newQValueIndexInit, actions,
                stateActionPairs, qtableValues)];
        newvalue = (1 - this.learningRate) * qtableValues[qValueIndex]
                + this.learningRate * (reward + (this.discountFactor * maxQValueForNextState));
        return newvalue;
    }
}