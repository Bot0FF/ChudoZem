package com.bot0ff.util;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class ExceptionMessage {

    public String unitNotFound() {
        return "Игрок не найден \nНажми /start";
    }

    public String locationNotFound() {
        return "Локация не найдена \nНажми /start";
    }

    public String otherException() {
        return "Ошибка обработки \nНажми /start";
    }
}
