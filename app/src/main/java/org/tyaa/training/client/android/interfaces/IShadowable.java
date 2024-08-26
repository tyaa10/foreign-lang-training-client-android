package org.tyaa.training.client.android.interfaces;

/**
 * Абстракция затенения и снятия тени
 * */
public interface IShadowable {

    /**
     * Затенить представление
     * */
    void shade();
    /**
     * Снять тень с представления
     * */
    void unshade();
}
