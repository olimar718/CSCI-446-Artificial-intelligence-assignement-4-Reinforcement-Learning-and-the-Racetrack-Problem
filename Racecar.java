//import.java.lang.*;
import java.util.ArrayList;
import java.util.Random;

public class Racecar {

    State state = new State();
    Action action = new Action();
    int[][] history;

    public Racecar(char[][] course) {

        this.history = new int[course.length][course[0].length];
        this.initCarPositon(course);
        printCarPosition(course);
    }

    public void initCarPositon(char[][] course) {
        ArrayList<Integer> startxposition = new ArrayList<>();
        ArrayList<Integer> startyposition = new ArrayList<>();
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                if (course[i][j] == 'S') {
                    startxposition.add(i);
                    startyposition.add(j);
                }
            }
        }
        Random random = new Random();
        this.state.xPosition = startxposition.get(random.nextInt(startxposition.size()));
        this.state.yPosition = startyposition.get(random.nextInt(startyposition.size()));
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

    public int getReward(char[][] course, int previousXPostion, int previousYPosition) {
        int reward = 0;
        if (course[this.state.xPosition][this.state.yPosition] == '.') {
            reward = 100;
        }
        if (course[this.state.xPosition][this.state.yPosition] == '#') {
            reward = -1000;
        }

        if (bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course, 'F')) {
            reward = 1000;
            return reward;
        }
        if (course[this.state.xPosition][this.state.yPosition] == 'F') {
            reward = 1000;
            return reward;
        }
        reward += this.history[this.state.xPosition][this.state.yPosition] * -100;
        this.history[this.state.xPosition][this.state.yPosition] += 1;

        return reward;
    }

    public int apply_action(Action action, char[][] course) {
        if (Math.random() <= .2) {// non determinism as required
        } else {
            this.state.xSpeed += action.xAcceleration;
            this.state.ySpeed += action.yAcceleration;

            // check speed boundaries
            if (this.state.xSpeed > 5) {
                this.state.xSpeed = 5;
            }
            if (this.state.xSpeed < -5) {
                this.state.xSpeed = -5;
            }
            if (this.state.ySpeed > 5) {
                this.state.ySpeed = 5;
            }
            if (this.state.ySpeed < -5) {
                this.state.ySpeed = -5;
            }
        }

        // backup in case we hit a wall
        int previousXPostion = this.state.xPosition;
        int previousYPosition = this.state.yPosition;
        this.state.xPosition += this.state.xSpeed;
        this.state.yPosition += this.state.ySpeed;

        // check track boundaries and if it crossed a wall
        DDA(1, 10, 2, 12, course, '#');
        int reward;
        if (this.state.xPosition >= course.length || this.state.yPosition >= course[0].length
                || this.state.xPosition < 0 || this.state.yPosition < 0 || bresenham(this.state.xPosition,
                        this.state.yPosition, previousXPostion, previousYPosition, course, '#')) {

            this.state.xSpeed = 0;
            this.state.ySpeed = 0;
            this.state.xPosition = previousXPostion;
            this.state.yPosition = previousYPosition;
            reward = this.getReward(course, previousXPostion, previousYPosition);
            reward += -1000;
        } else {
            reward = this.getReward(course, previousXPostion, previousYPosition);
            // check if we landed on a wall
            if (course[this.state.xPosition][this.state.yPosition] == '#') {
                this.state.xSpeed = 0;
                this.state.ySpeed = 0;
                this.state.xPosition = previousXPostion;
                this.state.yPosition = previousYPosition;
            }
        }

        return reward;
    }

    boolean bresenham(int x1, int y1, int x2, int y2, char[][] course, char cross) {
        int m_new = 2 * (y2 - y1);
        int slope_error_new = m_new - (x2 - x1);
        Boolean crossedWall = Boolean.FALSE;
        for (int x = x1, y = y1; x <= x2; x++) {
            // check for bound
            Boolean wasOutOfBound = Boolean.FALSE;
            int newx = x;
            int newy = y;
            if (x >= course.length) {
                wasOutOfBound = Boolean.TRUE;
                newx = course.length - 1;
            }
            if (y >= course[0].length) {
                wasOutOfBound = Boolean.TRUE;
                newy = course[0].length - 1;
            }
            if (x < 0) {
                wasOutOfBound = Boolean.TRUE;
                newx = 0;
            }
            if (y < 0) {
                wasOutOfBound = Boolean.TRUE;
                newy = 0;
            }

            // check if we cross a wall
            if (wasOutOfBound) {
                if (course[newx][newy] == cross) {
                    crossedWall = Boolean.TRUE;
                }

            } else {
                if (course[x][y] == cross) {
                    crossedWall = Boolean.TRUE;
                }
            }

            slope_error_new += m_new;

            if (slope_error_new >= 0) {
                y++;
                slope_error_new -= 2 * (x2 - x1);
            }
        }
        return crossedWall;
    }

    boolean DDA(int x1, int y1, int x2, int y2, char[][] course, char cross){
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps;
        if(Math.abs(dx) > Math.abs(dy)){
            steps = Math.abs(dx);
        }
        else{
            steps = Math.abs(dy);
        }
        Double xIncrement=dx/ (double)steps;
        Double yIncrement=dy/(double) steps;
        Double x = x1 + 0.0;
        Double y = y1+0.0;
        Boolean crossed=Boolean.FALSE;
        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;

            //check if cross
            if(course[(int)Math.round(y)][(int)Math.round(x)]=='#'){
                crossed=Boolean.TRUE;
            }
        }
        return crossed;
    }
}