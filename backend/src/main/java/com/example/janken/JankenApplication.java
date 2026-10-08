package com.example.janken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * アプリケーションの起動クラス。
 * ここから組み込みTomcatが起動し、8080番ポートでHTTPリクエストを待ち受ける。
 */
@SpringBootApplication
public class JankenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JankenApplication.class, args);
    }
}
