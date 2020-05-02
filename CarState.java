/**
 *  contains a state of the car regarding speed an position
 */
public class CarState {
    int xPosition;
    int yPosition;
    int xSpeed;
    int ySpeed;

    //initial construction
    public CarState(int xPosition, int yPosition, int xSpeed, int ySpeed) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public CarState() {

    }

    @Override
    //funtion to clone a new state to ensure no memory address complications occur
    protected Object clone() {
        CarState state = null;

        state = new CarState(this.xPosition, this.yPosition, this.xSpeed, this.ySpeed);

        return state;
    }

    @Override
    //function to tell if two states are equal, to be able to do indexOf
    public boolean equals(Object obj) {
        Boolean IsEqual = Boolean.FALSE;
        CarState state = (CarState) obj;
        if (this.xPosition == state.xPosition && this.yPosition == state.yPosition && this.xSpeed == state.xSpeed
                && this.ySpeed == state.ySpeed) {
            IsEqual = Boolean.TRUE;
        }
        return IsEqual;
    }
}