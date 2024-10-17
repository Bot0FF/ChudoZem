package com.bot0ff.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingCommandBot {
    private final ApplicationEventPublisher eventPublisher;
    private final String botName;
    private final String botToken;

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        eventPublisher.publishEvent(update);
    }
}
