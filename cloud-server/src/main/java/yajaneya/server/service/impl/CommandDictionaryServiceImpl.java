package yajaneya.server.service.impl;

import yajaneya.server.factory.Factory;
import yajaneya.server.service.ClientService;
import yajaneya.server.service.CommandDiсtionaryService;
import yajaneya.server.service.CommandService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDictionaryServiceImpl implements CommandDiсtionaryService {

    private final Map<String, CommandService> commandDictionary;

    public CommandDictionaryServiceImpl() {
        commandDictionary = Collections.unmodifiableMap(getCommonDictionary());
    }

    private Map<String, CommandService> getCommonDictionary() {
        List<CommandService> commandServices = Factory.getCommandServices();

        Map<String, CommandService> commandDictionary = new HashMap<>();
        for (CommandService commandService : commandServices) {
            commandDictionary.put(commandService.getCommand(), commandService);
        }

        return commandDictionary;
    }

    @Override
    public String processCommand(String command, ClientService clientService) {
        String[] commandParts = command.split("\\n");

        if (commandParts.length > 0 && commandDictionary.containsKey(commandParts[0])) {
            String commandResult = commandDictionary.get(commandParts[0]).processCommand(command, clientService);
            return commandResult;
        }

        return "errorCommand";
    }
}