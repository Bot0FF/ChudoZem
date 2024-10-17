package com.bot0ff.component.button;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class RegistrationSideButton {
    //кнопки выбора стороны при регистрации
    public static InlineKeyboardMarkup registrationSideButton() {
        List<InlineKeyboardButton> registrationButton = new ArrayList<>();

        registrationButton.add(new InlineKeyboardButton("Свет"));
        registrationButton.add(new InlineKeyboardButton("Тьма"));
        registrationButton.get(0).setCallbackData("LIGHT");
        registrationButton.get(1).setCallbackData("DARK");

        List<List<InlineKeyboardButton>> rowsInLine = List.of(registrationButton);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
