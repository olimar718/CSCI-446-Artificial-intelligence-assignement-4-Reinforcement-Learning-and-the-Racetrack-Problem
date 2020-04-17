import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Qlearning {
    public Qlearning(Racecar racecar, char[][] course) {
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
        ArrayList<StateActionPair> stateActionPairs=new ArrayList<>();
        ArrayList<Double> qtableValues=new ArrayList<>();
        Random random=new Random();
        random.setSeed(System.nanoTime());
        for (State state : states) {
            for (Action action : actions) {
                stateActionPairs.add(new StateActionPair(state,action));    
                qtableValues.add(random.nextDouble()*10);
            }
        }





        Double current_best_value = 0.0;
        Action current_best_Action = new Action();

        for (Action action : actions) {
            int indexOfValue=stateActionPairs.indexOf(new StateActionPair(racecar.state, action));
            Double current_value=qtableValues.get(indexOfValue);
            if ( current_value > current_best_value) {
                current_best_Action = action;
                current_best_value=current_value;
            }
        }
        apply_action(racecar, current_best_Action);

        racecar.printCarPosition(course);
        int c = 0;
        // repeat this until ...
        // start picking an action acording to current Qtable
        // update Qtable for the state action pair according to reward + discount factor
        // and step size
        // repeat
        
    }
    public Racecar apply_action(Racecar racecar, Action action){
        
        racecar.state.xSpeed+= action.xAcceleration;
        racecar.state.ySpeed+= action.yAcceleration;

        //check speed boundaries
        if(racecar.state.xSpeed > 5){
            racecar.state.xSpeed = 5;
        }
        if(racecar.state.xSpeed < -5){
            racecar.state.xSpeed = -5;
        }
        if(racecar.state.ySpeed > 5){
            racecar.state.ySpeed = 5;
        }
        if(racecar.state.ySpeed < -5){
            racecar.state.ySpeed = -5;
        }


        racecar.state.xPosition+=racecar.state.xSpeed;
        racecar.state.yPosition+=racecar.state.ySpeed;
        return racecar;
    }
}