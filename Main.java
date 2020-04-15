/**
 * Main
 */

public class Main {
    public static void main(String[] args) {
        String filename="R-track.txt";
        ReadFile read = new ReadFile(filename);
        char[][] course=read.getCourse();
    }
}