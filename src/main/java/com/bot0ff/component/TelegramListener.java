package com.bot0ff.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
@RequiredArgsConstructor
public class TelegramListener {
    private final UpdateHandler updateHandler;

    //распределение сообщений по статусу
    @EventListener(Update.class)
    public void telegramListener(Update update) {
        if(update == null) {
            log.error("Received update is null");
            return;
        }
        if(update.hasMessage()) {
            updateHandler.processTextMessage(update);
        }
        else if(update.hasCallbackQuery()) {
            updateHandler.processInlineMessage(update);
        }
    }
}
