package com.npu.aoxiangbackend.util;

import java.io.PrintStream;
import java.util.Arrays;

import static com.npu.aoxiangbackend.util.ConsoleUtils.*;

/**
 * 扩展了 {@link PrintStream} 类以支持彩色文本输出。
 * <p>
 * 此类提供了一种方式来启用或禁用颜色代码，并允许在打印消息时自动添加前缀，
 * 同时可以控制是否使用颜色输出。
 *
 * @author 贾聪毅
 * @version 1.0
 */
public class ColoredPrintStream extends PrintStream {

    /**
     * 控制是否启用颜色代码的标志。
     * 默认情况下，颜色代码是启用的。
     */
    private boolean enableColorCodes = true;

    /**
     * 构造一个新的 {@code ColoredPrintStream} 实例，该实例基于给定的基础 {@code PrintStream}。
     *
     * @param basePrinter 基础的 {@code PrintStream}，用于实际的输出操作。
     */
    public ColoredPrintStream(PrintStream basePrinter) {
        super(basePrinter);
    }

    /**
     * 获取当前是否启用了颜色代码。
     *
     * @return 如果颜色代码已启用，则返回 {@code true}；否则返回 {@code false}。
     */
    public boolean isEnableColorCodes() {
        return enableColorCodes;
    }

    /**
     * 设置是否启用颜色代码。
     *
     * @param enableColorCodes 如果应启用颜色代码，则设置为 {@code true}；否则设置为 {@code false}。
     * @return 返回此 {@code ColoredPrintStream} 实例，以便于链式调用。
     */
    public ColoredPrintStream setEnableColorCodes(boolean enableColorCodes) {
        this.enableColorCodes = enableColorCodes;
        return this;
    }

    /**
     * 将给定的对象转换为字符串，并根据是否启用了颜色代码来应用指定的颜色。
     *
     * @param object 要着色的对象。
     * @param color  应用的颜色代码（如果启用了颜色代码）。
     * @return 包含颜色代码的字符串，或者如果没有启用颜色代码，则返回原始字符串。
     */
    public String coloredText(Object object, String color) {
        return String.format("%s%s%s", enableColorCodes ? color : "", object, enableColorCodes ? RESET : "");
    }

    /**
     * 使用指定的前缀和格式化字符串打印消息。
     * 前缀将使用黄色高亮显示。
     *
     * @param prefix 消息前缀。
     * @param format 格式化字符串。
     * @param args   用于格式化字符串的参数。
     */
    public void printfWithPrefix(String prefix, String format, Object... args) {
        printf("%s ", coloredText('[' + prefix + ']', YELLOW_BRIGHT));
        printf(format, args);
    }

    /**
     * 类似于 {@link #printfWithPrefix(String, String, Object...)}，但在打印后添加换行符。
     *
     * @param prefix 消息前缀。
     * @param format 格式化字符串。
     * @param args   用于格式化字符串的参数。
     */
    public void printflnWithPrefix(String prefix, String format, Object... args) {
        printfWithPrefix(prefix, format, args);
        println();
    }

    public void printlnWithPrefix(String prefix, String message) {
        printflnWithPrefix(prefix, "%s", message);
    }

    /**
     * 格式化并打印一条消息，然后换行。
     *
     * @param format 格式化字符串。
     * @param args   用于格式化字符串的参数。
     */
    public void printfln(String format, Object... args) {
        printf(format, args);
        println();
    }

    public void printlnFromMethod(String message) {
        String caller = getCallerName();
        if (caller.isEmpty()) println(message);
        printlnWithPrefix(caller, message);
    }

    public void longPrintException(Exception ex) {
        print(RED);
        ex.printStackTrace(this);
        print(RESET);
    }

    public void shortPrintException(Exception ex) {
        String caller = getCallerName();
        if (!caller.isEmpty()) printfWithPrefix(caller, "");
        println(coloredText(ex.getMessage(), RED));
    }

    private String getCallerName() {
        var traces = Thread.currentThread().getStackTrace();
        if (traces.length <= 2) return "";
        var traceOptional = Arrays.stream(traces).skip(2).filter(t -> !t.getClassName().equals(this.getClass().getName())).findAny();
        return traceOptional.map(t -> t.getClass().getSimpleName() + "." + t.getMethodName()).orElse("");
    }
}