package com.bot0ff.service.generator;

import com.bot0ff.entity.Form;
import com.bot0ff.entity.Location;
import com.bot0ff.entity.Thing;
import com.bot0ff.entity.Unit;
import com.bot0ff.entity.enums.Side;
import com.bot0ff.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Log4j
@Service
@RequiredArgsConstructor
public class EntityGenerator {
    private final FormRepository formRepository;
    private final UnitRepository unitRepository;
    private final LocationRepository locationRepository;
    private final ThingRepository thingRepository;
    private final AiRepository aiRepository;

    //генерация и сохранение нового ai на локации
    public Unit getNewAiUnitId(Location location) {
        var aiUnit = aiRepository.findById(1L).orElse(null);
        if(aiUnit == null) {
            log.error("Не найден ai в бд");
            return null;
        }

        Unit newAiUnit = new Unit(
                null,
                aiUnit.getName(),
                aiUnit.getStatus(),
                aiUnit.getType(),
                Side.DARK,
                aiUnit.isActionEnd(),
                aiUnit.getPosX(),
                aiUnit.getPosY(),
                aiUnit.getHp(),
                aiUnit.getMana(),
                aiUnit.getPointAction(),
                aiUnit.getMaxPointAction(),
                aiUnit.getStrength(),
                aiUnit.getIntelligence(),
                aiUnit.getDexterity(),
                aiUnit.getEndurance(),
                aiUnit.getLuck(),
                0,
                aiUnit.getUnitSkill(),
                aiUnit.getCurrentAbility(),
                aiUnit.getAllAbility(),
                aiUnit.getWeapon(),
                aiUnit.getHead(),
                aiUnit.getHand(),
                aiUnit.getBody(),
                aiUnit.getLeg(),
                aiUnit.getFight(),
                aiUnit.getTeamNumber(),
                aiUnit.getLinePosition(),
                aiUnit.getFightStep(),
                aiUnit.getFightEffect(),
                Instant.now(),
                0
        );

        location.getAis().add(aiUnit.getId());
        unitRepository.save(newAiUnit);
        locationRepository.save(location);
        return newAiUnit;
    }

    //генерация и сохранение нового предмета на локацию
    @Transactional
    public Thing setNewThingToLocation(Long formId, Location location) {
        var form = formRepository.findById(formId).orElse(null);
        if(form == null) {
            log.error("Не найдена form в бд");
            return null;
        }

        Thing thing = new Thing(
                null,
                form.getName(),
                null,
                false,
                form.getThingType(),
                form.getSkillType(),
                form.getPhysDamage(),
                form.getMagModifier(),
                form.getHp(),
                form.getMana(),
                form.getPhysDefense(),
                form.getMagDefense(),
                form.getStrength(),
                form.getIntelligence(),
                form.getDexterity(),
                form.getEndurance(),
                form.getLuck(),
                form.getDistance(),
                form.getCondition(),
                //для книг
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                form.getPrice(),
                form.getDescription()
        );

        Thing newThing = thingRepository.save(thing);
        location.getThings().add(newThing.getId());
        locationRepository.save(location);

        return newThing;
    }

    //генерация и сохранение нового предмета в инвентарь
    public Thing setNewThingToInventory(Long playerId, Form form) {
        Thing newThing = new Thing(
                null,
                form.getName(),
                playerId,
                false,
                form.getThingType(),
                form.getSkillType(),
                form.getPhysDamage(),
                form.getMagModifier(),
                form.getHp(),
                form.getMana(),
                form.getPhysDefense(),
                form.getMagDefense(),
                form.getStrength(),
                form.getIntelligence(),
                form.getDexterity(),
                form.getEndurance(),
                form.getLuck(),
                form.getDistance(),
                form.getCondition(),
                //для книг
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                form.getPrice(),
                form.getDescription()
        );
        return thingRepository.save(newThing);
    }
}