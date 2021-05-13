package yajaneya.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import yajaneya.client.factory.Factory;
import yajaneya.client.service.NetworkService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageController {


    public Label messageLabel;
    public Button okButton;

    public void closeWindows(ActionEvent actionEvent) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
