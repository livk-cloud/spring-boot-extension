> gradle没有package的概念,目前就两种<br>
> 一个BOM(插件java-platform)<br>
> 一个JAR(插件java或者java-library)<br>
> 插件java与java-library区别

```groovy
apply plugin: 'java'
implementation "org.springframework.boot:spring-boot-starter-web"

apiElements "org.springframework.boot:spring-boot-starter-web"
```

> 不具有传递性，比如A使用上述(implementation)方式引用，B又引用A，B是无法获取到Web<br>
> 使用(apiElements)代表此jar包不需要使用，引用jar包可以获取，具有传递性，但是本身无法使用<br>

```groovy
apply plugin: 'java-library'
api "org.springframework.boot:spring-boot-starter-web"
```

> api就是为了解决上述问题产生出来的，本身可以使用，同时具有传递性<br>

```groovy
apply plugin: 'io.spring.dependency-management'
dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

> 引用BOM，gradle原生插件并没有此功能，Spring团队开发的插件如上(注意此插件有版本)<br>

```groovy
apply plugin: 'org.springframework.boot'
```

> SpringBoot项目打包插件，同时也是引用SpringBoot的插件(注意此插件有版本)<br>
> gradle项目没有Mvn里的父依赖，gradle更像是一种包含项目<br>
> 非SpringBoot项目不需要引入此插件，否则打包会出现MainClassNotFound<br>
> 也可以使用以下方式构建<br>

```groovy
buildscript {
    ext {
        set('springBootVersion', '3.0.0-SNAPSHOT')
    }

    repositories {
        mavenCentral()
        maven { url 'https://repo.spring.io/libs-snapshot/' }
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}
```

> 引入依赖的方式

```groovy
implementation "org.springframework.boot:spring-boot-starter-web"
//不具有传递性的引用
apiElements "org.springframework.boot:spring-boot-starter-web"
//具有传递性的引用，但本身不可使用
api "org.springframework.boot:spring-boot-starter-web"
//上两个的合体版
testImplementation 'org.springframework.boot:spring-boot-starter-test'
//test依赖引用
compileOnly 'org.projectlombok:lombok'
//只参与编译过程
annotationProcessor 'org.projectlombok:lombok'
//注解生成器,用于查找注解，生成Class等
runtimeOnly 'mysql:mysql-connector-java'
//只参与运行过程
```
