package yajaneya.server.service.impl;

import yajaneya.server.factory.Factory;
import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandDiсtionaryService;

import java.io.*;
import java.net.Socket;

public class IOClientService implements ClientService {

    private Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    private CommandDiсtionaryService diсtionaryService;

    public String nick;
    private final String PATH = "D:" + File.separator + "cloudFiles";

    public IOClientService(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.diсtionaryService = Factory.getCommandDictionaryService();

        initializeIOStreams();
    }

    private void initializeIOStreams() {
        try {
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public void startIOProcess() {
        new Thread( () -> {
            try {
                while (true) {
                    String clientCommand = readCommand();
                    String commandResult = diсtionaryService.processCommand(clientCommand, this);

                    writeCommandResult(commandResult);
                }
            } catch (RuntimeException ex) {
                System.out.println("Клиент " + nick + " отключился...");
            } catch (Exception ex) {
                System.err.println("Ошибка клиента: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }).start();
    }

    @Override
    public void writeCommandResult(String commandResult) {
        try {
            out.writeUTF(commandResult);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readCommand() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Сбой при чтении команды: " + e.getMessage());
        }
    }

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
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
