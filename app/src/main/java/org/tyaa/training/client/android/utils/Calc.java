package org.tyaa.training.client.android.utils;

/**
 * Вычисления
 * */
public class Calc {

    /**
     * Вычислить, сколько процентов составляет число x от числа y
     * */
    public static int percent(int x, int y) {
        return (int) (((double) x / (double) y) * 100d);
    }
}
