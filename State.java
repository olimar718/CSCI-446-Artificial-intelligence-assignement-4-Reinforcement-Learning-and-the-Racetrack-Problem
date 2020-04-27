
public class State {
    int xPosition;
    int yPosition;
    int xSpeed;
    int ySpeed;

    public State(int xPosition, int yPosition, int xSpeed, int ySpeed) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public State() {

    }

    @Override
    protected Object clone() {
        // TODO Auto-generated method stub
        State state = null;
        try {
            state = (State) super.clone();
        } catch (Exception e) {
            state = new State(this.xPosition, this.yPosition, this.xSpeed, this.ySpeed);
        }
        return state;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        Boolean IsEqual = Boolean.FALSE;
        State state = (State) obj;
        if (this.xPosition == state.xPosition && this.yPosition == state.yPosition && this.xSpeed == state.xSpeed
                && this.ySpeed == state.ySpeed) {
            IsEqual = Boolean.TRUE;
        }
        return IsEqual;
    }
}