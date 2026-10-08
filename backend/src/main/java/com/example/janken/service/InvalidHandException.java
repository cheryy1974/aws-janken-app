package com.example.janken.service;

/**
 * handの値が不正だったことを表す例外。
 * 利用者へ返すメッセージをここに1か所だけ持たせる。
 */
public class InvalidHandException extends RuntimeException {

    public static final String MESSAGE = "handにはrock、scissors、paperのいずれかを指定してください。";

    public InvalidHandException() {
        super(MESSAGE);
    }
}
