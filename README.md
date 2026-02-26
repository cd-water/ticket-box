# 电影时刻后端

## 前言

`电影时刻`项目是一个电影票务系统，采用前后端分离架构，包含后端服务、前台APP系统以及后台管理系统。

**后端项目传送门**：[cd-water/movie-time](https://github.com/cd-water/movie-time)

**APP端项目传送门**：[cd-water/movie-time-app](https://github.com/cd-water/movie-time-app)

**管理端项目传送门**：[cd-water/movie-time-admin](https://github.com/cd-water/movie-time-admin)

## 项目介绍

`电影时刻后端`基于Spring Boot+MyBatis+MySQL+Redis+RabbitMQ实现，使用Docker容器化部署。

## 技术栈

| 技术                                          | 说明          |
|---------------------------------------------|-------------|
| SpringBoot                                  | Web开发框架     |
| MyBatis                                     | ORM持久层框架    |
| MySQL                                       | 关系型数据库      |
| Redis                                       | 缓存中间件       |
| RabbitMQ                                    | 异步消息队列中间件   |
| MinIO                                       | 对象存储        |
| Redisson                                    | 分布式锁        |
| JWT                                         | 登录认证授权      |
| PageHelper                                  | MyBatis分页插件 |
| Commons Lang3/Commons Collections/FastJSON2 | Java通用工具类库  |
| Docker                                      | 容器化部署       |
| Maven                                       | 项目管理        |

## 开发工具

| 工具                            | 说明         |
|-------------------------------|------------|
| IDEA                          | IDE开发工具    |
| Navicat Premium 17            | MySQL客户端连接 |
| Another Redis Desktop Manager | Redis客户端连接 |
| Apifox                        | API接口调试    |

## 项目结构

```
movie-time
├── movie-time-boot -- 应用启动模块，包含所有业务模块的聚合和REST接口
├── movie-time-framework -- 框架核心模块，包含安全、Redis、消息队列等基础设施配置
├── movie-time-common -- 通用工具模块，包含公共类、常量、枚举、异常等
├── movie-time-movie -- 电影模块，电影及其影评相关接口
├── movie-time-cinema -- 影院模块，影院及其排场相关接口
├── movie-time-order -- 订单模块，订单相关接口
├── movie-time-user -- 用户模块，用户以及认证相关接口
```

## 开发环境

| 环境       | 版本                           |
|----------|------------------------------|
| JDK      | 21                           |
| MySQL    | 8.0.45                       |
| Redis    | 7.2.12                       |
| RabbitMQ | 4.2.2                        |
| MinIO    | RELEASE.2025-04-22T22-12-26Z |

## 环境搭建

- 容器部署：`docker compose up -d`
- MinIO管理界面添加存储桶`movie-time`并设置Access Policy为Public，获取access-key和secret-key填入`application-dev.yml`
- `movie-time`桶可以添加一张名为`default-avatar.png`的图片作为默认头像
- 创建名为`movie-time`的数据库，执行`sql/movie_time.sql`