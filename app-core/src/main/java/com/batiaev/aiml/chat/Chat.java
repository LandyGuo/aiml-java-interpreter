package com.batiaev.aiml.chat;

import com.batiaev.aiml.bot.Bot;
import com.batiaev.aiml.consts.AimlConst;
import com.batiaev.aiml.providers.Provider;

/**
 * Created by anbat on 6/18/15.
 *
 * @author anbat
 */
public class Chat {
    private final static String DEFAULT_NICKNAME = "Human";
    private final Bot bot;
    private final Provider provider;
    private String nickname = DEFAULT_NICKNAME;
    private ChatState state;

    public Chat(Bot bot, Provider provider) {
        this.bot = bot;
        this.provider = provider;
    }

    public void start() {
        provider.write("Hello! Welcome to chat with " + bot.getName() + ".\n");
        state = new ChatState(nickname);

        String message;
        while (true) {
            message = read();
            if (message.startsWith("/")) {
                parseCommand(message);
            } else {
                String response = bot.multisentenceRespond(message, state);
                state.newState(message, response);
                write(response);
            }
        }
    }

    private void parseCommand(final String command) {
        switch (command) {
            case ChatCommand.exit:
            case ChatCommand.quit:
                System.exit(0);
            case ChatCommand.stat:
                write(bot.getBrainStats());
                break;
            case ChatCommand.reload:
                bot.wakeUp();
                break;
            case "/connect russian":
            case "/c russian":
                bot.setName("russian");
                bot.wakeUp();
                break;
            case "/connect alice2":
            case "/c alice2":
                bot.setName("alice2");
                bot.wakeUp();
                break;
            case "/debug on":
            case "/debug true":
                AimlConst.debug = true;
                break;
            case "/debug off":
            case "/debug false":
                AimlConst.debug = false;
                break;
            default:
                String response = bot.multisentenceRespond(command, state);
                state.newState(command, response);
                write(response);
                break;
        }
    }

    private String read() {
        provider.write(nickname + ": ");
        String textLine = provider.read();
        return textLine == null || textLine.isEmpty() ? AimlConst.null_input : textLine.trim();
    }

    private void write(String message) {
        provider.write(bot.getName() + ": " + message + "\n");
    }
}
