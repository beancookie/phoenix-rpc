<p align="center"><a href="https://github.com/BeanCookie/phoenix-rpc"><img src="https://github.com/BeanCookie/phoenix-rpc/blob/main/phoenix-rpc.png" alt="DataEase" width="300" /></a></p>
<h3 align="center">轻量级Java RPC框架</h3>

### 快速开始
#### 1. Spring
```java
public interface HelloService {
    HelloDTO sayHello();
}
```

```java
public class HelloDTO implements Serializable {
    private Integer id;
    private String name;

    public HelloDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```
#### 服务提供方

```java
@Service
@PhoenixService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    public HelloDTO sayHello() {
        return new HelloDTO(1, "abc");
    }
}
```
```java
@Configuration
public class RpcConfig {
    @Bean
    public ServiceInitializer nettyServerAware() {
        return new ServiceInitializer("127.0.0.1", 9998, "127.0.0.1:8848");
    }
}
```

#### 服务消费方

```java
@RestController
public class HelloController {
    @PhoenixReference
    private HelloService helloService;

    @GetMapping("/hello")
    public HelloDTO sayHello() throws InterruptedException {
        return helloService.sayHello();
    }
}
```

```java
@Configuration
public class RpcConfig {
    @Bean
    public ReferenceInitializer nettyServerAware() {
        return new ReferenceInitializer("127.0.0.1:8848");
    }
}
```

#### 2. Spring Boot
// todo