package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class topScoreController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Users> usersTable;

    @FXML
    private TableColumn<Users, String> col_name;

    @FXML
    private TableColumn<Users, String> col_score;

    @FXML
    private Button leaveButton;

    ObservableList<Users> listM = FXCollections.observableArrayList();

    int index = -1;

    @FXML
    void switchToSam(MouseEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-project", "faridun", "faridun12332112");

            ResultSet resultSet = conn.createStatement().executeQuery("select * from users ORDER BY score DESC");

            while (resultSet.next()) {
                if (listM != null) {
                    listM.add(new Users(resultSet.getString("name"), resultSet.getString("score")));
                    System.out.println("CHECK FOR ADDING");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }



        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_score.setCellValueFactory(new PropertyValueFactory<>("score"));

        usersTable.setItems(listM);

    }
}
