
public class StateActionPair {
    CarState state;
    Action action;

    public StateActionPair(CarState state, Action action) {
        this.state=state;
        this.action=action;
    }
    @Override
    public boolean equals(Object obj) {
        StateActionPair stateActionPair= (StateActionPair)obj;
        if(this.state.xPosition==stateActionPair.state.xPosition&&
        this.state.yPosition==stateActionPair.state.yPosition && 
        this.state.xSpeed==stateActionPair.state.xSpeed && 
        this.state.ySpeed==stateActionPair.state.ySpeed&&
        this.action.xAcceleration==stateActionPair.action.xAcceleration&&
        this.action.yAcceleration==stateActionPair.action.yAcceleration){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}