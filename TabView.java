
import java.io.IOException;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.Arrays;

public class TabView extends Application
{
    TableView table = new TableView<>(); 
    ArrayList<String[]> lines = new ArrayList<>();
    ArrayList<TableColumn<String[], String>> column_list = new ArrayList<>();
    int quack; 


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
    @SuppressWarnings("unchecked")
    public void start(Stage primary) throws IOException
    {

        //Getting CSV file and putting it to a list of strings
        // Path filePath = Path.of("freshman_lbs.csv");
        // try(BufferedReader br = Files.newBufferedReader(filePath))
        // {
        //     br.lines().forEach(   e -> lines.add(Arrays.asList(e.split(", "))));
        // }
        // catch (NoSuchFileException ex)
        // {
        //     System.err.println("File does not exsist");
        // }

        // Gets Lines Using Paul's CSV Loader
        // his validation stuff is weird I gotta ask him about that (not like his code is bad)
        // I just 
        try {
            lines = CSVLoader.loadCSV("freshman_lbs.csv");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


        //Making a column for each thing in the csv file 
        for(Object column : lines.get(0))
        {
            //figure this out
            quack = Arrays.asList(lines.get(0)).indexOf(column);
            TableColumn<String[], String> addcol = new TableColumn<>(column.toString().replaceAll("\"", ""));
            addcol.setCellValueFactory( e -> 
            {
                String[] x = e.getValue();
                return new SimpleStringProperty(x != null && x.length>1 ? x[quack].replaceAll("\"", "") /*this you'll get the index */ : "<no value>");
            });
            table.getColumns().add(addcol);
            column_list.add(addcol);
        }
        lines.remove(0);


        table.getItems().addAll(lines); 
    

        //Showing stuff 
        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox);
        primary.setScene(scene);
        primary.show();

    }
}
