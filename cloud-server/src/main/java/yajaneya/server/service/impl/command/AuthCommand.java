package yajaneya.server.service.impl.command;

import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandService;

public class AuthCommand implements CommandService {

    @Override
    public String processCommand(String command, ClientService clientService) {
        final int requirementCountCommandParts = 3;

        String[] actualCommandParts = command.split("\\n");
        if(actualCommandParts.length != requirementCountCommandParts) {
            throw new IllegalArgumentException("Команда \"" + getCommand() + "\" не корректна.");
        }

        return process(actualCommandParts[1], actualCommandParts[2]);
    }

    private String process(String login, String password) {
        return "authOK";
    }

    @Override
    public String getCommand() {
        return "auth";
    }
}
