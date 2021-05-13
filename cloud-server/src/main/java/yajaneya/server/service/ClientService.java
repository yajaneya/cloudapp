// сервис обслуживания подключившегося клиента

package yajaneya.server.service;

public interface ClientService {

    String getNick(); // ник подключившегося клиента

    void startIOProcess(); //запуск процесса коммуникации с подключившимся клиентом

    void writeCommandResult(String commandResult); //отправка результата выполнения команды

    String readCommand(); //получение команды

}
