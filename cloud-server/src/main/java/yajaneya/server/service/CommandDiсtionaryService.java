// словарь команд, осуществляющий подбор и выполнение заявленной команды

package yajaneya.server.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface CommandDiсtionaryService {

    String processCommand(String command, ClientService clientService);  // запуск на выполнение заявленной команды

}
