package com.dev.cinema.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionAdvice {
    public static final int STATUS_404 = 404;

    @ExceptionHandler
    InternalException emptyCartException(EmptyCartException e, HttpServletRequest req,
                                              HttpServletResponse resp) {
        return handleCommon(e, STATUS_404, req, resp);
    }


    @ExceptionHandler
    InternalException emptyCartException(UserNotFoundException e, HttpServletRequest req,
                                         HttpServletResponse resp) {
        return handleCommon(e, STATUS_404, req, resp);
    }

    private <T extends Throwable> InternalException handleCommon(T e, int status, HttpServletRequest req,
                                                                 HttpServletResponse resp) {
        InternalException ex = new InternalException(e.getMessage(), e, status, req.getRequestURI());
        resp.setStatus(ex.getStatus());
        return ex;
    }
}
