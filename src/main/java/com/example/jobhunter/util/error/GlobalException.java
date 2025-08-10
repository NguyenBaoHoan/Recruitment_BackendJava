package com.example.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.jobhunter.controller.RestResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = { IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception exception) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(exception.getMessage());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { NoResourceFoundException.class })
    public ResponseEntity<?> handleNotFoundException(Exception ex, HttpServletRequest request) throws Exception {
        String path = request.getRequestURI();
        if (path.startsWith("/api/")) {
            RestResponse<Object> res = new RestResponse<>();
            res.setStatusCode(HttpStatus.BAD_REQUEST.value());
            res.setError(ex.getMessage());
            res.setMessage("404 Not Found, URL may not exist...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            throw ex;
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> FieldErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());
        // convert each error element => List
        List<String> errors = FieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        // if > 1 ? arr : string
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
