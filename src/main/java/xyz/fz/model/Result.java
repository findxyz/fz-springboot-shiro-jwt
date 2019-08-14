package xyz.fz.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private boolean success;

    private String message;

    private Object data;

    private String redirect;

    private String jwt;

    public static Result ofSuccess() {
        Result result = new Result();
        result.success = true;
        return result;
    }

    public static Result ofData(Object data) {
        Result result = new Result();
        result.success = true;
        result.data = data;
        return result;
    }

    public static Result ofMessage(String message) {
        Result result = new Result();
        result.success = false;
        result.message = message;
        return result;
    }

    public static Result ofRedirect(String redirect) {
        Result result = new Result();
        result.success = false;
        result.redirect = redirect;
        return result;
    }

    public static Result ofJwt(String jwt) {
        Result result = new Result();
        result.success = true;
        result.jwt = jwt;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
