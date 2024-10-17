package com.bot0ff.service;

import com.bot0ff.component.MessageExecutor;
import com.bot0ff.component.button.LocationButton;
import com.bot0ff.entity.Location;
import com.bot0ff.entity.Unit;
import com.bot0ff.repository.LocationRepository;
import com.bot0ff.repository.UnitRepository;
import com.bot0ff.util.EmojiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
@RequiredArgsConstructor
public class LocationService {
    private final UnitRepository unitRepository;
    private final LocationRepository locationRepository;
    private final EmojiService emojiService;

    //удаляет unit с локации
    public void removeUnitFromLocation(Long unitId, Location location) {
        location.getUnits().removeIf(currentUnitId -> currentUnitId.equals(unitId));
        locationRepository.save(location);
    }

    //добавляет unit на локацию
    public void setUnitOnLocation(Long unitId, Location location) {
        location.getUnits().removeIf(currentUnitId -> currentUnitId.equals(unitId));
        location.getUnits().add(unitId);
        locationRepository.save(location);
    }

    //запускает процесс исследования локации
    public boolean setProcessLocationExplorer(MessageExecutor messageExecutor, EditMessageText editMessageText, Unit unit, Location location) {
        unit.setTimeActivityEnd(Instant.now().plus(3L, ChronoUnit.SECONDS));
        unitRepository.save(unit);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var updateUnit = unitRepository.findById(unit.getId()).orElse(null);
            if(updateUnit == null) {
                log.error("Не найден player в БД по запросу unitId: " + unit.getId());
                return;
            }
            var updateLocation = locationRepository.findById(location.getId()).orElse(null);
            if(updateLocation == null) {
                log.info("Не найдена location в БД по запросу locationId: " + location.getId());
                return;
            }

            //TODO добавить ресурсы unit и уменьшить на локации
            editMessageText.setText(emojiService.getUnitStateOnLocation("Ты добыл: \n", unit, location));
            editMessageText.setReplyMarkup(LocationButton.buttonMove(location));
            messageExecutor.sendEditMessage(editMessageText);
        });

        return true;
    }
}
