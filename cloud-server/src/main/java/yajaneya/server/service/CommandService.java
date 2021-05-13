// обработчик команды

package yajaneya.server.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface CommandService {

    String processCommand(String command, ClientService clientService); // осущестляет выполнение команды, выдает результат выполнения

    String getCommand(); // выдает названия команды

}
