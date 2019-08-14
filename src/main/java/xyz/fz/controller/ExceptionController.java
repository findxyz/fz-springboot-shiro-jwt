package xyz.fz.controller;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.fz.model.Result;
import xyz.fz.util.ErrMsgUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @Value("${shiro.loginUrl}")
    private String loginUrl;

    @ExceptionHandler(UnauthenticatedException.class)
    public void unauthc(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public void unauthz(HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUrl);
    }

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        LOGGER.error(ErrMsgUtil.getStackTrace(e));
        return Result.ofMessage("server internal error");
    }
}
