package ru.otus.ushakova.server.processors;

import lombok.extern.slf4j.Slf4j;
import ru.otus.ushakova.server.BadRequestException;
import ru.otus.ushakova.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CalculatorProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (!request.containsParameter("a")) {
            log.error("Parameter 'a' is missing");
            throw new BadRequestException("Parameter 'a' is missing");
        }
        if (!request.containsParameter("b")) {
            log.error("Parameter 'b' is missing");
            throw new BadRequestException("Parameter 'b' is missing");
        }
        int a, b;
        try {
            a = Integer.parseInt(request.getParameter("a"));
        } catch (NumberFormatException e) {
            log.error("Parameter 'a' has incorrect type");
            throw new BadRequestException("Parameter 'a' has incorrect type");
        }
        try {
            b = Integer.parseInt(request.getParameter("b"));
        } catch (NumberFormatException e) {
            log.error("Parameter 'b' has incorrect type");
            throw new BadRequestException("Parameter 'b' has incorrect type");
        }

        var math = a + " + " + b + " = " + (a + b);
        var response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>" + math + "</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}