package ru.otus.ushakova.server.processors;

import com.google.gson.Gson;
import ru.otus.ushakova.server.HttpRequest;
import ru.otus.ushakova.server.app.item.Item;
import ru.otus.ushakova.server.app.item.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllItemsProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public GetAllItemsProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        List<Item> items = itemsRepository.getItems();
        Gson gson = new Gson();
        String itemsJson = gson.toJson(items);

        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                itemsJson;
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}