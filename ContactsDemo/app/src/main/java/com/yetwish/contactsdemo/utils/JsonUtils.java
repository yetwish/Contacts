package com.yetwish.contactsdemo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yetwish.contactsdemo.model.Contacts;

import java.lang.reflect.Type;
import java.util.List;

/**
 * json操作帮助类
 * Created by yetwish on 2016/9/9.
 */
public class JsonUtils {

    private static Gson sGson = new Gson();

    private JsonUtils() {
        throw new UnsupportedOperationException("JsonUtils cannot be instantiated");
    }

    /**
     * 将数组转换为json文本
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t) {
        return sGson.toJson(t);
    }

    /**
     * 将json文本转换为List<T>
     *
     * @param jsonStr
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> List<T> listFromJson(String jsonStr, Class<? extends T> classOfT) {
        Type type = new TypeToken<List<T>>() {}.getType();
        return sGson.fromJson(jsonStr, type);
    }

    /**
     * 将json文本转换为 T
     * @param jsonStr
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T objFromJson(String jsonStr, Class<? extends T> classOfT) {
        return sGson.fromJson(jsonStr, classOfT);
    }


}
