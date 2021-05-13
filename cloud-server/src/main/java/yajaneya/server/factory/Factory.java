package yajaneya.server.factory;

import yajaneya.server.service.*;
import yajaneya.server.service.ClientService;
import yajaneya.server.service.impl.*;
import yajaneya.server.service.impl.command.*;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Factory {

    public  static ServerService getServerService() {
        return SocketServerService.getInstance();
    }


    public static ClientService getClientService(Socket socket) {
        return new IOClientService(socket);
    }


    public static CommandDiсtionaryService getCommandDictionaryService() {
        return new CommandDictionaryServiceImpl();
    }

    public static List<CommandService> getCommandServices(){
        return Arrays.asList(
                new AuthCommand(),
                new DelFileCommand(),
                new NewDirCommand(),
                new ReceiveFileCommand(),
                new ViewFilesInDirCommand()
        );
    }
}
