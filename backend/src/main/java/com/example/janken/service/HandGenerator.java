package com.example.janken.service;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * 相手（コンピューター）の手をランダムに決めるクラス。
 * 乱数の部分を分離しておくと、勝敗判定のテストに乱数が混ざらない。
 */
@Component
public class HandGenerator {

    private final Random random = new Random();

    /** グー・チョキ・パーの3種類から1つをランダムに返す。 */
    public Hand next() {
        Hand[] hands = Hand.values();
        return hands[random.nextInt(hands.length)];
    }
}
