package com.example.janken.service;

import org.springframework.stereotype.Service;

/**
 * 勝敗判定だけを担当するクラス。
 * 乱数を一切使わないので、手の組み合わせを指定して全パターンをテストできる。
 */
@Service
public class JudgeService {

    /**
     * プレイヤーの手と相手の手から、プレイヤー視点の勝敗を返す。
     */
    public Result judge(Hand playerHand, Hand computerHand) {
        if (playerHand == computerHand) {
            return Result.DRAW;
        }
        return switch (playerHand) {
            case ROCK -> computerHand == Hand.SCISSORS ? Result.WIN : Result.LOSE;
            case SCISSORS -> computerHand == Hand.PAPER ? Result.WIN : Result.LOSE;
            case PAPER -> computerHand == Hand.ROCK ? Result.WIN : Result.LOSE;
        };
    }
}
