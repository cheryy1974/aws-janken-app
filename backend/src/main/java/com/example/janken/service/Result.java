package com.example.janken.service;

/**
 * 勝敗。プレイヤー視点で win / lose / draw を表す。
 */
public enum Result {

    WIN("win"),
    LOSE("lose"),
    DRAW("draw");

    private final String apiValue;

    Result(String apiValue) {
        this.apiValue = apiValue;
    }

    /** JSONで返す文字列表現を返す。 */
    public String apiValue() {
        return apiValue;
    }
}
