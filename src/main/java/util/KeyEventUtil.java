package util;

import javax.annotation.Resource;
import java.awt.*;
import java.util.function.Supplier;

import static java.awt.event.KeyEvent.*;

public class KeyEventUtil {
    @Resource
    private static Supplier<Robot> robotSupplier;

    static void clickKeyEvents(String input, int sleep) {
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            clickKeyEvent(aChar);
        }
        robotSupplier.get().delay(sleep);
    }

    static void clickKeyEvent(char c) {
        if (c == '\n') {
            chickOneKey(VK_ENTER);
            return;
        }
        boolean shift = Character.isUpperCase(c);
        c = Character.toUpperCase(c);

        if (c >= 'A' && c <= 'Z') {
            chickWithShift(c - 'A' + VK_A, shift);
            return;
        }

        // shift must false
        shift = true;
        switch (c) {
            case '!':
                c = '1';
                break;
            case '@':
                c = '2';
                break;
            case '#':
                c = '3';
                break;
            case '$':
                c = '4';
                break;
            case '%':
                c = '5';
                break;
            case '^':
                c = '6';
                break;
            case '&':
                c = '7';
                break;
            case '*':
                c = '8';
                break;
            case '(':
                c = '9';
                break;
            case ')':
                c = '0';
                break;
            case '~':
                c = '`';
                break;
            case '_':
                c = '-';
                break;
            case '+':
                c = '=';
                break;
            case '{':
                c = '[';
                break;
            case '}':
                c = ']';
                break;
            case '|':
                c = '\\';
                break;
            case ':':
                c = ';';
                break;
            case '\"':
                c = '\'';
                break;
            case '<':
                c = ',';
                break;
            case '>':
                c = '.';
                break;
            case '?':
                c = '/';
                break;
            default:
                shift = false;
        }

        int keyEvent;
        if (c >= '0' && c <= '9') {
            keyEvent = (c - '0' + VK_0);
        } else {
            switch (c) {
                case '`':
                    keyEvent = VK_BACK_QUOTE;
                    break;
                case '-':
                    keyEvent = VK_MINUS;
                    break;
                case '=':
                    keyEvent = VK_EQUALS;
                    break;
                case '[':
                    keyEvent = VK_BRACELEFT;
                    break;
                case ']':
                    keyEvent = VK_BRACERIGHT;
                    break;
                case '\\':
                    keyEvent = VK_BACK_SLASH;
                    break;
                case ';':
                    keyEvent = VK_SEMICOLON;
                    break;
                case '\'':
                    keyEvent = VK_QUOTE;
                    break;
                case ',':
                    keyEvent = VK_COMMA;
                    break;
                case '.':
                    keyEvent = VK_PERIOD;
                    break;
                case '/':
                    keyEvent = VK_SLASH;
                    break;
                case ' ':
                    keyEvent = VK_SPACE;
                    break;
                default:
                    throw new RuntimeException("can input this char :" + c);
            }
        }
        chickWithShift(keyEvent, shift);
    }

    static void chickCombination(int keyEvent1, int keyEvent2) {
        Robot robot = robotSupplier.get();
        robot.keyPress(keyEvent1);
        robot.keyPress(keyEvent2);
        robot.keyRelease(keyEvent1);
        robot.keyRelease(keyEvent2);
    }

    static void chickWithShift(int keyEvent, boolean shift) {
        if (shift) {
            chickCombination(VK_SHIFT,keyEvent);
        } else {
            chickOneKey(keyEvent);
        }
    }

    static void chickOneKey(int keyEvent) {
        Robot robot = robotSupplier.get();
        robot.keyPress(keyEvent);
        robot.keyRelease(keyEvent);
    }


}
