package com.bot0ff.controller;

import com.bot0ff.component.MessageExecutor;
import com.bot0ff.component.button.LocationButton;
import com.bot0ff.entity.*;
import com.bot0ff.entity.enums.LocationType;
import com.bot0ff.repository.AiRepository;
import com.bot0ff.repository.LocationRepository;
import com.bot0ff.repository.ThingRepository;
import com.bot0ff.repository.UnitRepository;
import com.bot0ff.service.LocationService;
import com.bot0ff.util.EmojiService;
import com.bot0ff.util.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Log4j
@Component
@AllArgsConstructor
public class LocationController {
    private final MessageExecutor messageExecutor;
    private final UnitRepository unitRepository;
    private final LocationRepository locationRepository;
    private final AiRepository aiRepository;
    private final ThingRepository thingRepository;

    private final LocationService locationService;
    private final EmojiService emojiService;
    private final ExceptionMessage exceptionMessage;

    //отправляет главную страницу на локации
    @Transactional
    public void getLocationPage(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        editMessageText.setText(emojiService.getUnitStateOnLocation("", unit, location));
        editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //меняет в базе координаты местоположения user
    //и обновляет состояние локации на которую перешел user
    @Transactional
    public void moveUnit(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var direction = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу userId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        Location newLocation;
        switch (direction) {
            case "NORTH" -> {
                unit.setPosY(unit.getPosY() - 1);
                newLocation = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
                if(newLocation == null) {
                    log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
                    unit.setPosY(unit.getPosY() + 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Туда нельзя перейти", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
                if(newLocation.getLocationType().equals(LocationType.RIVER)) {
                    unit.setPosY(unit.getPosY() + 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Впереди река", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
            }
            case "SOUTH" -> {
                unit.setPosY(unit.getPosY() + 1);
                newLocation = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
                if(newLocation == null) {
                    log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
                    unit.setPosY(unit.getPosY() - 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Туда нельзя перейти", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
                if(newLocation.getLocationType().equals(LocationType.RIVER)) {
                    unit.setPosY(unit.getPosY() - 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Впереди река", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
            }
            case "EAST" -> {
                unit.setPosX(unit.getPosX() + 1);
                newLocation = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
                if(newLocation == null) {
                    log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
                    unit.setPosX(unit.getPosX() - 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Туда нельзя перейти", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
                if(newLocation.getLocationType().equals(LocationType.RIVER)) {
                    unit.setPosX(unit.getPosX() - 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Впереди река", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
            }
            case "WEST" -> {
                unit.setPosX(unit.getPosX() - 1);
                newLocation = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
                if(newLocation == null) {
                    log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
                    unit.setPosX(unit.getPosX() + 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Туда нельзя перейти", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
                if(newLocation.getLocationType().equals(LocationType.RIVER)) {
                    unit.setPosX(unit.getPosX() + 1);
                    editMessageText.setText(emojiService.getUnitStateOnLocation("Впереди река", unit, location));
                    editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
                    messageExecutor.sendEditMessage(editMessageText);
                    return;
                }
            }
            default -> {
                log.info("Не найдена location в БД. Текущее положение unit: " + unit.getPosX() + "/" + unit.getPosY() + ". Движение - " + direction);
                editMessageText.setText(exceptionMessage.locationNotFound());
                messageExecutor.sendEditMessage(editMessageText);
                return;
            }
        }

        //удаляет unit с листа units с текущей локации
        locationService.removeUnitFromLocation(unit.getId(), location);
        //добавляет unit в лист units на новую локацию
        locationService.setUnitOnLocation(unit.getId(), newLocation);

        //загружает обновленное состояние unit и location после перехода
        var updateUnit = unitRepository.findById(unitId).orElse(null);
        if(updateUnit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var updateLocation = locationRepository.findLocation(unit.getPosX(), unit.getPosY()).orElse(null);
        if(updateLocation == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        //TODO добавить время перехода между локациями, в зависимости от навыка
        editMessageText.setText(emojiService.getUnitStateOnLocation("Ты перешел на локацию: \n" + updateLocation.getName(), unit, updateLocation));
        editMessageText.setReplyMarkup(LocationButton.buttonMove(newLocation));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //отправляет лист ais на локации
    public void showAisList(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var locationId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findById(Long.valueOf(locationId)).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        List<Ai> aiList = aiRepository.findAllById(location.getAis());

        editMessageText.setText(emojiService.getNotification("Выбери существо:"));
        editMessageText.setReplyMarkup(LocationButton.buttonAiList(aiList));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //отправляет лист units на локации
    public void showUnitsList(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var locationId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findById(Long.valueOf(locationId)).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        List<Unit> unitList = unitRepository.findAllById(location.getUnits());

        editMessageText.setText(emojiService.getNotification("Выбери героя:"));
        editMessageText.setReplyMarkup(LocationButton.buttonUnitList(unitList));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //отправляет лист things на локации
    public void showThingsList(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var locationId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findById(Long.valueOf(locationId)).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        List<Thing> thingList = thingRepository.findAllById(location.getThings());

        editMessageText.setText(emojiService.getNotification("Выбери предмет:"));
        editMessageText.setReplyMarkup(LocationButton.buttonThingList(thingList));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //перенаправляет в другую местность по нажатию locality
    public void applyRelocate(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var localityId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findById(Long.valueOf(localityId)).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        editMessageText.setText(emojiService.getNotification("Ты перешел в местность: \n" + location.getName()));
        editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
        messageExecutor.sendEditMessage(editMessageText);
    }

    //перенаправляет в другую местность по нажатию locality
    public void processLocationExplorer(EditMessageText editMessageText, Long unitId, String requestMessage) {
        var locationId = StringUtils.substringBetween(requestMessage, "[", "]");
        var unit = unitRepository.findById(unitId).orElse(null);
        if(unit == null) {
            log.error("Не найден unit в БД по запросу unitId: " + unitId);
            editMessageText.setText(exceptionMessage.unitNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }
        var location = locationRepository.findById(Long.valueOf(locationId)).orElse(null);
        if(location == null) {
            log.info("Не найдена location в БД по запросу locationId: " + unit.getPosX() + "/" + unit.getPosY());
            editMessageText.setText(exceptionMessage.locationNotFound());
            messageExecutor.sendEditMessage(editMessageText);
            return;
        }

        if(!locationService.setProcessLocationExplorer(messageExecutor, editMessageText, unit, location)) {
            editMessageText.setText(emojiService.getUnitStateOnLocation("Не удалось исследовать: \n" + location.getName(), unit, location));
        }
        else {
            editMessageText.setText(emojiService.getUnitStateOnLocation("Началось исследование: \n" + location.getName(), unit, location));
        }
        editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
        messageExecutor.sendEditMessage(editMessageText);
    }
}
