/**
 * Main function to set the course and call the corresponding algorithms
 */

public class Main {
    public static void main(String[] args) {
        String filename = "L-track.txt";
        ReadFile read = new ReadFile(filename);
        char[][] course = read.getCourse();
        Racecar racecar = new Racecar(course);
        new ValueIteration(course, 10, .99, Boolean.TRUE);
        //new Qlearning(racecar, course,0.8,0.3,Boolean.TRUE);
    }
}
