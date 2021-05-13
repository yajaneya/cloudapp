package yajaneya.server.service.impl.command;

import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandService;
import yajaneya.server.service.impl.NettyReceiveFileService;

import java.io.File;

public class ReceiveFileCommand implements CommandService {

    ClientService clientService;

    private final String PATH_CLOUD = "D:" + File.separator + "cloudFiles";

    @Override
    public String processCommand(String command, ClientService clientService) {

        this.clientService = clientService;

        final int requirementCountCommandParts = 4;

        String[] actualCommandParts = command.split("\\n");
        if(actualCommandParts.length != requirementCountCommandParts) {
            throw new IllegalArgumentException("Команда \"" + getCommand() + "\" не корректна.");
        }

        return process(actualCommandParts[1], actualCommandParts[2], actualCommandParts[3]);
    }

    private String process(String file, String sizeFile, String place) {
        File fileReceive;

        if (place.contains("*")) {
            fileReceive = new File(PATH_CLOUD + File.separator + file);
        } else {
                fileReceive = new File(PATH_CLOUD + File.separator + place + File.separator + file);
        }

        long lengthFile = Long.parseLong(sizeFile);

        if (fileReceive.exists()) {
            System.out.println("Такой файл уже существует");
            return "fileIsExist";
        }

        Thread th = new Thread(() -> {
            NettyReceiveFileService ns = new NettyReceiveFileService(8188, clientService, fileReceive, lengthFile);
            ns.startServer();
        });
        th.start();
        System.out.println("Сервер приема файла запущен");

        if (th.isAlive()) {
            return "sendFileOK";
        } else {
            return "sendFileProgress";
        }
    }

    @Override
    public String getCommand() {
        return "put";
    }
}
