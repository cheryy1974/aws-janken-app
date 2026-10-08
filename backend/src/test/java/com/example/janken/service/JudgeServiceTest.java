package com.example.janken.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * 勝敗判定のテスト。
 * 乱数を使わないクラスなので、3×3=9通りすべてを固定の組み合わせで検証できる。
 */
class JudgeServiceTest {

    private final JudgeService judgeService = new JudgeService();

    @ParameterizedTest(name = "自分={0} 相手={1} → {2}")
    @CsvSource({
            // あいこ（3通り）
            "ROCK,     ROCK,     DRAW",
            "SCISSORS, SCISSORS, DRAW",
            "PAPER,    PAPER,    DRAW",
            // 勝ち（3通り）
            "ROCK,     SCISSORS, WIN",
            "SCISSORS, PAPER,    WIN",
            "PAPER,    ROCK,     WIN",
            // 負け（3通り）
            "ROCK,     PAPER,    LOSE",
            "SCISSORS, ROCK,     LOSE",
            "PAPER,    SCISSORS, LOSE",
    })
    void 全9通りの勝敗判定が正しい(Hand playerHand, Hand computerHand, Result expected) {
        assertThat(judgeService.judge(playerHand, computerHand)).isEqualTo(expected);
    }
}
