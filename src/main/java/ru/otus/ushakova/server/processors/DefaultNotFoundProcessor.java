package ru.otus.ushakova.server.processors;

import ru.otus.ushakova.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotFoundProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        var response = "" +
                "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>Page Not Found</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}