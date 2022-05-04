```shell
Warning:java: 未知的枚举常量 javax.annotation.meta.When.MAYBE， 
找不到javax.annotation.meta.When的类文件
```

添加google findbugs

```xml

<dependency>
    <groupId>com.google.code.findbugs</groupId>
    <artifactId>annotations</artifactId>
    <version>3.0.1</version>
</dependency>
```

`````groovy
dependencies {
    implementation 'com.google.code.findbugs:annotations:3.0.1'
}
`````
