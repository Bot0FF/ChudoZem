package com.bot0ff.component.button;

import com.bot0ff.entity.Ai;
import com.bot0ff.entity.Location;
import com.bot0ff.entity.Thing;
import com.bot0ff.entity.Unit;
import com.bot0ff.util.Constants;
import com.bot0ff.util.EmojiService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
public class LocationButton {

    //кнопки перемещения и количества встречаемых объектов на локации
    public static InlineKeyboardMarkup buttonMove(Location location) {
        List<InlineKeyboardButton> buttonMoveList = new ArrayList<>();
        List<InlineKeyboardButton> buttonLocalityList = new ArrayList<>();
        List<InlineKeyboardButton> buttonUnitsList = new ArrayList<>();
        List<InlineKeyboardButton> buttonThingsList = new ArrayList<>();

        buttonMoveList.add(new InlineKeyboardButton(EmojiService.north));
        buttonMoveList.add(new InlineKeyboardButton(EmojiService.west));
        buttonMoveList.add(new InlineKeyboardButton(EmojiService.east));
        buttonMoveList.add(new InlineKeyboardButton(EmojiService.south));
        buttonMoveList.get(0).setCallbackData("/MOVE:[NORTH]");
        buttonMoveList.get(1).setCallbackData("/MOVE:[WEST]");
        buttonMoveList.get(2).setCallbackData("/MOVE:[EAST]");
        buttonMoveList.get(3).setCallbackData("/MOVE:[SOUTH]");

        //добавляем кнопку возможности перехода на локацию либо исследования
        if(location.getLocalityId().equals(0L)) {
            buttonLocalityList.add(new InlineKeyboardButton("Исследовать"));
            buttonLocalityList.get(0).setCallbackData("/EXPLORE:[" + location.getId() + "]");
        }
        else {
            buttonLocalityList.add(new InlineKeyboardButton(String.format("%s", location.getName())));
            buttonLocalityList.get(0).setCallbackData("/LOCALITY:[" + location.getLocalityId() + "]");
        }

        //список unit
        if(location.getUnits().size() > 1) {
            buttonUnitsList.add(new InlineKeyboardButton(String.format("Игроки (%s)", location.getUnits().size())));
            buttonUnitsList.get(0).setCallbackData("/UNITS:[" + location.getId() + "]");
        }

        //список ais
        if(!location.getAis().isEmpty()) {
            buttonUnitsList.add(new InlineKeyboardButton(String.format("Существа (%s)", location.getAis().size())));
            if(location.getUnits().size() > 1) {
                buttonUnitsList.get(1).setCallbackData("/AIS:[" + location.getId() + "]");
            }
            else {
                buttonUnitsList.get(0).setCallbackData("/AIS:[" + location.getId() + "]");
            }
        }

        //список things
        if(!location.getThings().isEmpty()) {
            buttonThingsList.add(new InlineKeyboardButton(String.format("Предметы (%s)", location.getThings().size())));
            buttonThingsList.get(0).setCallbackData("/THINGS:[" + location.getId() + "]");
        }

        List<List<InlineKeyboardButton>> rowsInLine = List.of(buttonLocalityList, buttonUnitsList, buttonThingsList, buttonMoveList);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    //кнопки списка ai на локации
    public static InlineKeyboardMarkup buttonAiList(List<Ai> aiList) {
        List<List<InlineKeyboardButton>> buttonAiList = new ArrayList<>();
        buttonAiList.add(new ArrayList<>());

        for(int i = 0, a = 0; i < aiList.size(); i++) {
            Ai ai = aiList.get(i);
            InlineKeyboardButton aiButton = new InlineKeyboardButton(ai.getName());
            aiButton.setCallbackData("AIS_LOC_VIEW:[" + ai.getId() + "]");
            buttonAiList.get(a).add(aiButton);
            if(buttonAiList.get(a).size() >= Constants.MAX_NUMBER_BUTTON_LINE) {
                buttonAiList.add(new ArrayList<>());
                a++;
            }
        }

        List<InlineKeyboardButton> buttonMain = new ArrayList<>();
        buttonMain.add(new InlineKeyboardButton("На главную"));
        buttonMain.get(0).setCallbackData("/LOCATION_PAGE:");
        buttonAiList.add(buttonAiList.size(), buttonMain);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(buttonAiList);

        return markupInline;
    }

    //кнопки списка unit на локации
    public static InlineKeyboardMarkup buttonUnitList(List<Unit> unitList
    ) {
        List<List<InlineKeyboardButton>> buttonAiList = new ArrayList<>();
        buttonAiList.add(new ArrayList<>());

        for(int i = 0, a = 0; i < unitList.size(); i++) {
            Unit unit = unitList.get(i);
            InlineKeyboardButton unitButton = new InlineKeyboardButton(unit.getName());
            unitButton.setCallbackData("UNIT_LOC_VIEW:[" + unit.getId() + "]");
            buttonAiList.get(a).add(unitButton);
            if(buttonAiList.get(a).size() >= Constants.MAX_NUMBER_BUTTON_LINE) {
                buttonAiList.add(new ArrayList<>());
                a++;
            }
        }

        List<InlineKeyboardButton> buttonMain = new ArrayList<>();
        buttonMain.add(new InlineKeyboardButton("На главную"));
        buttonMain.get(0).setCallbackData("/LOCATION_PAGE:");
        buttonAiList.add(buttonAiList.size(), buttonMain);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(buttonAiList);

        return markupInline;
    }

    //кнопки списка предметов на локации
    public static InlineKeyboardMarkup buttonThingList(List<Thing> thingList) {
        List<List<InlineKeyboardButton>> buttonAiList = new ArrayList<>();
        buttonAiList.add(new ArrayList<>());

        for(int i = 0, a = 0; i < thingList.size(); i++) {
            Thing thing = thingList.get(i);
            InlineKeyboardButton thingButton = new InlineKeyboardButton(thing.getName() + "(" + thing.getCondition() + ")");
            thingButton.setCallbackData("THING_LOC_VIEW:[" + thing.getId() + "]");
            buttonAiList.get(a).add(thingButton);
            if(buttonAiList.get(a).size() >= Constants.MAX_NUMBER_BUTTON_LINE) {
                buttonAiList.add(new ArrayList<>());
                a++;
            }
        }

        List<InlineKeyboardButton> buttonMain = new ArrayList<>();
        buttonMain.add(new InlineKeyboardButton("На главную"));
        buttonMain.get(0).setCallbackData("/LOCATION_PAGE:");
        buttonAiList.add(buttonAiList.size(), buttonMain);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(buttonAiList);

        return markupInline;
    }
}
