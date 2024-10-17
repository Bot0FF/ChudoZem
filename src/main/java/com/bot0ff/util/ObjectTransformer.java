package com.bot0ff.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ObjectTransformer {
    private final ObjectMapper objectMapper;

    public Object parseMessage(String msg, Class<?> type) throws IOException {
        try(JsonParser parser = objectMapper.createParser(msg)) {
            return parser.readValuesAs(type);
        }
    }
}
