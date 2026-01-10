package com.bytespacegames.chattingenthusiast.utils;

public class CharacterUtils {
    public static int scancodeToCodepoint(int scancode, boolean shift) {
        return switch (scancode) {
            // Numbers row
            case 2 -> shift ? '!' : '1';
            case 3 -> shift ? '@' : '2';
            case 4 -> shift ? '#' : '3';
            case 5 -> shift ? '$' : '4';
            case 6 -> shift ? '%' : '5';
            case 7 -> shift ? '^' : '6';
            case 8 -> shift ? '&' : '7';
            case 9 -> shift ? '*' : '8';
            case 10 -> shift ? '(' : '9';
            case 11 -> shift ? ')' : '0';
            case 12 -> shift ? '_' : '-';
            case 13 -> shift ? '+' : '=';

            // Top row letters
            case 16 -> shift ? 'Q' : 'q';
            case 17 -> shift ? 'W' : 'w';
            case 18 -> shift ? 'E' : 'e';
            case 19 -> shift ? 'R' : 'r';
            case 20 -> shift ? 'T' : 't';
            case 21 -> shift ? 'Y' : 'y';
            case 22 -> shift ? 'U' : 'u';
            case 23 -> shift ? 'I' : 'i';
            case 24 -> shift ? 'O' : 'o';
            case 25 -> shift ? 'P' : 'p';
            case 26 -> shift ? '{' : '[';
            case 27 -> shift ? '}' : ']';
            case 43 -> shift ? '|' : '\\';

            // Home row letters
            case 30 -> shift ? 'A' : 'a';
            case 31 -> shift ? 'S' : 's';
            case 32 -> shift ? 'D' : 'd';
            case 33 -> shift ? 'F' : 'f';
            case 34 -> shift ? 'G' : 'g';
            case 35 -> shift ? 'H' : 'h';
            case 36 -> shift ? 'J' : 'j';
            case 37 -> shift ? 'K' : 'k';
            case 38 -> shift ? 'L' : 'l';
            case 39 -> shift ? ':' : ';';
            case 40 -> shift ? '"' : '\'';

            // Bottom row letters
            case 44 -> shift ? 'Z' : 'z';
            case 45 -> shift ? 'X' : 'x';
            case 46 -> shift ? 'C' : 'c';
            case 47 -> shift ? 'V' : 'v';
            case 48 -> shift ? 'B' : 'b';
            case 49 -> shift ? 'N' : 'n';
            case 50 -> shift ? 'M' : 'm';
            case 51 -> shift ? '<' : ',';
            case 52 -> shift ? '>' : '.';
            case 53 -> shift ? '?' : '/';

            // Space, Enter, Tab
            case 57 -> ' ';
            case 28 -> '\n';
            case 15 -> '\t';

            default -> 0; // unknown key
        };
    }
}
