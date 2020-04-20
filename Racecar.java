import java.util.ArrayList;
import java.util.Random;

public class Racecar {


    State state=new State();
    Action action=new Action();
    public Racecar(char[][] course) {
        ArrayList<Integer> startxposition=new ArrayList<>();
        ArrayList<Integer> startyposition=new ArrayList<>();
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                if (course[i][j]=='S') {
                    startxposition.add(i);
                    startyposition.add(j);
                }
            }
        }
        Random random=new Random();
        this.state.xPosition=startxposition.get(random.nextInt(startxposition.size()));
        this.state.yPosition=startyposition.get(random.nextInt(startyposition.size()));
        printCarPosition(course);
    }

    public void printCarPosition(char[][] course){
        for (int i = 0; i < course.length; i++) {
            for (int j = 0; j < course[i].length; j++) {
                if(i==this.state.xPosition&&j==this.state.yPosition){
                    System.out.print("C");
                    continue;
                }
                System.out.print(course[i][j]);
            }
            System.out.println("");
        }
    }
    public int getReward(char[][] course){
        int reward=10;
        if(course[this.state.xPosition][this.state.yPosition]=='.'){
            reward=100;
        }
        if(course[this.state.xPosition][this.state.yPosition]=='#'){
            reward=-1000;
        }
        if(course[this.state.xPosition][this.state.yPosition]=='F'){
            reward=1000;
        }
        return reward;
    }
    public void apply_action(Action action, char[][] course) {
        if(Math.random() <= .2){//non determinism as required
            return;
        }

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

        this.state.xPosition += this.state.xSpeed;
        this.state.yPosition += this.state.ySpeed;

        //check track boundaries
        if(this.state.xPosition >= course.length){
            this.state.xPosition = course.length-1;
        }
        if(this.state.yPosition >= course[0].length){
            this.state.yPosition = course[0].length-1;
        }
        if(this.state.xPosition < 0){
            this.state.xPosition=0;
        }
        if(this.state.yPosition < 0){
            this.state.yPosition=0;
        }
    }
}