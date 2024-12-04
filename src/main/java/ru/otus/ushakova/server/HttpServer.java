package ru.otus.ushakova.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.otus.ushakova.server.HttpRequest.getHeader;

@Slf4j
public class HttpServer {
    private int port;
    private Dispatcher dispatcher;
    static private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        executorService.execute(() -> {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Сервер запущен на порту: {}", port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    byte[] buffer = new byte[8192];
                    var n = socket.getInputStream().read(buffer);
                    if (n < 1) continue;
                    var rawRequest = new String(buffer, 0, n);
                    log.info("rawRequest = {}", rawRequest);
                    getHeader(rawRequest);
                    HttpRequest request = new HttpRequest(rawRequest);
                    request.info(true);
                    dispatcher.execute(request, socket.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        });
        executorService.shutdown();
    }

}