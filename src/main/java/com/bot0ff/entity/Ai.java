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
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** таблица с aiUnit, из которой берутся сущности для общей таблицы unit */
@Entity
@Table(name = "ai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ai {

    //общее
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Status status;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Type Type;

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
    private int hp;

    @Column(name = "mana")
    private int mana;

    //очки действия
    @Column(name = "pointAction")
    private int pointAction;

    //максимальные очки действия
    @Column(name = "maxPointAction")
    private int maxPointAction;

    /** аттрибуты */
    //сила
    @Column(name = "strength")
    private int strength;

    //интеллект
    @Column(name = "intelligence")
    private int intelligence;

    //ловкость
    @Column(name = "dexterity")
    private int dexterity;

    //выносливость
    @Column(name = "endurance")
    private int endurance;

    //удача
    @Column(name = "luck")
    private int luck;

    /** навыки */
    @Convert(converter = UnitJsonSubjectToSkillConverter.class)
    @Column(name = "unitSkill")
    private UnitSkill unitSkill;

    /** список умений */
    //активные умения
    @Column(name = "currentAbility")
    @JsonIgnore
    private List<Long> currentAbility;

    //все умения
    @Column(name = "allAbility")
    @JsonIgnore
    private List<Long> allAbility;

    /** экипировка */
    //оружие
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "weapon", length = 1024)
    @JsonIgnore
    private UnitArmor weapon;

    //голова
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "head", length = 1024)
    @JsonIgnore
    private UnitArmor head;

    //руки
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "hand", length = 1024)
    @JsonIgnore
    private UnitArmor hand;

    //тело
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "body", length = 1024)
    @JsonIgnore
    private UnitArmor body;

    //ноги
    @Convert(converter = UnitJsonSubjectToArmorConverter.class)
    @Column(name = "leg", length = 1024)
    @JsonIgnore
    private UnitArmor leg;

    /** сражение */
    @JsonIgnore
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "fight")
    private Fight fight;

    @Column(name = "teamNumber")
    private Long teamNumber;

    //позиция на линии сражения
    @Column(name = "linePosition")
    private Long linePosition;

    @JsonIgnore
    @Convert(converter = UnitJsonSubjectToFightStepConverter.class)
    @Column(name = "fightStep", length = 1024)
    private List<UnitFightStep> fightStep;

    @Convert(converter = UnitJsonSubjectToFightEffectConverter.class)
    @Column(name = "fightEffect", length = 1024)
    @JsonIgnore
    private UnitFightEffect fightEffect;
}