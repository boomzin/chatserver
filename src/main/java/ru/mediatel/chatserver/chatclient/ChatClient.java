package ru.mediatel.chatserver.chatclient;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class ChatClient {

    private static final String SERVER_URL = "http://localhost:8080/chat";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        String chatId = startChat();
        System.out.println("✅ Новый чат: " + chatId);
        System.out.println("Напишите сообщение (или 'exit' для выхода)");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Вы: ");
            String message = scanner.nextLine();

            if ("exit".equalsIgnoreCase(message)) {
                System.out.println("👋 Завершение чата");
                break;
            }

            String reply = sendMessage(chatId, message);
            System.out.println("🤖 Ассистент: " + reply);
        }
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