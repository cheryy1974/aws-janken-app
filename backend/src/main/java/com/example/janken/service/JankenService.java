package com.example.janken.service;

import com.example.janken.dto.JankenResponse;
import org.springframework.stereotype.Service;

/**
 * じゃんけん1回分の流れをまとめるクラス。
 * 「入力の変換 → 相手の手を決める → 勝敗判定 → レスポンスを組み立てる」を順に行う。
 */
@Service
public class JankenService {

    private final HandGenerator handGenerator;
    private final JudgeService judgeService;

    public JankenService(HandGenerator handGenerator, JudgeService judgeService) {
        this.handGenerator = handGenerator;
        this.judgeService = judgeService;
    }

    /**
     * @param requestedHand APIで受け取ったhandの文字列
     * @return プレイヤーの手・相手の手・勝敗を含むレスポンス
     */
    public JankenResponse play(String requestedHand) {
        Hand playerHand = Hand.fromApiValue(requestedHand);
        Hand computerHand = handGenerator.next();
        Result result = judgeService.judge(playerHand, computerHand);

        return new JankenResponse(playerHand.apiValue(), computerHand.apiValue(), result.apiValue());
    }
}
