package com.github.bobcat33.gamepadmousecontroller.output;

import java.awt.event.KeyEvent;

public class deleteme {
    public Integer delete(char character) {
        return switch (character) {
            case 'a' -> KeyEvent.VK_A;
            case 'b' -> KeyEvent.VK_B;
            case 'c' -> KeyEvent.VK_C;
            case 'd' -> KeyEvent.VK_D;
            case 'e' -> KeyEvent.VK_E;
            case 'f' -> KeyEvent.VK_F;
            case 'g' -> KeyEvent.VK_G;
            case 'h' -> KeyEvent.VK_H;
            case 'i' -> KeyEvent.VK_I;
            case 'j' -> KeyEvent.VK_J;
            case 'k' -> KeyEvent.VK_K;
            case 'l' -> KeyEvent.VK_L;
            case 'm' -> KeyEvent.VK_M;
            case 'n' -> KeyEvent.VK_N;
            case 'o' -> KeyEvent.VK_O;
            case 'p' -> KeyEvent.VK_P;
            case 'q' -> KeyEvent.VK_Q;
            case 'r' -> KeyEvent.VK_R;
            case 's' -> KeyEvent.VK_S;
            case 't' -> KeyEvent.VK_T;
            case 'u' -> KeyEvent.VK_U;
            case 'v' -> KeyEvent.VK_V;
            case 'w' -> KeyEvent.VK_W;
            case 'x' -> KeyEvent.VK_X;
            case 'y' -> KeyEvent.VK_Y;
            case 'z' -> KeyEvent.VK_Z;
            case '`' -> KeyEvent.VK_BACK_QUOTE;
            case '0' -> KeyEvent.VK_0;
            case '1' -> KeyEvent.VK_1;
            case '2' -> KeyEvent.VK_2;
            case '3' -> KeyEvent.VK_3;
            case '4' -> KeyEvent.VK_4;
            case '5' -> KeyEvent.VK_5;
            case '6' -> KeyEvent.VK_6;
            case '7' -> KeyEvent.VK_7;
            case '8' -> KeyEvent.VK_8;
            case '9' -> KeyEvent.VK_9;
            case '-' -> KeyEvent.VK_MINUS;
            case '=' -> KeyEvent.VK_EQUALS;
            case '!' -> KeyEvent.VK_EXCLAMATION_MARK;
            case '@' -> KeyEvent.VK_AT;
            case '#' -> KeyEvent.VK_NUMBER_SIGN;
            case '$' -> KeyEvent.VK_DOLLAR;
            case '^' -> KeyEvent.VK_CIRCUMFLEX;
            case '&' -> KeyEvent.VK_AMPERSAND;
            case '*' -> KeyEvent.VK_ASTERISK;
            case '(' -> KeyEvent.VK_LEFT_PARENTHESIS;
            case ')' -> KeyEvent.VK_RIGHT_PARENTHESIS;
            case '_' -> KeyEvent.VK_UNDERSCORE;
            case '+' -> KeyEvent.VK_PLUS;
            case '\t' -> KeyEvent.VK_TAB;
            case '\n' -> KeyEvent.VK_ENTER;
            case '[' -> KeyEvent.VK_OPEN_BRACKET;
            case ']' -> KeyEvent.VK_CLOSE_BRACKET;
            case '\\' -> KeyEvent.VK_BACK_SLASH;
            case ';' -> KeyEvent.VK_SEMICOLON;
            case ':' -> KeyEvent.VK_COLON;
            case '\'' -> KeyEvent.VK_QUOTE;
            case '"' -> KeyEvent.VK_QUOTEDBL;
            case ',' -> KeyEvent.VK_COMMA;
            case '.' -> KeyEvent.VK_PERIOD;
            case '/' -> KeyEvent.VK_SLASH;
            case ' ' -> KeyEvent.VK_SPACE;
            default -> null;
        };
    }
}
