package com.example.janken.config;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS（別オリジンのページからAPIを呼べるようにする設定）。
 *
 * <p>画面はS3やローカルのHTTPサーバーから配信され、APIはEC2で動くため、
 * ブラウザーから見ると「別オリジン」になる。許可するオリジンは環境変数
 * ALLOWED_ORIGINS で切り替える。</p>
 *
 * <p>注意: CORSはブラウザーを守るための仕組みであり、APIの認証や
 * ネットワーク制限の代わりにはならない。アクセス元の制限は
 * セキュリティグループなどで別に行う。</p>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    private final List<String> allowedOrigins;

    public CorsConfig(@Value("${janken.allowed-origins:}") String allowedOrigins) {
        // カンマ区切りの文字列を分解し、前後の空白と空要素を取り除く。
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();
        log.info("CORSで許可するオリジン: {}", this.allowedOrigins);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 本番想定ではワイルドカード（*）を使わず、必要なオリジンだけを列挙する。
                .allowedOrigins(allowedOrigins.toArray(String[]::new))
                // 画面が使うのはGETとPOST。OPTIONSはプリフライト用にSpringが処理する。
                .allowedMethods("GET", "POST", "OPTIONS")
                // JSONを送るために必要なヘッダー。
                .allowedHeaders("Content-Type")
                // 認証Cookieは使わない。
                .allowCredentials(false)
                .maxAge(3600);
    }
}
