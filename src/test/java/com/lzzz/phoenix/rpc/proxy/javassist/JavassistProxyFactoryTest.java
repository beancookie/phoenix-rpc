package com.lzzz.phoenix.rpc.proxy.javassist;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JavassistProxyFactoryTest {

    public interface HelloService {

        void say(String msg);

        String echo(String msg);

        String[] getHobbies();
    }

    public class HelloServiceImpl implements HelloService {

        @Override
        public void say(String msg) {
            System.out.println(msg);
        }

        @Override
        public String echo(String msg) {
            return msg;
        }

        @Override
        public String[] getHobbies() {
            return new String[] {"a", "b", "c"};
        }
    }

    @Test
    public void testCreateReference() throws Throwable {
        ProxyFactory factory = new JavassistProxyFactory();

        HelloService target = new HelloServiceImpl();

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                //打印日志
                System.out.println("[before] The method " + methodName + " begins");
                Object result = null;
                try{
                    //前置通知
                    result = method.invoke(target, args);
                    //返回通知, 可以访问到方法的返回值
                    System.out.println(String.format("after method:%s execute", method.getName()));
                } catch (Exception e){
                    e.printStackTrace();
                    //异常通知, 可以访问到方法出现的异常
                }
                //后置通知. 因为方法可以能会出异常, 所以访问不到方法的返回值
                //打印日志
                System.out.println("[after] The method ends with " + result);
                return result;
            }
        };

        HelloService proxy = factory.createReference(HelloService.class, handler);
        HelloService proxy1 = factory.createReference(HelloService.class, handler);
        HelloService proxy2 = factory.createReference(HelloService.class, handler);

        assertNotNull(proxy);

        proxy.say("ricky");
        proxy.echo("world");
        proxy.getHobbies();
    }

}