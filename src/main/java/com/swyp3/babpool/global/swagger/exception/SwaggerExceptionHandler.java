package com.swyp3.babpool.global.swagger.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SwaggerExceptionHandler {

//    @ExceptionHandler(SwaggerException.class)
//    protected ResponseEntity<ApiErrorResponse> handleSwaggerException(SwaggerException exception){
//        log.error("SwaggerException getErrorCode() >> "+exception.getErrorCode());
//        log.error("SwaggerException getMessage() >> "+exception.getMessage());
//        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
//                .body(ApiErrorResponse.of(exception.getErrorCode()));
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SwaggerException.class)
    protected String handleSwaggerExceptionWithErrorPage(SwaggerException exception){
        log.error("SwaggerException getErrorCode() >> "+exception.getErrorCode());
        log.error("SwaggerException getMessage() >> "+exception.getMessage());
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "    <style>\n" +
                "        * {transition: all 0.6s;}\n" +
                "        html {height: 100%;}\n" +
                "        body {font-family: 'Lato', sans-serif; color: #888; margin: 0;}\n" +
                "        #main {display: table; width: 100%; height: 100vh; text-align: center;}\n" +
                "        .fof {display: table-cell; vertical-align: middle;}\n" +
                "        .fof h1 {font-size: 50px; display: inline-block; padding-right: 12px; animation: type .5s alternate infinite;}\n" +
                "        @keyframes type {from {box-shadow: inset -3px 0px 0px #888;} to {box-shadow: inset -3px 0px 0px transparent;}}\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"main\">\n" +
                "        <div class=\"fof\">\n" +
                "            <h1>Error "+exception.getErrorCode().getHttpStatus()+"</h1>\n" +
                "            <h2>"+exception.getErrorCode().getMessage()+"</h2>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
        return html;
    }
}