
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
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

        // Gets Lines Using Paul's CSV Loader
        try {
            lines = CSVLoader.loadCSV("test.csv");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


        //Making a column for each thing in the csv file 
        //for(Object column : lines.get(0))
        for (int i = 0; i < lines.get(0).length; i++)
        {
            String column = lines.get(0)[i];

            final int quack = i;

            ////figure this out
            TableColumn<String[], String> addcol = new TableColumn<>(column.toString().replaceAll("\"", ""));
            addcol.setCellValueFactory( e -> 
            {
                String[] x = e.getValue();
                return new SimpleStringProperty(x[quack].replaceAll("\"", ""));
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

        //Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
