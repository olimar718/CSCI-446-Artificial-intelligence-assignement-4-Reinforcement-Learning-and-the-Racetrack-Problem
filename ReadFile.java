import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadFile {
    private char[][] course;
    
    public ReadFile(String file){
        File f = new File(file);
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new FileReader(f));
            //get array size and init new course
            String[] size=buff.readLine().split(",");          
            this.course=new char[Integer.parseInt(size[0])][Integer.parseInt(size[1])];
            
            String line = "";
            int i = 0;
            while((line = buff.readLine()) != null){
                // store each character in the course array
                course [i] = line.toCharArray();
                i++;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

    char[][] getCourse(){
        return course;
    }
}