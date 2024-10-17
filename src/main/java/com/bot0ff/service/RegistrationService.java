package com.bot0ff.service;

import com.bot0ff.entity.Unit;
import com.bot0ff.entity.enums.Side;
import com.bot0ff.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.bot0ff.entity.Fight;
import com.bot0ff.entity.enums.Type;
import com.bot0ff.entity.unit.UnitArmor;
import com.bot0ff.entity.unit.UnitFightEffect;
import com.bot0ff.entity.unit.UnitSkill;
import com.bot0ff.repository.UnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {
    private final UnitRepository unitRepository;

    //регистрация user с начальными параметрами и проверка имени
    @Transactional
    public Unit registrationUnitAndCheckName(Update update) {
        Unit temp = unitRepository.findByName(update.getMessage().getFrom().getUserName()).orElse(null);
        String unitName = "TEMP_NAME";
        Status status = Status.REGISTRATION_NAME;
        if(temp == null) {
            unitName = update.getMessage().getFrom().getUserName();
            status = Status.REGISTRATION_SIDE;
        }

        Unit newUnit = registrationUnitToDb(update.getMessage().getFrom().getId(), unitName, status);

        log.info(String.format("New User: %s", newUnit));
        return newUnit;
    }

    //проверка дубликата имени при регистрации
    @Transactional
    public boolean checkNameForRegistrationRepeat(Update update) {
        Unit temp = unitRepository.findByName(update.getMessage().getText()).orElse(null);
        if(temp == null) {
            Unit unit = unitRepository.findById(update.getMessage().getFrom().getId()).orElse(null);
            if(unit != null) {
                unit.setName(update.getMessage().getText());
                unit.setStatus(Status.REGISTRATION_SIDE);
                unitRepository.save(unit);
                return true;
            }
            else return false;
        }
        else return false;
    }

    //регистрирует нового unit с начальными параметрами
    public Unit registrationUnitToDb(Long unitId, String name, Status status) {
        Unit unit;

        unit = new Unit();
        unit.setId(unitId);
        unit.setName(name);
        unit.setSide(Side.LIGHT);
        unit.setActionEnd(false);
        unit.setPosX(3);
        unit.setPosY(3);
        unit.setHp(20);
        unit.setMana(20);
        unit.setPointAction(3);
        unit.setMaxPointAction(3);

        unit.setType(Type.USER);

        unit.setStrength(1);
        unit.setIntelligence(1);
        unit.setDexterity(1);
        unit.setEndurance(1);
        unit.setLuck(1);
        unit.setBonusPoint(3);
        unit.setUnitSkill(new UnitSkill());

        List<Long> startAbility = new ArrayList<>();
        startAbility.add(1L);
        unit.setCurrentAbility(startAbility);
        unit.setAllAbility(startAbility);
        unit.setWeapon(new UnitArmor());
        unit.setHead(new UnitArmor());
        unit.setBody(new UnitArmor());
        unit.setHand(new UnitArmor());
        unit.setLeg(new UnitArmor());

        unit.setFight(new Fight());
        unit.setFightStep(List.of());
        unit.setFightEffect(new UnitFightEffect());
        unit.setStatus(status);
        unit.setTimeActivityEnd(Instant.now());
        unitRepository.save(unit);

        return unit;
    }

    //сохраняет id сообщения для редактирования
    public void saveUnitMessageId(Unit unit, int messageId) {
        unit.setMessageId(messageId);
        unitRepository.save(unit);
    }

    //сохраняет выбранную сторону unit
    public void saveUnitSide(Unit unit, String side) {
        switch (side) {
            case "LIGHT" -> unit.setSide(Side.LIGHT);
            case "DARK" -> unit.setSide(Side.DARK);
        }
        unit.setStatus(Status.ACTIVE);
        unitRepository.save(unit);
    }

    //проверяем свободно ли имя при попытке смены имени user
//    public boolean attemptChangeName(String textMessage, Unit unit) {
//        String newName = textMessage.substring(9);
//        Unit temp = unitRepo.findByUserName(newName).orElse(null);
//        if(temp == null && Objects.requireNonNull(unit).getUserGoldMoney() >= 10) {
//            unit.setUserGoldMoney(unit.getUserGoldMoney() - 10);
//            String oldName = unit.getUserName();
//            unit.setUserName(newName);
//            unitRepo.save(unit);
//            log.info(String.format("ChangeName: Old: %s New: %s | %d", oldName, newName, unit.getId()));
//            return true;
//        } else return false;
//    }

//    private void messageForAdmin(TelegramBot telegramBot, String message) {
//        SendMessage forAdmin = new SendMessage();
//        forAdmin.setChatId(String.valueOf(775968915));
//        forAdmin.setText(message);
////        setView(telegramBot, forAdmin, null);
//    }

//    //возвращает карту локации на которой user
//    public String currentLocation(User user) {
//        return "\n----------------------------\n" +
//                emojiService.getEmojiMap(user) + "\n" +
//                emojiService.getEmMove() +
//                "Ты на локации: \n" +
//                emojiService.getEmMove() + user.getPosX() + "|" + user.getPosY() + " - " + ConstructMap.getUserLocation(user).getNameLoc() + "\n" +
//                String.format("%sЗдоровье: %d \n%sМана: %d", emojiService.getEmHp(), user.getUserHp(), emojiService.getEmMana(), user.getUserMana());
//    }
//
//    //отправляем ответ user
//    private void setView(TelegramBot telegramBot, SendMessage sendMessage, User user) {
//        telegramBot.sendMessage(sendMessage, user);
//    }
}
