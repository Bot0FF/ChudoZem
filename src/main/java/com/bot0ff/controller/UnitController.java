package com.bot0ff.controller;

import com.bot0ff.component.MessageExecutor;
import com.bot0ff.component.button.UnitButton;
import com.bot0ff.entity.Thing;
import com.bot0ff.repository.ThingRepository;
import com.bot0ff.repository.UnitRepository;
import com.bot0ff.util.EmojiService;
import com.bot0ff.util.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Log4j
@Component
@AllArgsConstructor
public class UnitController {
    private final MessageExecutor messageExecutor;
    private final EmojiService emojiService;
    private final ExceptionMessage exceptionMessage;
    private final UnitRepository unitRepository;
    private final ThingRepository thingRepository;

    //отправляет главную страницу на локации
    @Transactional
    public void getBag(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден player в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        List<Thing> thingList = thingRepository.findByOwnerId(unit.getId());

        editMessageText.setText(emojiService.getNotification("Выбери предмет:"));
        editMessageText.setReplyMarkup(UnitButton.thingListBag(thingList));
        messageExecutor.sendEditMessage(editMessageText);
    }
}
