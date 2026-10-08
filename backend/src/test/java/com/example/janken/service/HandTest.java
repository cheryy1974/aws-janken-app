package com.example.janken.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/** APIで受け取った文字列からHandへの変換のテスト。 */
class HandTest {

    @ParameterizedTest
    @CsvSource({"rock, ROCK", "scissors, SCISSORS", "paper, PAPER"})
    void 正しい文字列はHandに変換できる(String apiValue, Hand expected) {
        assertThat(Hand.fromApiValue(apiValue)).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "Rock", "ROCK", "ぐー", "stone", " rock"})
    void 不正な文字列は例外になる(String apiValue) {
        assertThatThrownBy(() -> Hand.fromApiValue(apiValue))
                .isInstanceOf(InvalidHandException.class)
                .hasMessage(InvalidHandException.MESSAGE);
    }

    @Test
    void 各Handは想定どおりの文字列を返す() {
        assertThat(Hand.ROCK.apiValue()).isEqualTo("rock");
        assertThat(Hand.SCISSORS.apiValue()).isEqualTo("scissors");
        assertThat(Hand.PAPER.apiValue()).isEqualTo("paper");
    }
}
