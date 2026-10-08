package com.example.janken.dto;

/**
 * POST /api/janken の正常レスポンス。
 * 例: {"playerHand":"rock","computerHand":"scissors","result":"win"}
 */
public record JankenResponse(String playerHand, String computerHand, String result) {
}
