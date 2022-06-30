> 使用Import注解注入bean在XXXAutoConfiguration优先与@bean

```java

@Import(HttpServiceFactory.class) //优先级高
@AutoConfiguration
public class HttpAutoConfiguration {
    @Bean//使用此方式controller会获取不到RemoteService
    public HttpServiceFactory httpServiceFactory() {
        return new HttpServiceFactory();
    }
}

```
