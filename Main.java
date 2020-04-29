/**
 * Main
 */

public class Main {
    public static void main(String[] args) {
        String filename = "test-track.txt";
        ReadFile read = new ReadFile(filename);
        char[][] course = read.getCourse();
        Racecar racecar = new Racecar(course);
        new ValueIteration(course, Math.pow(10.0, 3), .9, Boolean.FALSE);
        //new Qlearning(racecar, course,0.8,0.3);
    }
}
