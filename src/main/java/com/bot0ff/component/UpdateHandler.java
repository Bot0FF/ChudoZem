package com.bot0ff.component;

import com.bot0ff.controller.LocationController;
import com.bot0ff.controller.RegistrationController;
import com.bot0ff.controller.TestController;
import com.bot0ff.controller.UnitController;
import com.bot0ff.entity.Unit;
import com.bot0ff.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateHandler {
    private final MessageExecutor messageExecutor;
    private final RegistrationController registrationController;

    private final TestController testController;
    private final UnitController unitController;
    private final LocationController locationController;
    private final UnitRepository unitRepository;

    @Async
    public void processTextMessage(Update update) {
        var requestMessage = update.getMessage().getText();
        var editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getMessage().getChatId().toString());

        //TODO логи запросов
        log.info("Входящее " + update.getMessage().getText());

        //удаление отправленного пользователем текстового сообщения
        deleteUserMessage(update);

        Unit unit = unitRepository.findById(update.getMessage().getFrom().getId()).orElse(null);
        if(requestMessage.equals("/start")) {
            if(unit == null) {
                registrationController.processRegistrationUnitIsNull(update);
                return;
            }
        }
        if(unit == null) return;
        editMessageText.setMessageId(unit.getMessageId());

        //распределение текстового запроса по контроллерам
        switch (unit.getStatus()) {
            case REGISTRATION_NAME -> {
                registrationController.processRegistrationUnitIfNameExist(update, editMessageText);
                return;
            }
            case REGISTRATION_SIDE -> {
                registrationController.processRegistrationUserSide(editMessageText, unit);
                return;
            }
            case FIGHT -> {}
        }

        String patternCommand = StringUtils.substringBetween(requestMessage, "/", ":");
        if(patternCommand == null) return;

        switch (patternCommand) {
            case "ADD" -> testController.setThingOnLocation(editMessageText, unit.getId(), requestMessage);

            case "BAG" -> unitController.getBag(editMessageText, unit.getId(), requestMessage);
            case "CHARACTERISTIC" -> {}
            default -> locationController.getLocationPage(editMessageText, unit.getId(), requestMessage);
        }
    }

    @Async
    public void processInlineMessage(Update update) {
        String requestMessage = update.getCallbackQuery().getData();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());

        //TODO логи запросов
        log.info("Входящее " + requestMessage);

        Unit unit = unitRepository.findById(update.getCallbackQuery().getFrom().getId()).orElse(null);
        if(unit == null) return;
        editMessageText.setMessageId(unit.getMessageId());

        if(Instant.now().isBefore(unit.getTimeActivityEnd())) {
            long second = unit.getTimeActivityEnd().getEpochSecond() - Instant.now().getEpochSecond();
            processActionDelayInline(answerCallbackQuery, second);
            return;
        }

        switch (unit.getStatus()) {
            case REGISTRATION_SIDE -> {
                registrationController.processFinishRegistration(unit, update.getCallbackQuery().getMessage().getText(), editMessageText);
                messageExecutor.sendAnswerCallbackQuery(answerCallbackQuery);
                return;
            }
            case FIGHT -> {}
        }

        String patternCommand = StringUtils.substringBetween(requestMessage, "/", ":");
        if(patternCommand == null) {
            messageExecutor.sendAnswerCallbackQuery(answerCallbackQuery);
            return;
        }
        switch (patternCommand) {
            //StateController
            case "LOCATION_PAGE" -> locationController.getLocationPage(editMessageText, unit.getId(), requestMessage);
            //MoveController
            case "MOVE" -> locationController.moveUnit(editMessageText, unit.getId(), requestMessage);
            //LocationController
            case "EXPLORE" -> locationController.processLocationExplorer( editMessageText, unit.getId(), requestMessage);
            //case "LOCALITY" -> responseDto = locationController.relocateLocality(actionDto, responseDto);
            case "UNITS" -> locationController.showUnitsList(editMessageText, unit.getId(), requestMessage);
            case "AIS" -> locationController.showAisList(editMessageText, unit.getId(), requestMessage);
            case "THINGS" -> locationController.showThingsList(editMessageText, unit.getId(), requestMessage);
            case "LOCALITY" -> locationController.applyRelocate(editMessageText, unit.getId(), requestMessage);
        }
        messageExecutor.sendAnswerCallbackQuery(answerCallbackQuery);
    }

    //информирование о времени задержки между действиями в ответ на inline сообщение
    private void processActionDelayInline(AnswerCallbackQuery answerCallbackQuery, long second) {
        answerCallbackQuery.setText("Жди " + second + " секунд(ы)");
        messageExecutor.sendAnswerCallbackQuery(answerCallbackQuery);
    }

    //удаление отправленного пользователем сообщения
    private void deleteUserMessage(Update update) {
        var deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(update.getMessage().getChatId().toString());
        deleteMessage.setMessageId(update.getMessage().getMessageId());
        messageExecutor.deleteMessage(deleteMessage);
    }
}
