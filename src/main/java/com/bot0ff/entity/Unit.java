package com.bot0ff.entity;

import com.bot0ff.entity.enums.Side;
import com.bot0ff.entity.enums.Status;
import com.bot0ff.entity.enums.Type;
import com.bot0ff.entity.unit.UnitArmor;
import com.bot0ff.entity.unit.UnitFightEffect;
import com.bot0ff.entity.unit.UnitFightStep;
import com.bot0ff.entity.unit.UnitSkill;
import com.bot0ff.util.converter.UnitJsonSubjectToArmorConverter;
import com.bot0ff.util.converter.UnitJsonSubjectToFightEffectConverter;
import com.bot0ff.util.converter.UnitJsonSubjectToFightStepConverter;
import com.bot0ff.util.converter.UnitJsonSubjectToSkillConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/** таблица для всех unit */
@Entity
@Table(name = "unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    //общее
    @Id
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Status status;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Type type;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Side side;

    @Column(name = "actionEnd")
    @NotNull
    private boolean actionEnd;

    /** локация */
    @Column(name = "pos_x")
    @NotNull
    private int posX;

    @Column(name = "pos_y")
    @NotNull
    private int posY;

    /** основные характеристики */
    //здоровье
    @Column(name = "hp")
    @NotNull
    private int hp;

    @Column(name = "mana")
    @NotNull
    private int mana;

    //очки действия
    @Column(name = "pointAction")
    @NotNull
    private int pointAction;

    //максимальные очки действия
    @Column(name = "maxPointAction")
    @NotNull
    private int maxPointAction;

    /** аттрибуты */
    //сила
    @Column(name = "strength")
    @NotNull
    private int strength;

    //интеллект
    @Column(name = "intelligence")
    @NotNull
    private int intelligence;

    //ловкость
    @Column(name = "dexterity")
    @NotNull
    private int dexterity;

    //выносливость
    @Column(name = "endurance")
    @NotNull
    private int endurance;

    //удача
    @Column(name = "luck")
    @NotNull
    private int luck;

    //свободные очки для распределения
    @Column(name = "bonusPoint")
    @NotNull
    private int bonusPoint;

    /** навыки */
    @Convert(converter = UnitJsonSubjectToSkillConverter.class)
    @Column(name = "unitSkill")
    @NotNull
    private UnitSkill unitSkill;

    /** список умений */
    //активные умения
    @Column(name = "currentAbility")
    @NotNull
    private List<Long> currentAbility;

    //все умения
    @Column(name = "allAbility")
    @NotNull
    private List<Long> allAbility;

    /** экипировка */
    //оружие
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "weapon", length = 1024)
    @NotNull
    private UnitArmor weapon;

    //голова
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "head", length = 1024)
    @NotNull
    private UnitArmor head;

    //руки
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "hand", length = 1024)
    @NotNull
    private UnitArmor hand;

    //тело
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "body", length = 1024)
    @NotNull
    private UnitArmor body;

    //ноги
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "leg", length = 1024)
    @NotNull
    private UnitArmor leg;

    /** сражение */
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "fight")
    private Fight fight;

    @Column(name = "teamNumber")
    private Long teamNumber;

    //позиция на линии сражения
    @Column(name = "linePosition")
    private Long linePosition;

    @Convert(converter = UnitJsonSubjectToFightStepConverter.class)
    @Column(name = "fightStep", length = 1024)
    private List<UnitFightStep> fightStep;

    @Convert(converter = UnitJsonSubjectToFightEffectConverter.class)
    @Column(name = "fightEffect", length = 1024)
    private UnitFightEffect fightEffect;

    //время завершения определенного действия
    @Column(name = "timeActivityEnd")
    private Instant timeActivityEnd;

    //id редактируемого сообщения
    @Column(name = "messageId")
    private Integer messageId;
}
