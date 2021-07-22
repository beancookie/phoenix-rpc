package com.lzzz.phoenix.rpc.common.serializer;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public interface Serializer<T> {
    byte[] serialize(T data);

    T deserialize(byte[] bytes, Class<T> clazz);
}
