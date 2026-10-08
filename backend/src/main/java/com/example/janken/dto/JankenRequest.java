package com.example.janken.dto;

/**
 * POST /api/janken のリクエストボディ。
 * 例: {"hand":"rock"}
 */
public record JankenRequest(String hand) {
}
