import java.util.ArrayList;

public class Qlearning {
    double discountFactor;
    double learningRate;
    ArrayList<Double> qtableValues = new ArrayList<>();
    ArrayList<StateActionPair> stateActionPairs = new ArrayList<>();

    public Qlearning(Racecar racecar, char[][] course, double discountFactor, double learningRate) {
        this.discountFactor = discountFactor;
        this.learningRate = learningRate;
        // enumerate all states
        ArrayList<State> states = Learning.enumerateAllStates(course);
        // enumerate all actions
        ArrayList<Action> actions = Learning.enumerateAllActions(course);
        // initialize Qtable randomly, based on states action pair
        this.stateActionPairs = Learning.enumerateAllStateActionPairs(states, actions);
        for (StateActionPair stateActionPair : stateActionPairs) {
            this.qtableValues.add(Math.random() * 10);
        }

        // Number of races
        for (int i = 0; i < 1000000; i++) {
            // repeat this until reaches finish line
            int number_of_action = 0;
            Action current_best_Action = new Action();
            while (Boolean.TRUE) {
                number_of_action++;
                StateActionPair currentStateActionPair = Learning.searchQtable(racecar.state, actions, stateActionPairs,
                        qtableValues);
                current_best_Action = currentStateActionPair.action;
                // start picking an action acording to current Qtable
                int reward = racecar.apply_action(current_best_Action, course, null);
                // racecar.printCarPosition(course);

                // update Qtable for the state action pair according to reward + discount factor
                // and step size
                int qtableIndex = this.stateActionPairs.indexOf(currentStateActionPair);

                int updatedReward = reward + (number_of_action * -5);

                Double newQtablevalue = updateQtable(qtableIndex, updatedReward, racecar.state, actions);
                this.qtableValues.set(qtableIndex, newQtablevalue);
                if (reward == 1000) {// reached the finish line
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

    private Double updateQtable(int qValueIndex, int reward, State newState, ArrayList<Action> actions) {
        Double newvalue = 0.0;
        Double maxQValueForNextState = this.qtableValues.get(this.stateActionPairs
                .indexOf(Learning.searchQtable(newState, actions, stateActionPairs, qtableValues)));
        newvalue = (1 - this.learningRate) * qtableValues.get(qValueIndex)
                + this.learningRate * (reward + (this.discountFactor * maxQValueForNextState));
        return newvalue;
    }
}