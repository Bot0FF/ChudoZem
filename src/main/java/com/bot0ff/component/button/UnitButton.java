package com.bot0ff.component.button;

import com.bot0ff.entity.Thing;
import com.bot0ff.util.Constants;
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
public class UnitButton {

    //кнопки списка предметов unit из рюкзака
    public static InlineKeyboardMarkup thingListBag(List<Thing> aiList) {
        List<List<InlineKeyboardButton>> buttonAiList = new ArrayList<>();
        buttonAiList.add(new ArrayList<>());

        for(int i = 0, a = 0; i < aiList.size(); i++) {
            Thing thing = aiList.get(i);
            InlineKeyboardButton aiButton = new InlineKeyboardButton(thing.getName() + "(" + thing.getCondition() + ")");
            aiButton.setCallbackData("BAG:[" + thing.getId() + "]");
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
}
