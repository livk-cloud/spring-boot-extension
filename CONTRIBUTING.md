# 向 spring-boot-extension贡献代码

## 项目环境

- 使用SpringBoot最新版本
- JDK 21
- Gradle最新版
- 本地需要安装有Docker

## 使用 GitHub Issues

使用GitHub Issues记录项目出现的各种问题，或者提出新的意见、示例等建议。

## 提交代码前的工作

使用 `gradle build` 构建当前提交的代码，以确保代码可以正常运行。

## Gradle Custom Plugins

- **"com.livk.bom"** - 用于BOM或dependencies的管理
- **"com.livk.module"** - 用于标记模块，支持所有可用节点
- **"com.livk.common"** - 打包方式为普通JAR，而不是Spring Boot JAR
- **"com.livk.service"** - 打包方式为Spring Boot JAR
- **"com.livk.root"** - 用于标记根节点，从而直接操作子包
- **"com.livk.mvn.deployed"** - 用于部署并发布到Maven

## Gradle Dependency API

- **management platform** - 用于添加BOM
- **compileProcessor** - 用于编译器的注解处理器
- **optional** - 等同于Maven中的optional
- **api** - 可传递的依赖引用
- **implementation** - 不可传递的依赖引用
