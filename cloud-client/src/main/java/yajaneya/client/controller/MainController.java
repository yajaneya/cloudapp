package yajaneya.client.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import yajaneya.client.factory.Factory;
import yajaneya.client.service.NetworkService;
import yajaneya.client.view.MessageFileView;
import yajaneya.client.view.SendFileView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {

    public NetworkService networkService;
    public Button sendToCloud;
    public Button delFromCloud;
    public Button newDirToCloud;
    public Button takeFromCloud;
    public Button test;
    public Button lsFromCloud;
    private Thread rh;
    private MessageFileView msgWindows;

    private final String PATH_CLIENT = "D:" + File.separator + "clientFiles";
    private final String PATH_CLOUD = "D:" + File.separator + "cloudFiles";

    private File sendFile;


    @FXML
    public MenuItem mnFileClose;
    public Button btAuth;
    public TextArea comResTextArea;
    public TreeView explServer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networkService = Factory.getNetworkService();
        msgWindows = new MessageFileView();

//        networkService.auth();

        loadDir();
        explServer.setShowRoot(false);

        createCommandResultHandler();
    }

    public TreeItem<String> getNodesForDirectory(File file) {

        TreeItem<String> root = new TreeItem<String>("CLOUD");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int i=0; i<nodeList.getLength(); i++) {

                TreeItem<String> el = new TreeItem (nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
                if (nodeList.item(i).getNodeName().equals("DIR")) {
                    el=getNodesFromOneDir(el, nodeList.item(i));
                }
                root.getChildren().add(el);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return root;
    }

    private TreeItem<String> getNodesFromOneDir(TreeItem<String> dir, Node node) {
        TreeItem<String> temp = new TreeItem<String>(node.getAttributes().getNamedItem("name").getNodeValue());
        NodeList nodeList = node.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++) {
            TreeItem<String> el;

            if (nodeList.item(i).getNodeName().equals("DIR")) {
                el = getNodesFromOneDir(temp, nodeList.item(i));
            } else {
                el = new TreeItem (nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue());
            }
             temp.getChildren().add(el);
        }
        return temp;
    }



    private void createCommandResultHandler() {
        byte[] buffer = new byte[1024];
        rh = new Thread(() -> {
            while (true) {
                String resultCommand = networkService.readCommandResult();
                 if (resultCommand.contains("readyReceiveFile")) {
                    networkService.sendFile(sendFile);
                }
                if (resultCommand.contains("endReceiveFile")) {
                    Platform.runLater(() -> {
                        loadDir();
                    });
                    networkService.stopSendService();
                }
                if (resultCommand.contains("delOK")
                        |resultCommand.contains("newDirOK")) {
                    Platform.runLater(() -> {
                        loadDir();
                    });
                }
                Platform.runLater(() -> comResTextArea.appendText("Server: " + resultCommand + System.lineSeparator()));
            }
        });
        rh.start();
    }

    private void loadDir () {
        networkService.sendCommand("ls\n.");
        explServer.setRoot(getNodesForDirectory(new File("dir_cloud.xml")));
    }

    public void shutdown() {
        rh.interrupt();
        networkService.sendCommand("/exit");
        networkService.closeConnection();
        System.exit(0);
    }

    public void close(ActionEvent actionEvent) {
        shutdown();
    }

    public void btnAuth(ActionEvent actionEvent) {
        messInProgress();
        networkService.sendCommand("auth\n.");
    }

    public void sendToCloud(ActionEvent actionEvent) {

        SendFileView sendFileView = new SendFileView();

        String pathCloud = "";

        TreeItem <String> selCloudItem = (TreeItem<String>) explServer.getSelectionModel().getSelectedItem();
        if ((selCloudItem != explServer.getRoot()) & (explServer.getSelectionModel().getSelectedItem() != null)) {
            pathCloud = pathFromTreeElement(selCloudItem.getParent(), explServer);
        }
        if (pathCloud.equals("")) pathCloud = "*";

        sendFile = sendFileView.openwin(pathCloud);

    }


    public void delFromCloud(ActionEvent actionEvent) {
        TreeItem <String> selItem = (TreeItem<String>) explServer.getSelectionModel().getSelectedItem();
        String path = pathFromTreeElement(selItem, explServer);

        networkService.sendCommand("del\n"+path);
    }

    public void newDirToCloud(ActionEvent actionEvent) {
        messInProgress();
        networkService.sendCommand("mkdir\n.");
    }

    public void takeFromCloud(ActionEvent actionEvent) {
        messInProgress();
        networkService.sendCommand("take\n.");
    }

    private String pathFromTreeElement (TreeItem <String> selItem, TreeView treeView) {
        String path = "";
        try {
            if (selItem != treeView.getRoot()) {
                path = selItem.getValue();
                while (selItem.getParent() != treeView.getRoot()) {
                    path = selItem.getParent().getValue() + File.separator + path;
                    selItem = selItem.getParent();
            }
            }

        } catch (NullPointerException e) {
            msgWindows.openwin("Сделайте выбор!");
        }

        return path;

    }

    public void lsFromCloud(ActionEvent actionEvent) {
        loadDir();
    }

    private void messInProgress () {
        msgWindows.openwin("Эта функция в процессе разработки...");
    }
}
