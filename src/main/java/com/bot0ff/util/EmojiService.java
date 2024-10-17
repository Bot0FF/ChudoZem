package com.bot0ff.util;

import com.bot0ff.entity.Location;
import com.bot0ff.entity.Unit;
import com.vdurmont.emoji.EmojiParser;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

//сервис парсинга emoji
@Data
@Service
public class EmojiService {
    public static String north = EmojiParser.parseToUnicode(":arrow_up_small:");
    public static String west = EmojiParser.parseToUnicode(":arrow_backward:");
    public static String east = EmojiParser.parseToUnicode(":arrow_forward:");
    public static String south = EmojiParser.parseToUnicode(":arrow_down_small:");
    private String emHp = EmojiParser.parseToUnicode(":heart:");
    private String emDamage = EmojiParser.parseToUnicode(":dagger_knife:");
    private String emDefense = EmojiParser.parseToUnicode(":shield:");
    private String emMoney = EmojiParser.parseToUnicode(":moneybag:");
    private String emToNextLevel = EmojiParser.parseToUnicode(":military_medal:");
    private String emLevel = EmojiParser.parseToUnicode(":trophy:");
    private String emBrakeThing = EmojiParser.parseToUnicode(":hammer_and_wrench:");
    private String emTakeOff = EmojiParser.parseToUnicode(":horse:");
    private String emThrowAway = EmojiParser.parseToUnicode(":wastebasket:");
    private String emPutOnError = EmojiParser.parseToUnicode(":bust_in_silhouette:");
    private String emPutOn = EmojiParser.parseToUnicode(":man_farmer:");
    private String emLongMessage = EmojiParser.parseToUnicode(":scissors:");
    private String emChat = EmojiParser.parseToUnicode(":memo:");
    private String emChance = EmojiParser.parseToUnicode(":game_die:");
    private String emCharacteristic = EmojiParser.parseToUnicode(":man_farmer:");
    private String emBug = EmojiParser.parseToUnicode(":briefcase:");
    private String emTrash = EmojiParser.parseToUnicode(":nut_and_bolt:");
    private String emRegistration = EmojiParser.parseToUnicode(":scroll:");
    private String emReturn = EmojiParser.parseToUnicode(":paw_prints:");
    private String emMove = EmojiParser.parseToUnicode(":paw_prints:");
    private String emBattle = EmojiParser.parseToUnicode(":crossed_swords:");
    private String emLevelUp = EmojiParser.parseToUnicode(":sports_medal:");
    private String emExp = EmojiParser.parseToUnicode(":dart:");
    private String emNothing = EmojiParser.parseToUnicode(":mag:");
    private String emCondition = EmojiParser.parseToUnicode(":gear:");
    private String emShop = EmojiParser.parseToUnicode(":dollar:");
    private String emPharmacy = EmojiParser.parseToUnicode(":man_scientist:");
    private String emGoldMoney = EmojiParser.parseToUnicode(":credit_card:");
    private String emEquip = EmojiParser.parseToUnicode(":tophat:");
    private String emEffect = EmojiParser.parseToUnicode(":sparkles:");
    private String emDescription = EmojiParser.parseToUnicode(":page_with_curl:");
    private String emCrit = EmojiParser.parseToUnicode(":rocket:");
    private String emAttention = EmojiParser.parseToUnicode(":exclamation:");
    private String emQuestion = EmojiParser.parseToUnicode(":question:");
    private String emWait = EmojiParser.parseToUnicode(":hourglass:");
    private String emMana = EmojiParser.parseToUnicode(":blue_heart:");
    private String emPotion = EmojiParser.parseToUnicode(":pill:");
    private String emCrystal = EmojiParser.parseToUnicode(":chrystal_ball:");

    private String emojiParser(String emojiName) {
        return EmojiParser.parseToUnicode(emojiName);
    }

    //возвращает карту в виде смайлов 5 на 5
    public StringBuilder getEmojiMap(Unit unit) {
        StringBuilder sb = new StringBuilder();
        for(int y = unit.getPosY() - 2; y <= unit.getPosY() + 2; y++) {
            for(int x = unit.getPosX() - 2; x <= unit.getPosX() + 2; x++) {
                if(y == unit.getPosY() && x == unit.getPosX()) {
                    sb.append(" ").append(emojiParser(":man_farmer:"));
                    continue;
                }
                if((y < 0 | y > MapImageService.allLocations.length - 1) | (x < 0 | x > MapImageService.allLocations.length - 1)) {
                    sb.append(" ").append(emojiParser(":spider_web:"));
                    continue;
                }

                sb.append(" ").append(emojiParser(MapImageService.allLocations[y][x]));
            }
            sb.append("\n");
        }
        return sb;
    }

    public String getUnitStateOnLocation(String notification, Unit unit, Location location) {
        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH-mm-ss:SSS");
        String formattedDate = zonedDateTime.format(formatter);

        return formattedDate +
                "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                notification +
                "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                getEmojiMap(unit) + "\n" + getEmMove() +
                "Ты на локации: \n" +
                getEmMove() + unit.getPosX() + "|" + unit.getPosY() + " - " + location.getName() + "\n" +
                String.format("%sЗдоровье: %d \n%sМана: %d", getEmHp(), unit.getHp(), getEmMana(), unit.getMana());
    }

    public String getNotification(String notification) {
        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH-mm-ss:SSS");
        String formattedDate = zonedDateTime.format(formatter);

        return formattedDate +
                "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - -\n" +
                notification +
                "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
    }
}
