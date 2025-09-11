package ru.mediatel.chatserver.rest;

public record ChatRequest(String chatId, String userId, String message) {}

