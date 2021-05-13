package yajaneya.server.service.impl.command;

import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandService;

import java.io.File;

public class DelFileCommand implements CommandService {

    private final String PATH_CLOUD = "D:" + File.separator + "cloudFiles";

    @Override
    public String processCommand(String command, ClientService clientService) {
        final int requirementCountCommandParts = 2;

        String[] actualCommandParts = command.split("\\n");
        if(actualCommandParts.length != requirementCountCommandParts) {
            throw new IllegalArgumentException("Команда \"" + getCommand() + "\" не корректна.");
        }

        return process(actualCommandParts[1]);
    }

    private String process(String file) {
        File delFile = new File(PATH_CLOUD + File.separator + file);
        Boolean isDel = delFile.delete();

        return (isDel) ? "delOK" : "delNotOK";
    }

    @Override
    public String getCommand() {
        return "del";
    }
}
