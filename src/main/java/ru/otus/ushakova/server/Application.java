package ru.otus.ushakova.server;

public class Application {
    // Домашнее задание:
    // 1. Добавить обработку запросов в цикле
    // 2. Добавить обработку запросов в тред пуле

    public static void main(String[] args) {
        new HttpServer(8189).start();
    }
}