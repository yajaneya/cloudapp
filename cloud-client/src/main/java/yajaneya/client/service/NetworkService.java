// сервис работы клиента с сервером

package yajaneya.client.service;

import java.io.File;

public interface NetworkService {

    void auth(); // авторизация клиента

    void sendCommand (String command); // отправка команды

    String readCommandResult(); // чтение результата команды

    void closeConnection(); // закрытие соединения

    void sendFile(File file); // отправка файла

    void stopSendService();
}
