package ru.otus.ushakova.server.processors;

import ru.otus.ushakova.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotAllowedProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        var response = "HTTP/1.1 405 Method Not Allowed\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>Method Not Allowed!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}