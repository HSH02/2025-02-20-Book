package com.book.global.common;

import org.springframework.http.HttpStatus;

/**
 * 공통 API 응답을 나타내는 클래스
 *
 * @param <T> 응답 데이터 타입
 */
public record ApiResponse<T>(boolean success, String message, T data, int status) {

    /**
     * 공통 응답 객체 생성
     *
     * @param success 요청 성공 여부
     * @param message 응답 메시지
     * @param data    응답 데이터
     * @param status  HTTP 상태 코드
     * @return ApiResponse 객체
     */
    public static <T> ApiResponse<T> of(boolean success, String message, T data, HttpStatus status) {
        return new ApiResponse<>(success, message, data, status.value());
    }

    /**
     * 200 응답을 반환합니다.
     *
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success() {
        return of(true, "요청 성공", null, HttpStatus.OK);
    }

    /**
     * 200 응답을 반환합니다.
     *
     * @param data 응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return of(true, "요청 성공", data, HttpStatus.OK);
    }

    /**
     * 200 응답을 반환합니다.
     *
     * @param message 응답 메시지
     * @param data    응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return of(true, message, data, HttpStatus.OK);
    }

    /**
     * 201 응답을 반환합니다.
     *
     * @return 생성 성공 응답 객체
     */
    public static <T> ApiResponse<T> created() {
        return of(true, "리소스 생성 성공", null, HttpStatus.CREATED);
    }

    /**
     * 201 응답을 반환합니다.
     *
     * @param data 생성된 리소스 데이터
     * @return 생성 성공 응답 객체
     */
    public static <T> ApiResponse<T> created(T data) {
        return of(true, "리소스 생성 성공", data, HttpStatus.CREATED);
    }

    /**
     * 401 응답을 반환합니다.
     *
     * @return 인증 실패 응답 객체
     */
    public static <T> ApiResponse<T> unauthorized() {
        return of(false, "인증 오류", null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 403 응답을 반환합니다.
     *
     * @return 권한 없음 응답 객체
     */
    public static <T> ApiResponse<T> forbidden() {
        return of(false, "인가 오류", null, HttpStatus.FORBIDDEN);
    }


    /**
     * 400 응답을 반환합니다.
     *
     * @return 잘못된 요청 응답 객체
     */
    public static <T> ApiResponse<T> badRequest() {
        return of(false, "클라이언트 오류", null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 400 응답을 반환합니다.
     *
     * @param message 오류 메시지
     * @return 잘못된 요청 응답 객체
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return of(false, message, null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 404 응답을 반환합니다.
     *
     * @param message 오류 메시지
     * @return 리소스 없음 응답 객체
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return of(false, message, null, HttpStatus.NOT_FOUND);
    }


    /**
     * 500 응답을 반환합니다.
     *
     * @return 서버 오류 응답 객체
     */
    public static <T> ApiResponse<T> error() {
        return of(false, "서버 오류", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 500 응답을 반환합니다.
     *
     * @param message 오류 메시지
     * @return 서버 오류 응답 객체
     */
    public static <T> ApiResponse<T> error(String message) {
        return of(false, message, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
