package com.example.janken.dto;

/**
 * エラー時に返すJSON。
 * 例: {"message":"handにはrock、scissors、paperのいずれかを指定してください。"}
 */
public record ErrorResponse(String message) {
}
