package com.apmall.dto.defaultRes;

import com.apmall.constant.ResponseMessage;
import com.apmall.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DefaultRes<T> {

    public int statusCode;
    public String responseMessage;
    public T data;

    public DefaultRes(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static <T> DefaultRes<T> res(final int statusCode, final String responseMessage, final T t) {
        return DefaultRes.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }


    public static HashMap<String, Object> getRes(List<Object> result) {
        HashMap<String, Object> resMap = new HashMap<>();
        List<Object> resultList = result;

        if (resultList != null && resultList.size() != 0) {
            resMap.put("defaultRes", res(StatusCode.OK, ResponseMessage.SUCCESS, result));
            resMap.put("HttpStatus", HttpStatus.OK);
        } else {
            resMap.put("defaultRes", res(StatusCode.NO_CONTENT, ResponseMessage.NO_CONTENT));
            resMap.put("HttpStatus", HttpStatus.NO_CONTENT);
        }

        return resMap;
    }

    public static HashMap<String, Object> getRes(int result) {

        HashMap<String, Object> resMap = new HashMap<>();

        if (result > 0) {
            resMap.put("defaultRes", res(StatusCode.OK, ResponseMessage.SUCCESS));
            resMap.put("HttpStatus", HttpStatus.OK);
        } else {

            resMap.put("defaultRes", res(StatusCode.NO_CONTENT, ResponseMessage.NO_CONTENT));
            resMap.put("HttpStatus", HttpStatus.NO_CONTENT);
        }

        return resMap;
    }

    public static HashMap<String, Object> getBadRequestRes() {

        HashMap<String, Object> resMap = new HashMap<>();

        resMap.put("defaultRes", res(StatusCode.BAD_REQUEST, ResponseMessage.BAD_REQUEST));
        resMap.put("HttpStatus", HttpStatus.BAD_REQUEST);

        return resMap;
    }
}