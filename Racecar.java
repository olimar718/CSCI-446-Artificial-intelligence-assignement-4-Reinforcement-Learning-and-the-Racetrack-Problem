
/**
 * This class holds data corresponding to the racecar and its differing attributes
 * This also contains functions that are related to both qlearning and valueIteration
 */
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public class Racecar {

    CarState state = new CarState();
    // Action action = new Action();

    public Racecar(char[][] course) {

        this.initCarPositon(course,'S');
        // printCarPosition(course);
    }

    // Place the car stopped on a random S position
    public void initCarPositon(char[][] course, char startingSymbol) {
        ArrayList<Integer> startxposition = new ArrayList<>();
        ArrayList<Integer> startyposition = new ArrayList<>();
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                if (course[i][j] == startingSymbol) {
                    startxposition.add(i);
                    startyposition.add(j);
                }
            }
        }

        Random rand = new Random();

        this.state.xPosition = startxposition.get(rand.nextInt(startyposition.size()));
        this.state.yPosition = startyposition.get(rand.nextInt(startyposition.size()));//
        this.state.xSpeed = 0;
        this.state.ySpeed = 0;
    }

    public void printCarPosition(char[][] course) {
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                if (i == this.state.xPosition && j == this.state.yPosition) {
                    System.out.print("C");
                    continue;
                }

                System.out.print(course[i][j]);
            }
            System.out.println("");
        }
    }

    // based on the current and previous position of the car, determine the reward
    // by seeing the position landed on as well as the spaces crosses
    public int getReward(char[][] course, int previousXPostion, int previousYPosition, Boolean badCrash) {
        int reward = 0;
        // The car has crossed the finish line
        if (bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course, 'F')) {
            return Integer.MAX_VALUE;
        }
        // normal position

        else if (course[this.state.xPosition][this.state.yPosition] == '.') {
            reward = 100;
        }
        // The car has reached a checkpoint itteration
        else if (bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course,
                'p')) {
            reward = 110;
        }
        

        // The car has landed or crossed a wall
        else if (bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course,
                '#')) {
            if (badCrash) {
                reward = Integer.MIN_VALUE;
            } else {
                reward = -1000;
            }

        }

        return reward;
    }

    // This method will apply the action selected. the return is the reward.
    // This method can be called in 2 way depending of the Boolean valueIteration.
    // If it's null then the car will have 20% probability of not performing the
    // given action
    // If it's true then the action will be performed(used in value iteration)
    // If it's false then the action will not be performed(used in value iteration)
    // The boolean badcrash is used to decide if the car is put back to the start if
    // it hits a wall
    public int apply_action(Action action, char[][] course, Boolean valueIterationMode, Boolean badCrash) {
        Double number = 0.0;
        if (valueIterationMode == null) {
            number = Math.random();
            // number = 1.0;
        } else {
            if (valueIterationMode) {
                number = 1.0;
            } else {
                number = 0.0;
            }

        }
        if (number <= .2) {// non determinism as required, 20% probability of not accelerating at all
        } else {
            this.state.xSpeed += action.xAcceleration;
            this.state.ySpeed += action.yAcceleration;

            // check speed boundaries
            this.state.xSpeed = (this.state.xSpeed > 5) ? 5 : this.state.xSpeed;
            this.state.xSpeed = (this.state.xSpeed < -5) ? -5 : this.state.xSpeed;
            this.state.ySpeed = (this.state.ySpeed > 5) ? 5 : this.state.ySpeed;
            this.state.ySpeed = (this.state.ySpeed < -5) ? -5 : this.state.ySpeed;
        }

        // backup in case we hit a wall
        int previousXPostion = this.state.xPosition;
        int previousYPosition = this.state.yPosition;
        this.state.xPosition += this.state.xSpeed;
        this.state.yPosition += this.state.ySpeed;

        // check track boundaries or if car crossed a wall
        int reward;
        if (bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course, 'F')) {
            reward=Integer.MAX_VALUE;
            initCarPositon(course,'F');
        } else if (isOutOfBound(this.state.xPosition, this.state.yPosition, course) || bresenham(this.state.xPosition,
                this.state.yPosition, previousXPostion, previousYPosition, course, '#')) {
            this.state.xSpeed = 0;
            this.state.ySpeed = 0;

            if (badCrash) {
                initCarPositon(course,'S');
                reward = Integer.MIN_VALUE;
            } else {
                this.state.xPosition = previousXPostion;
                this.state.yPosition = previousYPosition;
                reward = -1000;
            }
            // reward = this.getReward(course, previousXPostion, previousYPosition);

        } else {
            reward = this.getReward(course, previousXPostion, previousYPosition, badCrash);
        }

        return reward;
    }

    // This function deals only with integers in order to determine if a given
    // character is crossed from the previous position to the current position
    Boolean bresenham(int x1, int y1, int x2, int y2, char[][] course, char cross) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (Boolean.TRUE) {
            if (!isOutOfBound(x1, y1, course)) {
                if (course[x1][y1] == cross) {
                    return Boolean.TRUE;
                }
            }

            if (x1 == x2 && y1 == y2)
                break;
            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return false;
    }

    Boolean isOutOfBound(int x, int y, char[][] course) {
        if (x >= course.length || y >= course[0].length || x < 0 || y < 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}