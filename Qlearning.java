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
        ArrayList<Action> actions = new ArrayList<>();
        // enumerates all action
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                actions.add(new Action(i, j));
            }
        }
        // initialize Qtable randomly, based on states action pair
        for (State state : states) {
            for (Action action : actions) {
                stateActionPairs.add(new StateActionPair(state, action));
                qtableValues.add(Math.random() * 10);
            }
        }
        // 100 races to learn
        for (int i = 0; i < 10000; i++) {
            // repeat this until reaches finish line
            int number_of_action = 0;
            while (Boolean.TRUE) {
                number_of_action++;
                Action current_best_Action = new Action();
                StateActionPair currentStateActionPair = searchQtable(racecar.state, actions);
                current_best_Action = currentStateActionPair.action;
                // start picking an action acording to current Qtable
                int reward = racecar.apply_action(current_best_Action, course);
                //racecar.printCarPosition(course);


                // update Qtable for the state action pair according to reward + discount factor
                // and step size
                int qtableIndex = this.stateActionPairs.indexOf(currentStateActionPair);
                Double newQtablevalue = updateQtable(qtableIndex, reward, racecar.state, actions);
                this.qtableValues.set(qtableIndex, newQtablevalue);
                if (reward == 1000) {// reached the finish line
                    break;
                }

            }
            System.out.println(number_of_action);
            if(number_of_action<15){
                int nb=0;
            }
            racecar.initCarPositon(course);
        }
    }

    private Double updateQtable(int qValueIndex, int reward, State newState, ArrayList<Action> actions) {
        Double newvalue = 0.0;
        Double maxQValueForNextState = this.qtableValues
                .get(this.stateActionPairs.indexOf(searchQtable(newState, actions)));
        newvalue = (1 - this.learningRate) * qtableValues.get(qValueIndex)
                + this.learningRate * (reward + (this.discountFactor * maxQValueForNextState));
        return newvalue;
    }

    private StateActionPair searchQtable(State state, ArrayList<Action> actions) {
        int indexOfBestAction = stateActionPairs.indexOf(new StateActionPair(state, new Action()));;
        Double current_best_value = qtableValues.get(indexOfBestAction);

        for (Action action : actions) {
            int indexOfValue = stateActionPairs.indexOf(new StateActionPair(state, action));
            Double current_value = qtableValues.get(indexOfValue);
            if (current_value > current_best_value) {
                current_best_value = current_value;
                indexOfBestAction = indexOfValue;
            }
        }
        return stateActionPairs.get(indexOfBestAction);
    }
}