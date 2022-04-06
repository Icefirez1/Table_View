import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PlayGround extends Application
{
    /**
     * Constructor with constant variables
     */
    public PlayGround()
    {   

    }
    /**
     * Initializes objects 
     */
    @Override
    public void init()
    {

    }
    /**
     * Application starting
     */
    @Override
    public void start(Stage primary)
    {
        TableView tableView = new TableView<>();
        


        //adding and running program
        VBox vbox  = new VBox(tableView);
        Scene scene = new Scene(vbox);
        primary.setScene(scene);
        primary.show();
    }
    /**
     * stopping application
     */
    @Override 
    public void stop()
    {

    }
}