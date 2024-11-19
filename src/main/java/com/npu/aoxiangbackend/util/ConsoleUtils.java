package com.npu.aoxiangbackend.util;

import java.awt.*;
import java.io.PrintStream;
import java.util.HashMap;

public final class ConsoleUtils {
    private ConsoleUtils() {
    }

    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright Colors
    public static final String BLACK_BRIGHT = "\u001B[90m";
    public static final String RED_BRIGHT = "\u001B[91m";
    public static final String GREEN_BRIGHT = "\u001B[92m";
    public static final String YELLOW_BRIGHT = "\u001B[93m";
    public static final String BLUE_BRIGHT = "\u001B[94m";
    public static final String PURPLE_BRIGHT = "\u001B[95m";
    public static final String CYAN_BRIGHT = "\u001B[96m";
    public static final String WHITE_BRIGHT = "\u001B[97m";

    // Background Colors
    public static final String BLACK_BG = "\u001B[40m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String PURPLE_BG = "\u001B[45m";
    public static final String CYAN_BG = "\u001B[46m";
    public static final String WHITE_BG = "\u001B[47m";

    // Bright Background Colors
    public static final String BLACK_BRIGHT_BG = "\u001B[100m";
    public static final String RED_BRIGHT_BG = "\u001B[101m";
    public static final String GREEN_BRIGHT_BG = "\u001B[102m";
    public static final String YELLOW_BRIGHT_BG = "\u001B[103m";
    public static final String BLUE_BRIGHT_BG = "\u001B[104m";
    public static final String PURPLE_BRIGHT_BG = "\u001B[105m";
    public static final String CYAN_BRIGHT_BG = "\u001B[106m";
    public static final String WHITE_BRIGHT_BG = "\u001B[107m";
}
