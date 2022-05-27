> gradle没有package的概念,目前就两种<br>
> 一个BOM(插件java-platform)<br>
> 一个JAR(插件java或者java-library(建议引用这种))<br>
> 插件java与java-library区别

```groovy
apply plugin: 'java'
implementation "org.springframework.boot:spring-boot-starter-web"

apiElements "org.springframework.boot:spring-boot-starter-web"
```

> 不具有传递性，比如A使用上述(implementation)方式引用，B又引用A，B是无法获取到Spring Boot Web<br>
> 使用(apiElements)代表此jar包不需要使用，引用jar包可以获取，具有传递性，但是本身无法使用<br>

```groovy
apply plugin: 'java-library'
api "org.springframework.boot:spring-boot-starter-web"
```

> api就是为了解决上述问题产生出来的，本身可以使用，同时具有传递性(就是MVN的dependency)必须使用插件java-library<br>

```groovy
apply plugin: 'io.spring.dependency-management'
dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

> 引用BOM，gradle原生插件并没有此功能，Spring团队开发的插件如上(注意此插件有版本)<br>
> dependency也是这个插件的

```groovy
apply plugin: 'org.springframework.boot'
```

> SpringBoot项目打包插件，同时也是引用SpringBoot的插件(注意此插件有版本)<br>
> gradle项目没有Mvn里的父依赖，gradle更像是一种包含项目<br>
> 非SpringBoot项目不需要引入此插件，否则打包会出现MainClassNotFound<br>
> 也可以使用以下方式构建(一般用于顶级父项目)子项目就可以不标注版本<br>

```groovy
buildscript {
    ext {
        set('springBootVersion', '2.6.2')
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
//不具有传递性的引用(SpringBoot项目使用)
implementation "org.springframework.boot:spring-boot-starter-web"
//具有传递性的引用，但本身不可使用(普通jar使用)
apiElements "org.springframework.boot:spring-boot-starter-web"
//上两个的合体版(普通jar使用)
api "org.springframework.boot:spring-boot-starter-web"
//test依赖引用
testImplementation 'org.springframework.boot:spring-boot-starter-test'
//只参与编译过程
compileOnly 'org.projectlombok:lombok'
//注解生成器,用于查找注解，生成Class等
annotationProcessor 'org.projectlombok:lombok'
//只参与运行过程
runtimeOnly 'mysql:mysql-connector-java'
```

> 规范：<br>
> 普通jar项目-》引用插件、描述、依赖、test <br>
> springboot项目-》插件、描述、springboot构建、依赖、test <br>
> 顶级父项目-》插件、版本等描述、buildscript、allprojects、subprojects、test <br>
> buildscript一般用来构建springboot配置-》版本控制、仓库设置、springboot gradle依赖引入 <br>
> allprojects 版本控制等、仓库设置、编译字符集TASK <br>
> subprojects 子项目插件统一导入、需要排除的依赖、需要配置的依赖 <br>
> allprojects与subprojects(多个博客推荐共同使用) <br>
> 每一个配置，例如插件、描述等之间空格一行
> 依赖导入、需要引用set的变量使用双引号"org.xx:xxx:${xxxxVersion}",不需要的话，双引号单引号都可使用

> 主要使用插件 <br>
> java-library引用api <br>
> io.spring.dependency-management引用mavenBom、dependency <br>
> org.springframework.boot 标注这是一个springboot项目 <br>
> maven-publish上传jar包

```groovy
allprojects {
    repositories {
        mavenLocal()
        maven { url 'https://repo.spring.io/libs-snapshot/' }
        maven {
            url 'https://maven.aliyun.com/repository/public'
        }
        maven {
            url "https://maven.aliyun.com/repository/public"
        }
        maven {
            url "https://maven.aliyun.com/repository/jcenter"
        }
        maven {
            url "https://maven.aliyun.com/repository/spring"
        }
        maven {
            url "https://maven.aliyun.com/repository/spring-plugin"
        }
        maven {
            url "https://maven.aliyun.com/repository/Gradle-plugin"
        }
        maven {
            url "https://maven.aliyun.com/repository/google"
        }
        maven {
            url "https://maven.aliyun.com/repository/grails-core"
        }
        maven {
            url "https://maven.aliyun.com/repository/apache-snapshots"
        }
    }
}
```

> gradle与idea的兼容性有点小问题，很多爆红的，但是可以运行

> [idea使用gradle有bug](https://blog.csdn.net/Icannotdebug/article/details/83081745) <br>
> 方式一:调用gradle classes(提前编译好再运行项目) <br>
> 方式二:修改idea gradle配置build and run using换成idea，这样编译输出的文件会变成out<br>
> 方式三:使用Gradle Plugin[示例](./buildSrc/src/main/groovy/com/livk/plugin/ResourcesPlugin.groovy)<br>
>
方式四：[idea官方人员建议使用java而不是java-library](https://youtrack.jetbrains.com/issue/IDEA-292741/Spring-Boot-Gradle-project-resources-from-dependency-module-are-#focus=Comments-27-6069566.0-0)
