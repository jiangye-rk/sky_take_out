# Sky Take Out - 外卖订餐平台

## 项目简介

Sky Take Out 是一个基于 Spring Boot 开发的外卖订餐平台后端系统，提供完整的商家管理和用户订餐功能。

## 技术栈

- **核心框架**: Spring Boot 2.7.3
- **持久层框架**: MyBatis 2.2.0
- **数据库连接池**: Druid 1.2.1
- **缓存**: Redis
- **安全认证**: JWT (JSON Web Token)
- **API文档**: Knife4j 3.0.2
- **分页插件**: PageHelper 1.3.0
- **对象存储**: 阿里云 OSS
- **支付**: 微信支付
- **工具库**: Lombok、Fastjson、Apache POI

## 项目结构

```
sky-take-out/
├── sky-common/          # 公共模块
│   ├── constant/        # 常量定义
│   ├── context/         # 上下文管理
│   ├── enumeration/     # 枚举类
│   ├── exception/       # 自定义异常
│   ├── json/            # JSON处理
│   ├── properties/      # 配置属性类
│   ├── result/          # 统一返回结果
│   └── utils/           # 工具类
├── sky-pojo/            # 实体类模块
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   └── vo/              # 视图对象
└── sky-server/          # 业务服务模块
    ├── annotation/      # 自定义注解
    ├── aspect/          # AOP切面
    ├── config/          # 配置类
    ├── controller/      # 控制器
    │   ├── admin/       # 管理端接口
    │   └── user/        # 用户端接口
    ├── handler/         # 异常处理器
    ├── interceptor/     # 拦截器
    ├── mapper/          # 数据访问层
    ├── service/         # 业务逻辑层
    └── resources/       # 配置文件
```

## 主要功能

### 管理端功能
- 员工管理：员工登录、信息维护、密码修改
- 分类管理：菜品分类和套餐分类的CRUD
- 菜品管理：菜品信息的增删改查、口味管理
- 套餐管理：套餐组合、起售停售
- 订单管理：订单查询、接单、拒单、取消
- 数据统计：营业额统计、订单统计、销量排行

### 用户端功能
- 用户登录：微信授权登录
- 菜品浏览：分类查看菜品和套餐
- 购物车：添加商品、清空购物车
- 订单管理：提交订单、支付、查看历史订单
- 地址管理：收货地址的增删改查

## 环境要求

- JDK 1.8+
- MySQL 5.7+
- Redis 5.0+
- Maven 3.6+

## 快速开始

### 1. 克隆项目

```bash
git clone <项目仓库地址>
cd sky-take-out
```

### 2. 配置数据库

创建数据库并执行初始化SQL脚本（如有），然后在 `sky-server/src/main/resources/application-dev.yml` 中配置数据库连接信息。

### 3. 配置Redis

确保Redis服务已启动，并在配置文件中配置Redis连接信息。

### 4. 配置其他服务

根据需要配置以下服务：
- 阿里云OSS（文件上传）
- 微信支付（支付功能）
- 微信小程序（用户登录）

### 5. 编译运行

```bash
# 编译项目
mvn clean package

# 运行项目
cd sky-server
mvn spring-boot:run
```

或者直接运行 `SkyApplication` 主类。

### 6. 访问接口文档

项目启动后，访问以下地址查看API文档：

```
http://localhost:8080/doc.html
```

## 配置文件说明

### application.yml
- 服务器端口：8080
- 文件上传路径：./uploads/
- 文件大小限制：10MB

### application-dev.yml
开发环境配置，包含：
- 数据库连接配置
- Redis连接配置
- JWT密钥配置
- 微信小程序配置
- 阿里云OSS配置

## 接口说明

### 管理端接口
- 基础路径：`/admin`
- 认证方式：JWT Token
- Token名称：token

### 用户端接口
- 基础路径：`/user`
- 认证方式：JWT Token
- Token名称：authentication

## 核心特性

1. **统一返回结果**：使用 Result 类封装接口返回数据
2. **全局异常处理**：GlobalExceptionHandler 统一处理业务异常
3. **JWT认证**：基于Token的无状态认证机制
4. **AOP自动填充**：自动填充创建时间、更新时间等字段
5. **分页查询**：使用PageHelper实现分页功能
6. **缓存支持**：基于Spring Cache和Redis的缓存机制

## 开发规范

- 实体类：使用Lombok简化代码，位于 `sky-pojo/entity`
- DTO：用于数据传输，位于 `sky-pojo/dto`
- VO：用于视图展示，位于 `sky-pojo/vo`
- 常量：统一定义在 `sky-common/constant`
- 异常：自定义业务异常位于 `sky-common/exception`

## 注意事项

1. 生产环境请修改JWT密钥
2. 敏感配置信息（如数据库密码、微信密钥）建议使用环境变量或配置中心管理
3. 文件上传路径在生产环境中需要配置为绝对路径

## 许可证

本项目仅供学习交流使用。
