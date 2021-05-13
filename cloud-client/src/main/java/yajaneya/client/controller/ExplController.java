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

public class ExplController implements Initializable {

    public NetworkService networkService;
    public File sendFile;
    public String pathCloud;


    public Button sendButton;
    public Button cancelButton;
    public Label fileNameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkService = Factory.getNetworkService();
    }

    public void sendFile(ActionEvent actionEvent) {

        String pathClient = sendFile.getName();
        networkService.sendCommand("put\n" + pathClient + "\n" + sendFile.length() + "\n" + pathCloud);
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
