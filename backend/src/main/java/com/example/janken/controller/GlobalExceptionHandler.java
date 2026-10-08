package com.example.janken.controller;

import com.example.janken.dto.ErrorResponse;
import com.example.janken.service.InvalidHandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 例外をJSONのエラーレスポンスに変換する。
 * スタックトレースは利用者へ返さず、サーバーのログにだけ出す。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** handが未指定・null・未知の値のとき。 */
    @ExceptionHandler(InvalidHandException.class)
    public ResponseEntity<ErrorResponse> handleInvalidHand(InvalidHandException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    /** JSONとして壊れているボディや、空のボディが送られたとき。 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
        log.warn("リクエストボディを解釈できませんでした: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("リクエストボディをJSONとして解釈できません。"));
    }

    /** Content-Type が application/json 以外だったとき。 */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorResponse("Content-Type には application/json を指定してください。"));
    }

    /** 想定外の例外。詳細はログだけに残す。 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("想定外のエラーが発生しました", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("サーバー内部でエラーが発生しました。"));
    }
}
