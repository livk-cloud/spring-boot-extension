> java(Kotlin)编写脚本使用 META-INF/gradle-plugins/{pluginName}.properties
> build.gradle使用

```properties
implementation-class=全类名
```

```groovy
apply plugin: 'java-gradle-plugin'
gradlePlugin {
    plugins {
        resourcesPlugin {
            id = 'livk.resources.plugin'
            implementationClass = 'com.livk.plugin.ResourcesPlugin'
        }
    }
}

/*
kt使用插件
 */
apply plugin: 'kotlin'

buildscript {
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10" 需要与idea当前kt版本对应
    }
}
```

> groovy编写脚本 使用 META-INF/gradle-plugins/{pluginName}.properties

```properties
implementation-class=全类名
```

```groovy
class ResourcesPlugin implements Plugin<Project> {
    
    @Override
    void apply(Project project) {
        project.tasks
                .getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
                .dependsOn(Format.NAME, JavaPlugin.PROCESS_RESOURCES_TASK_NAME)
    }
}

task compile(dependsOn: [compileJava, processResources, compileTestJava, processTestResources] )
```
