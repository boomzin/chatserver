package ru.mediatel.chatserver.chatclient;

import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ChatClient {

    private static final String DEFAULT_SERVER_URL = "http://localhost:8080/chat";
    private static final HttpClient client = HttpClient.newHttpClient();

    private static String SERVER_URL = DEFAULT_SERVER_URL;

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            SERVER_URL = args[0];
        }
        System.out.println("SERVER_URL: " + SERVER_URL);

        Charset charset = usedCharset();
        System.out.println("Используемая кодировка: " + charset);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("\nЗавершение работы (Ctrl+C)")));

        String chatId = startChat();
        System.out.println("✅ Новый чат: " + chatId);
        System.out.println("Напишите сообщение (или 'exit' для выхода)");

        try (Scanner scanner = new Scanner(System.in, charset)) {
            while (true) {
                System.out.print("Вы: ");
                // ждем ввод
                if (!scanner.hasNextLine()) {
                    break; // входной поток закрыт (например, Ctrl+C)
                }
                // если ввод получен, читаем его
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("👋 Завершение чата");
                    break;
                }

                String reply = sendMessage(chatId, message);
                System.out.println("🤖 Ассистент: " + reply);
            }
        }


    }

    /**
     * Для терминала windows (cmd, ps) проблема с русскими символами
     * Там могут быть 3 кодировки CP866, CP1251, UTF-8
     * Команда chcp 65001 меняет на utf-8, 1251, 866
     * По умолчанию 866
     * Если ничего не делать в коде, то Scanner неправильно читает
     * Если просто поменять кодировку в консоли, то ломается еще и вывод
     * Если запускать в idea, то вывод сразу поломан как и ввод
     * С изменениями в коде
     * Не меняя кодировки, все работает, но не отображаются смайлики
     * Если поменять в консоли кодировку на 65001, то еще и смайлики начинают отображаться
     * В idea сразу со смайликами отображается
     * @return - Используемая кодировка
     */
    private static Charset usedCharset() {
        Charset charset = System.console() != null ? System.console().charset() : Charset.defaultCharset();
        PrintStream out = new PrintStream(System.out, true, charset);
        System.setOut(out);
        return charset;
    }

    private static String startChat() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/start"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().replace("\"", "");
    }

    private static String sendMessage(String chatId, String message) throws Exception {
        String json = String.format("{\"chatId\":\"%s\",\"message\":\"%s\"}", chatId, message);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/message"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}