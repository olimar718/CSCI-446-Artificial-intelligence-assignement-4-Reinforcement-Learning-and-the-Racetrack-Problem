/**
 * Main
 */

public class Main {
    public static void main(String[] args) {
        String filename="L-track.txt";
        ReadFile read = new ReadFile(filename);
        char[][] course=read.getCourse();
        Racecar racecar=new Racecar(course);
        new Qlearning(racecar, course,0.9,0.2);
    }
}
