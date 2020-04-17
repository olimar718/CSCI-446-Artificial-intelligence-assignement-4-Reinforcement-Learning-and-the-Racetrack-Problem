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
}