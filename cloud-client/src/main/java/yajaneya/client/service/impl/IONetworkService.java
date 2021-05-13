package yajaneya.client.service.impl;

import yajaneya.client.service.NetworkService;

import java.io.*;
import java.net.Socket;

public class IONetworkService implements NetworkService {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8189;

    private static IONetworkService instance;

    private static Socket socket;
    public static DataInputStream in;
    public static DataOutputStream out;

    NettySendFileService nettySendService;

    private IONetworkService() {}

    public static IONetworkService getInstance() {
        if (instance == null) {
            instance = new IONetworkService();

            initializeSocket();
            initializeIOStreams();
        }

        return instance;
    }

    public void auth() {
        try {
            out.writeUTF("auth");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeIOStreams() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeSocket() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e)  {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCommand(String command) {
        try {
            out.writeUTF(command);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendFile(File file) {

        nettySendService = new NettySendFileService(8188);
        nettySendService.start();
        // реализовать более аккуратно вместо задержки
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nettySendService.sendFile(file);
    }

    @Override
    public void stopSendService() {
        nettySendService.stop();
    }

    @Override
    public String readCommandResult() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Сбой при чтении команды: " + e.getMessage());
        }


    }

    @Override
    public void closeConnection() {

        closeInStream();
        closeOutStream();
        closeSocket();

    }

    private void closeInStream() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeOutStream() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
