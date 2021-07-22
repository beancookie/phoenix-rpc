package com.lzzz.phoenix.rpc.common.serializer;

import com.google.gson.Gson;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public class JsonSerializer<T> implements Serializer<T> {
    private static final Gson GSON = new Gson();

    @Override
    public byte[] serialize(T data) {
        return GSON.toJson(data).getBytes();
    }

    @Override
    public T deserialize(byte[] data, Class<T> clazz) {
        return GSON.fromJson(new String(data), clazz);
    }
}
