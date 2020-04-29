
public class CarState {
    int xPosition;
    int yPosition;
    int xSpeed;
    int ySpeed;

    public CarState(int xPosition, int yPosition, int xSpeed, int ySpeed) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public CarState() {

    }

    @Override
    protected Object clone() {
        // TODO Auto-generated method stub
        CarState state = null;

        state = new CarState(this.xPosition, this.yPosition, this.xSpeed, this.ySpeed);

        return state;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        Boolean IsEqual = Boolean.FALSE;
        CarState state = (CarState) obj;
        if (this.xPosition == state.xPosition && this.yPosition == state.yPosition && this.xSpeed == state.xSpeed
                && this.ySpeed == state.ySpeed) {
            IsEqual = Boolean.TRUE;
        }
        return IsEqual;
    }
}