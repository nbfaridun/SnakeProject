package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class InstructionsController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button buttonConfirm;

    @FXML
    private CheckBox confirm1;

    @FXML
    private CheckBox confirm2;

    @FXML
    private CheckBox confirm3;

    public void switchToSample(ActionEvent event) throws IOException {
        if (confirm1.isSelected() && confirm2.isSelected() && confirm3.isSelected()) {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("READ CAREFULLY! TO WHOM I WROTE THEM?!");
            alert.showAndWait();
        }
    }
}
