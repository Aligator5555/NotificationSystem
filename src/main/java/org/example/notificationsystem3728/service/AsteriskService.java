package org.example.notificationsystem3728.service;

import jakarta.annotation.PostConstruct;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.EventsAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.response.CoreStatusResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class AsteriskService {

    private static final Logger log = LoggerFactory.getLogger(AsteriskService.class);
    private ManagerConnection connection;
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public AsteriskService() {
        this.host = "asterisk";
        this.port = 5038;
        this.username = "admin";
        this.password = "adminpassword";
    }

    public void startAsterisk() throws AuthenticationFailedException, IOException, TimeoutException {
        int maxRetries = 10;
        int retryDelaySeconds = 5;

        for (int i = 0; i < maxRetries; i++) {
            try {
                connection = new DefaultManagerConnection(host,port,username,password);
                connection.login();
                log.info("Успешный запуск Asterisk");
                return;
            } catch (IOException e) {
                log.warn("Не удалось подключиться к Asterisk (попытка {}/{}) : {}", i + 1, maxRetries, e.getMessage());
                if (i < maxRetries - 1) {
                    try { Thread.sleep(retryDelaySeconds * 1000); } catch (InterruptedException ie) { log.error("Sleep interrupted", ie); }
                }
            }
        }
        throw new IOException("Не удалось подключиться к Asterisk после нескольких попыток");

    }
    @PostConstruct
    public void init(){
        try {
            startAsterisk();
            runMethod();
        } catch (IOException | AuthenticationFailedException | TimeoutException e) {
            log.error("Критическая ошибка: не удалось подключиться к Asterisk. Приложение остановится.", e);
            // В реальном проекте здесь лучше выбросить исключение, чтобы контейнер понял, что сервис не готов
            throw new RuntimeException(e);
        }
    }

    public void runMethod() throws AuthenticationFailedException, IOException, TimeoutException {

        OriginateAction originateAction;
        ManagerResponse managerResponse;

        originateAction = new OriginateAction();
        originateAction.setChannel("SIP/1300");
        originateAction.setContext("default");
        originateAction.setExten("89188797964");
        originateAction.setPriority(1);
        originateAction.setTimeout(30000);

        managerResponse = connection.sendAction(originateAction,30000);
        System.out.println(managerResponse.getResponse());

        connection.logoff();
    }



}
