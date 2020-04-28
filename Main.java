/**
 * Main
 */

public class Main {
    public static void main(String[] args) {
        String filename = "L-track.txt";
        ReadFile read = new ReadFile(filename);
        char[][] course = read.getCourse();
        Racecar racecar = new Racecar(course);
        new ValueIteration(course, 91, .8);
        //new Qlearning(racecar, course,0.8,0.3);
    }
}
