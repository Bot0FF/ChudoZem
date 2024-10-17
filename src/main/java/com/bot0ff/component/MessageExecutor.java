package com.bot0ff.component;

import com.bot0ff.configuration.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
@RequiredArgsConstructor
public class MessageExecutor {
    private final TelegramBot telegramBot;

    public Message sendMessage(SendMessage sendMessage) {
        try {
            return telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean deleteMessage(DeleteMessage deleteMessage) {
        try {
            telegramBot.execute(deleteMessage);
            return true;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean sendEditMessage(EditMessageText editMessageText) {
        try {
            telegramBot.execute(editMessageText);
            return true;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void sendAnswerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            telegramBot.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
