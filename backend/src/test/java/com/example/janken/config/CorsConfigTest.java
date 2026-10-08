package com.example.janken.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * CORSのテスト。
 * 許可したオリジンにだけ Access-Control-Allow-Origin を返すことを確認する。
 */
@SpringBootTest(properties = "janken.allowed-origins=http://localhost:8000,http://example-bucket.s3-website-ap-northeast-1.amazonaws.com")
@AutoConfigureMockMvc
class CorsConfigTest {

    private static final String ALLOWED_ORIGIN = "http://localhost:8000";
    private static final String DENIED_ORIGIN = "http://evil.example.com";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 許可オリジンのプリフライトは通る() throws Exception {
        mockMvc.perform(options("/api/janken")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ALLOWED_ORIGIN))
                .andExpect(header().stringValues("Access-Control-Allow-Methods", "GET,POST,OPTIONS"));
    }

    @Test
    void 許可オリジンからの実リクエストに許可ヘッダーが付く() throws Exception {
        mockMvc.perform(post("/api/janken")
                        .header("Origin", ALLOWED_ORIGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"rock\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ALLOWED_ORIGIN));
    }

    @Test
    void 許可していないオリジンのプリフライトは拒否される() throws Exception {
        mockMvc.perform(options("/api/janken")
                        .header("Origin", DENIED_ORIGIN)
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void 許可していないオリジンの実リクエストには許可ヘッダーを返さない() throws Exception {
        mockMvc.perform(post("/api/janken")
                        .header("Origin", DENIED_ORIGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hand\":\"rock\"}"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void 認証情報の送信は許可しない() throws Exception {
        mockMvc.perform(options("/api/janken")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Access-Control-Allow-Credentials"));
    }
}
