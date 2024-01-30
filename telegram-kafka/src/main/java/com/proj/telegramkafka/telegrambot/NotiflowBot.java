package com.proj.telegramkafka.telegrambot;

import com.proj.telegramkafka.service.TelegramUserService;
import com.proj.telegramkafka.exception.NotiflowBotException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Slf4j
public class NotiflowBot extends TelegramLongPollingBot {

    private final ExecutorService executorService;
    private final TelegramUserService telegramUserService;


    public NotiflowBot(String token, TelegramUserService telegramUserService) {
        super(token);
        this.executorService = Executors.newFixedThreadPool(10);
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.submit(() -> handleUpdate(update));
    }

    private void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        saveUserIfNotExist(update, chatId);
        sendCommand(chatId);
    }

    private void saveUserIfNotExist(Update update, long chatId) {
        String username = "@" + update.getMessage().getChat().getUserName();
        telegramUserService.createIfNotExist(username, chatId);
    }

    private synchronized void sendCommand(long chatId) {
        sendMsg(chatId, "Thanks for using our notification Service❤️" +
                "\uD83C\uDDFA\uD83C\uDDE6\uD83C\uDDFA\uD83C\uDDE6\uD83C\uDDFA\uD83C\uDDE6");
    }

    public synchronized void sendMsg(long chatId, String text) {
        SendMessage sendMessage = createSendMessage(chatId, text);
        executeMessage(sendMessage);
    }

    private synchronized SendMessage createSendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    private synchronized void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("{}, Exception while executing message: {}", Level.SEVERE, e.toString());
            throw new NotiflowBotException("Sorry, it`s our mistake, please contact to our customer support.");
        }
    }

    @Override
    public String getBotUsername() {
        return "notiflow_bot";
    }
}
