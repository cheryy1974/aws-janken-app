package com.example.janken.controller;

import com.example.janken.dto.JankenRequest;
import com.example.janken.dto.JankenResponse;
import com.example.janken.service.JankenService;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ブラウザーのJavaScriptから呼ばれるREST API。
 * JSONを受け取り、JSONを返すだけに専念し、判定処理はServiceに任せる。
 */
@RestController
@RequestMapping("/api")
public class JankenController {

    private final JankenService jankenService;

    public JankenController(JankenService jankenService) {
        this.jankenService = jankenService;
    }

    /**
     * じゃんけんを1回行う。
     * 相手の手の決定と勝敗判定はこのJava側で実行する。
     */
    @PostMapping(
            path = "/janken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public JankenResponse janken(@RequestBody JankenRequest request) {
        // JSONが {} だった場合などに備え、hand が null でもService側で400にできるようにする。
        return jankenService.play(request == null ? null : request.hand());
    }

    /** 動作確認用。常に {"status":"ok"} を返す。 */
    @GetMapping(path = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
