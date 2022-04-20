import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;


public class TabView extends Application
{
    TableView table = new TableView<>(); 
    List<List> lines = new ArrayList<>();


    /**
     * Variables and stuff ig
     */
    public TabView()
    {


    }
    /**
     * Starting application
     * @throws IOException
     */
    public void start(Stage primary) throws IOException
    {

        //Getting CSV file and putting it to a list of strings
        Path filePath = Path.of("freshman_lbs.csv");
        try(BufferedReader br = Files.newBufferedReader(filePath))
        {
            br.lines().forEach(   e -> lines.add(Arrays.asList(e.split(", "))));
        }
        catch (NoSuchFileException ex)
        {
            System.err.println("File does not exsist");
        }

        //Making a column for each thing in the csv file 
        for(Object column : lines.get(0))
        {
            //figure this out
        }


        System.out.println(lines);
        

        //Showing stuff 
        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox);
        primary.setScene(scene);
        primary.show();

    }
}
