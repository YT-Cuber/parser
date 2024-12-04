package org.ytcuber.telegrambot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer implements CommandLineRunner {

    private final TelegramBot telegramBot;

    public BotInitializer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Инициализация TelegramBotsApi с сессией по умолчанию
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Регистрация бота
            telegramBotsApi.registerBot(telegramBot);
            System.out.println("Bot is registered successfully!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
