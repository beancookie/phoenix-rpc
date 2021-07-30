package com.lzzz.phoenix.common.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.lzzz.phoenix.common.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;

public class KryoSerializer implements Serializer {

    @Override
    public byte[] serialize(Object data) {
        Kryo kryo = new Kryo();
        kryo.register(data.getClass(), new JavaSerializer());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output out = new Output(byteArrayOutputStream);
        kryo.writeObject(out, data);
        out.close();
        return byteArrayOutputStream.toByteArray();

    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.register(clazz, new JavaSerializer());
        Input in = new Input(new ByteArrayInputStream(bytes));
        Object result = kryo.readObject(in, clazz);
        in.close();
        return result;

    }
}
