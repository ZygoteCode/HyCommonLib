package me.zygotecode.hycommonlib.utils;

import com.hypixel.hytale.server.core.Message;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public final class HsMessageFormatter {
    private static final Map<Character, Color> COLORS = new HashMap<>();

    static {
        COLORS.put('0', new Color(28, 28, 28));
        COLORS.put('1', new Color(0, 85, 255));
        COLORS.put('2', new Color(0, 204, 0));
        COLORS.put('3', new Color(0, 204, 204));
        COLORS.put('4', new Color(255, 0, 0));
        COLORS.put('5', new Color(204, 0, 204));
        COLORS.put('6', new Color(255, 170, 0));
        COLORS.put('7', new Color(192, 192, 192));
        COLORS.put('8', new Color(128, 128, 128));
        COLORS.put('9', new Color(85, 170, 255));
        COLORS.put('a', new Color(0, 255, 0));
        COLORS.put('b', new Color(0, 255, 255));
        COLORS.put('c', new Color(255, 85, 85));
        COLORS.put('d', new Color(255, 85, 255));
        COLORS.put('e', new Color(255, 255, 85));
        COLORS.put('f', new Color(255, 255, 255));
    }

    public static Message parse(String input) {
        return parse(input, '&');
    }

    public static Message parse(String input, char mainCharacter) {
        Message result = Message.empty();

        StringBuilder buffer = new StringBuilder();

        Color color = null;
        boolean bold = false;
        boolean italic = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == mainCharacter && i + 1 < input.length()) {
                char code = Character.toLowerCase(input.charAt(i + 1));

                if (!buffer.isEmpty()) {
                    result = result.insert(applyStyles(
                            Message.raw(buffer.toString()),
                            color, bold, italic
                    ));
                    buffer.setLength(0);
                }

                if (COLORS.containsKey(code)) {
                    color = COLORS.get(code);
                    bold = false;
                    italic = false;
                }
                else {
                    switch (code) {
                        case 'l' -> bold = true;
                        case 'o' -> italic = true;
                        case 'r' -> {
                            color = null;
                            bold = false;
                            italic = false;
                        }
                        default -> buffer.append(c).append(code);
                    }
                }

                i++;
            } else {
                buffer.append(c);
            }
        }

        if (!buffer.isEmpty()) {
            result = result.insert(applyStyles(
                    Message.raw(buffer.toString()),
                    color, bold, italic
            ));
        }

        return result;
    }

    private static Message applyStyles(Message message, Color color, boolean bold, boolean italic) {
        if (color != null) message = message.color(color);
        if (bold) message = message.bold(true);
        if (italic) message = message.italic(true);
        return message;
    }
}