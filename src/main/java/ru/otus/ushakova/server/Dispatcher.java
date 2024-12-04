package ru.otus.ushakova.server;

import lombok.extern.slf4j.Slf4j;
import ru.otus.ushakova.server.app.item.ItemsRepository;
import ru.otus.ushakova.server.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Dispatcher {

    private Map<String, RequestProcessor> processors;
    private RequestProcessor defaultNotFoundProcessor;
    private RequestProcessor defaultInternalServerErrorProcessor;
    private RequestProcessor defaultBadRequestProcessor;
    private final RequestProcessor defaultNotAllowedProcessor;

    private ItemsRepository itemsRepository;

    public Dispatcher() {
        this.itemsRepository = new ItemsRepository();
        this.processors = new HashMap<>();
        this.processors.put("GET /", new HelloWorldProcessor());
        this.processors.put("GET /calculator", new CalculatorProcessor());
        this.processors.put("GET /items", new GetAllItemsProcessor(itemsRepository));
        this.processors.put("POST /items", new CreateNewItemsProcessor(itemsRepository));
        this.defaultNotFoundProcessor = new DefaultNotFoundProcessor();
        this.defaultInternalServerErrorProcessor = new DefaultInternalServerErrorProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        this.defaultNotAllowedProcessor = new DefaultNotAllowedProcessor();
    }

    public void execute(HttpRequest request, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(request.getRoutingKey())) {
                defaultNotFoundProcessor.execute(request, out);
                return;
            }
            if (processors.containsKey(request.getRoutingKey()) && request.getRoutingKey().equals("POST /items")) {
                defaultNotAllowedProcessor.execute(request, out);
                return;
            }
            processors.get(request.getRoutingKey()).execute(request, out);
        } catch (BadRequestException e) {
            log.error("Невозможно обработать некорректный запрос: ", e);
            request.setException(e);
            defaultBadRequestProcessor.execute(request, out);
        } catch (Exception e) {
            log.error("Ошибка на стороне сервера: ", e);
            defaultInternalServerErrorProcessor.execute(request, out);
        }
    }

}