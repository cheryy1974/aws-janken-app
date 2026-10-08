package com.example.janken.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.janken.service.Hand;
import com.example.janken.service.HandGenerator;
import com.example.janken.service.InvalidHandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * APIの入出力のテスト。
 * 相手の手を決めるHandGeneratorだけをモックに差し替え、結果を固定して検証する。
 */
@SpringBootTest
@AutoConfigureMockMvc
class JankenApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HandGenerator handGenerator;

    @Test
    void ヘルスチェックはokを返す() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void グーでチョキに勝つ() throws Exception {
        given(handGenerator.next()).willReturn(Hand.SCISSORS);

        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"rock\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerHand").value("rock"))
                .andExpect(jsonPath("$.computerHand").value("scissors"))
                .andExpect(jsonPath("$.result").value("win"));
    }

    @Test
    void パーはグーに勝ちチョキには負ける() throws Exception {
        given(handGenerator.next()).willReturn(Hand.ROCK);
        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"paper\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("win"));

        given(handGenerator.next()).willReturn(Hand.SCISSORS);
        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"paper\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("lose"));
    }

    @Test
    void 同じ手ならあいこ() throws Exception {
        given(handGenerator.next()).willReturn(Hand.SCISSORS);

        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"scissors\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("draw"));
    }

    @ParameterizedTest(name = "リクエスト {0} は400")
    @ValueSource(strings = {
            "{}",                      // handが無い
            "{\"hand\":null}",         // handがnull
            "{\"hand\":\"\"}",         // 空文字
            "{\"hand\":\"guu\"}",      // 未知の値
            "{\"hand\":\"ROCK\"}",     // 大文字（未知の値として扱う）
            "{\"hand\":123}",          // 数値
    })
    void handが不正なら400とJSONのメッセージを返す(String body) throws Exception {
        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(InvalidHandException.MESSAGE));
    }

    @ParameterizedTest(name = "壊れたJSON {0} は400")
    @ValueSource(strings = {
            "{\"hand\":",   // 途中で終わっている
            "rock",         // JSONではない
            "",             // 空のボディ
    })
    void JSONとして壊れていれば400を返す(String body) throws Exception {
        mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void エラー応答にスタックトレースを含めない() throws Exception {
        String responseBody = mockMvc.perform(post("/api/janken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"guu\"}"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        org.assertj.core.api.Assertions.assertThat(responseBody)
                .doesNotContain("Exception")
                .doesNotContain("com.example.janken");
    }
}
