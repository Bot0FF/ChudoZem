package com.bot0ff.configuration;

import com.bot0ff.component.button.TextButton;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Data
@PropertySource("application.yml")
@Configuration
public class BotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String botToken;

    @Bean
    public TelegramBot telegramBot(ApplicationEventPublisher eventPublisher, BotProperties botProperties, List<BotCommand> commands) throws TelegramApiException {
        TelegramBot telegramBot = new TelegramBot(eventPublisher, botProperties.getName(), botProperties.getToken());

        new TelegramBotsApi(DefaultBotSession.class).registerBot(telegramBot);
        telegramBot.execute(new SetMyCommands(TextButton.commandMarkup(), new BotCommandScopeDefault(), null));

        return telegramBot;
    }
}
