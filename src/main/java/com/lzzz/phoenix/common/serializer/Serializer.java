package com.lzzz.phoenix.common.serializer;

/**
 * @author: LuZhong
 * @date: 2021/07/19
 */
public interface Serializer {
    byte[] serialize(Object data);

    <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
