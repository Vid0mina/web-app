package ru.otus.ushakova.server;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private final String rawRequest;
    private HttpMethod method;
    @Getter
    private String uri;
    private Map<String, String> parameters;
    @Getter
    private String body;
    @Getter
    @Setter
    private Exception exception;

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public HttpRequest(String rawRequest) throws IOException {
        this.rawRequest = rawRequest;
        this.parse();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public static synchronized void getHeader(@NonNull String rawRequest) throws IOException {
        Map<String, String> headersMap = new HashMap<>();
        String[] headers = rawRequest.split("\r\n");
        log.info("Request Array = {}", Arrays.toString(Arrays.stream(headers).toArray()));
        for (var i = 1; i < headers.length; i++) {
            String[] value = headers[i].split(":");
            headersMap.put(value[0], value[1]);
        }
        log.info("\nHeaders Result Map = {}", headersMap);
    }

    private void parse() {
        var startIndex = rawRequest.indexOf(' ');
        var endIndex = rawRequest.indexOf(' ', startIndex + 1);
        uri = rawRequest.substring(startIndex + 1, endIndex);
        method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keysValues = elements[1].split("[&]");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }
    }

    public void info(boolean debug) {
        if (debug) {
            log.info(rawRequest);
        }
        log.info("Method: {}\nURI: {}\nParameters: {}\nBody: {}", method, uri, parameters, body);
    }

}