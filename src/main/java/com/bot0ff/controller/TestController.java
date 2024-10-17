package com.bot0ff.controller;

import com.bot0ff.component.MessageExecutor;
import com.bot0ff.component.button.LocationButton;
import com.bot0ff.repository.LocationRepository;
import com.bot0ff.repository.UnitRepository;
import com.bot0ff.service.generator.EntityGenerator;
import com.bot0ff.util.EmojiService;
import com.bot0ff.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Log4j
@Component
@RequiredArgsConstructor
public class TestController {
    private final MessageExecutor messageExecutor;
    private final EmojiService emojiService;
    private final ExceptionMessage exceptionMessage;
    private final LocationRepository locationRepository;
    private final UnitRepository unitRepository;
    private final EntityGenerator entityGenerator;

    //добавляет предмет на локацию
    public void setThingOnLocation(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var formId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден player в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        var newThing = entityGenerator.setNewThingToLocation(Long.valueOf(formId), location);

        editMessageText.setText(emojiService.getUnitStateOnLocation("На локацию добавлено: \n" + newThing.getName(), unit, location));
        editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
        messageExecutor.sendEditMessage(editMessageText);
    }
}
