package top.huajieyu001.blog.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) throws Exception {
        throw ex;
//        System.err.println(ex.getMessage());
//        ErrorResponse error = new ErrorResponse() {
//            @Override
//            public HttpStatusCode getStatusCode() {
//                return HttpStatusCode.valueOf(500);
//            }
//
//            @Override
//            public ProblemDetail getBody() {
//                return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//            }
//        };
//        return ResponseEntity.status(500).body(error);
    }
}