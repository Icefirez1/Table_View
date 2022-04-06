import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.ObjectExpression;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PlayGround extends Application
{
    private ArrayList<String> fNames;
    private ArrayList<String> lNames; 
    private TableView tableView = new TableView<>();
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
    @SuppressWarnings("unchecked")
    public void start(Stage primary)
    {
        
        TableColumn<Person, String> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(
            new PropertyValueFactory<>("firstName"));
        TableColumn<Person, String> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);


        tableView.getItems().add(new Person("Eric", "Chen"));
        tableView.getItems().add(new Person("Ivan", "Zheng"));
        tableView.getItems().add(new Person("Your", "Mom"));



        //adding and running program
        VBox vbox  = new VBox(tableView);
        Scene scene = new Scene(vbox);
        primary.setScene(scene);
        primary.show();
    }
    /**
     * will add people to the thingy
     */
    
    public void addPeople()
    {
        //toDO: making input function to add people
        //textBox?
        //Scanner? 
    }
    /**
     * stopping application
     */
    @Override 
    public void stop()
    {

    }

    /*Person subclass*/
    public class Person 
    {
        public String firstName; 
        public String lastName; 

        /**
         * person constructor
         * @param firstName
         * @param lastName
         */
        public Person(String firstName, String lastName)
        {
            this.firstName = firstName; 
            this.lastName = lastName; 
        }
        /**
         * returns first name
         * @return First name
         */
        public String getFirstName()
        {
            return firstName;
        }
        /**
         * sets First name
         * @param newName
         */
        public void setFirstName(String newName)
        {
            this.firstName = newName; 
        }
        /**
         * returns last name
         * @return lastName
         */
        public String getLastName()
        {
            return lastName;
        }
        /**
         * sets Last name
         * @param newName
         */
        public void setLastName(String newName)
        {
            this.lastName = newName; 
        }
    }
}