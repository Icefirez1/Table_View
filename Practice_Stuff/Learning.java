package Practice_Stuff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Learning {

    
    public static void addIt(String e)
    {
        String[] out = e.split(", "); 
        List<String> yuh = Arrays.asList(out);
        System.out.println(yuh.toString());
    }
    public static void main(String[] args) throws IOException
    {
        Path filePath = Path.of("freshman_lbs.csv");
 
        try(BufferedReader br = Files.newBufferedReader(filePath))
        {
            br.lines().forEach(e -> addIt(e));
        }
        catch (NoSuchFileException ex)
        {
            System.err.println("File does not exsist");
        }
    }
}
