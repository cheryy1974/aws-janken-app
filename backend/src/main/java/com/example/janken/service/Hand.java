package com.example.janken.service;

/**
 * じゃんけんの手。
 * APIでやり取りする文字列（rock / scissors / paper）と、Java内部の値を対応づける。
 */
public enum Hand {

    ROCK("rock"),
    SCISSORS("scissors"),
    PAPER("paper");

    private final String apiValue;

    Hand(String apiValue) {
        this.apiValue = apiValue;
    }

    /** JSONで送受信する文字列表現を返す。 */
    public String apiValue() {
        return apiValue;
    }

    /**
     * APIで受け取った文字列をHandへ変換する。
     * null・未知の値は {@link InvalidHandException} とし、Controller側で400を返す。
     */
    public static Hand fromApiValue(String value) {
        if (value != null) {
            for (Hand hand : values()) {
                if (hand.apiValue.equals(value)) {
                    return hand;
                }
            }
        }
        throw new InvalidHandException();
    }
}
