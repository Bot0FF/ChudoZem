package com.bot0ff.controller;

import com.bot0ff.component.MessageExecutor;
import com.bot0ff.component.button.LocationButton;
import com.bot0ff.component.button.RegistrationSideButton;
import com.bot0ff.entity.Unit;
import com.bot0ff.repository.LocationRepository;
import com.bot0ff.service.RegistrationService;
import com.bot0ff.util.EmojiService;
import com.bot0ff.util.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
@AllArgsConstructor
public class RegistrationController {
    private final MessageExecutor messageExecutor;
    private final RegistrationService registrationService;
    private final EmojiService emojiService;
    private final ExceptionMessage exceptionMessage;
    private final LocationRepository locationRepository;

    //регистрация нового пользователя если его нет в бд
    public void processRegistrationUnitIsNull(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        var unit = registrationService.registrationUnitAndCheckName(update);

        if(unit.getName().equals("TEMP_NAME")) {
            sendMessage.setText("Приветствуем тебя, " + update.getMessage().getFrom().getUserName()
                    + "\nК сожалению, это имя занято "
                    + "\nВыбери другое имя:");
        }
        else  {
            //TODO первичное описание игры при первом входе
            sendMessage.setText("Приветствуем тебя, " + update.getMessage().getFrom().getUserName() +
                    "\nВыбери сторону:");
            sendMessage.setReplyMarkup(RegistrationSideButton.registrationSideButton());
        }

        var sentMessage = messageExecutor.sendMessage(sendMessage);
        registrationService.saveUnitMessageId(unit, sentMessage.getMessageId());
    }

    //регистрация нового пользователя на этапе выбора имени
    public void processRegistrationUnitIfNameExist(Update update, EditMessageText editMessageText) {
        if(update.getMessage().getText().startsWith("/")) {
            editMessageText.setText("\nИмя не может начинаться " +
                                    "\nс этого символа " +
                                    "\nВыбери другое имя:");
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        if(registrationService.checkNameForRegistrationRepeat(update)) {
            //TODO первичное описание игры при первом входе
            editMessageText.setText("Приветствуем тебя, " + update.getMessage().getText() +
                                    "\nВыбери сторону:");
            editMessageText.setReplyMarkup(RegistrationSideButton.registrationSideButton());
        }
        else {
            editMessageText.setText("\nК сожалению, это имя занято " +
                                    "\nВыбери другое имя:");
        }
        messageExecutor.sendEditMessage(editMessageText);
    }

    //отправляет выбор стороны заново, если на этом этапе пришел текстовый ответ от user
    public void processRegistrationUserSide(EditMessageText editMessageText, Unit unit) {
        editMessageText.setText("Приветствуем тебя, " + unit.getName() +
                                "\nВыбери сторону:");
        editMessageText.setReplyMarkup(RegistrationSideButton.registrationSideButton());
        messageExecutor.sendEditMessage(editMessageText);
    }

    //завершение регистрации после выбора стороны
    @Transactional
    public void processFinishRegistration(Unit unit, String side, EditMessageText editMessageText) {
        var location = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);

        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        registrationService.saveUnitSide(unit, side);

        //TODO добавить описание при первом попадании на локацию
        editMessageText.setText(emojiService.getUnitStateOnLocation("Добро пожаловать в игру! \n", unit, location));
        editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
        messageExecutor.sendEditMessage(editMessageText);
        log.info(String.format("New Unit: %s", unit));
    }
}
