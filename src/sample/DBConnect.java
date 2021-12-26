package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.lang.String;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DBConnect {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-project", "faridun", "faridun12332112");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Connection");
            alert.setHeaderText(null);
            alert.setContentText("Connection Successful");
            alert.showAndWait();
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database Connection");
            alert.setHeaderText(null);
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();

        }

        return con;
    }

    /** I DONT KNOW, WHY DATABASE
     *  CONNECTION IS NOT WORKING,
     *  I DID EVERYTHING, BUT I DIDNT GET
     * WHERE IS A PROBLEM... */

}
