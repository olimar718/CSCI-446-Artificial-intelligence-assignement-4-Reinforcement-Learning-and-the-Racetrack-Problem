import java.lang.Math;
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
        this.state.xPosition = startxposition.get(0);//this.state.xPosition = startxposition.get(random.nextInt(startxposition.size()));
        this.state.yPosition = startyposition.get(0);//this.state.yPosition = startyposition.get(random.nextInt(startyposition.size()));
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
        // if (Math.random() <= .2) {// non determinism as required
        // } else {
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
        // }

        // backup in case we hit a wall
        int previousXPostion = this.state.xPosition;
        int previousYPosition = this.state.yPosition;
        this.state.xPosition += this.state.xSpeed;
        this.state.yPosition += this.state.ySpeed;

        // check track boundaries and if it crossed a wall

        //TEST
        // bresenham(3, 5, 5, 1, course, '#');
        int reward=0;
        if (this.state.xPosition >= course.length || this.state.yPosition >= course[0].length
                || this.state.xPosition < 0 || this.state.yPosition < 0) {

            this.state.xSpeed = 0;
            this.state.ySpeed = 0;
            this.state.xPosition = previousXPostion;
            this.state.yPosition = previousYPosition;
            reward += -1000;
        } 
        else if(bresenham(this.state.xPosition, this.state.yPosition, previousXPostion, previousYPosition, course, '#')){
            reward -= 1000;
        }
        else {
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

    boolean bresenham(int x1, int y1, int x2, int y2, char[][] course, char cross){
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int err = dx-dy;
        int e2;

        while(true){
            if(course[x1][y1] == cross){
                return true;
            }
            if(x1 == x2 && y1 == y2)
                break;
            e2 = 2 * err;
            if(e2 > -dy){
                err = err - dy;
                x1 = x1 + sx;
            }
            if(e2 < dx){
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return false;
    }
}