# 宠物健康管理系统

## 项目概述

宠物健康管理系统是一个基于Spring Boot和HTML/CSS/JavaScript的全栈应用，旨在帮助宠物主人管理宠物的健康信息、提供在线咨询服务、生成健康方案等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.x
- **认证**: JWT (JSON Web Token)
- **构建工具**: Maven
- **ORM**: Hibernate

### 前端
- **HTML5**
- **CSS3** (Tailwind CSS)
- **JavaScript** (原生)
- **响应式设计**: 支持桌面和移动设备

## 项目结构

### 后端目录结构
```
src/
└── main/
    ├── java/com/pethealth/system/
    │   ├── config/          # 配置类
    │   ├── controller/      # 控制器
    │   ├── entity/          # 实体类
    │   ├── repository/      # 数据访问层
    │   ├── service/         # 业务逻辑层
    │   ├── utils/           # 工具类
    │   └── PetHealthSystemApplication.java  # 应用主类
    └── resources/
        ├── application.properties  # 应用配置
        └── static/              # 静态资源
            └── frontend/        # 前端文件
```

### 前端目录结构
```
frontend/
├── index.html          # 首页
├── login.html          # 登录页
├── pets.html           # 宠物管理页
├── consultation.html   # 咨询服务页
├── profile.html        # 个人中心页
├── health.html         # 健康记录页
├── life-plan.html      # 生活方案页
└── src/
    └── js/
        └── main.js     # 前端主脚本
```

## 核心功能

### 1. 用户认证
- **注册**: 创建新用户账号
- **登录**: 使用邮箱和密码登录
- **JWT认证**: 生成和验证JWT令牌
- **个人资料管理**: 修改个人信息和密码

### 2. 宠物管理
- **添加宠物**: 录入宠物基本信息
- **宠物列表**: 查看所有宠物
- **宠物详情**: 查看宠物详细信息
- **编辑宠物**: 修改宠物信息
- **删除宠物**: 移除宠物

### 3. 健康记录
- **添加健康记录**: 记录宠物健康状况
- **健康记录列表**: 查看所有健康记录
- **健康记录详情**: 查看详细健康信息
- **编辑健康记录**: 修改健康记录
- **删除健康记录**: 移除健康记录

### 4. 在线咨询
- **提交咨询**: 提交宠物健康问题
- **咨询列表**: 查看所有咨询
- **咨询详情**: 查看咨询状态和回复
- **咨询状态**: 跟踪咨询处理进度

### 5. 健康方案
- **生成方案**: 根据宠物健康状况生成健康方案
- **方案列表**: 查看所有生成的方案
- **方案详情**: 查看方案详细内容

### 6. 文章资讯
- **文章列表**: 查看宠物相关文章
- **文章详情**: 阅读文章内容
- **文章点赞**: 对文章进行点赞
- **文章收藏**: 收藏感兴趣的文章

## API 接口

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/profile` - 获取用户资料
- `PUT /api/auth/profile` - 更新用户资料
- `PUT /api/auth/password` - 修改密码

### 宠物接口
- `GET /api/pets` - 获取宠物列表
- `POST /api/pets` - 添加宠物
- `GET /api/pets/{id}` - 获取宠物详情
- `PUT /api/pets/{id}` - 更新宠物信息
- `DELETE /api/pets/{id}` - 删除宠物

### 健康记录接口
- `GET /api/health-records` - 获取健康记录列表
- `POST /api/health-records` - 添加健康记录
- `GET /api/health-records/{id}` - 获取健康记录详情
- `PUT /api/health-records/{id}` - 更新健康记录
- `DELETE /api/health-records/{id}` - 删除健康记录

### 咨询接口
- `GET /api/consultations` - 获取咨询列表
- `POST /api/consultations` - 提交咨询
- `GET /api/consultations/{id}` - 获取咨询详情
- `PUT /api/consultations/{id}` - 更新咨询状态
- `DELETE /api/consultations/{id}` - 删除咨询

### 文章接口
- `GET /api/articles` - 获取文章列表
- `GET /api/articles/{id}` - 获取文章详情
- `POST /api/articles/{id}/like` - 点赞文章
- `POST /api/articles/{id}/collect` - 收藏文章

## 数据库设计

### 主要表结构

#### users 表
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | BIGINT | PRIMARY KEY | 用户ID |
| name | VARCHAR(100) | NOT NULL | 用户名 |
| username | VARCHAR(100) | UNIQUE NOT NULL | 登录用户名 |
| email | VARCHAR(100) | UNIQUE NOT NULL | 邮箱 |
| password | VARCHAR(255) | NOT NULL | 密码 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### pets 表
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | BIGINT | PRIMARY KEY | 宠物ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| name | VARCHAR(100) | NOT NULL | 宠物名称 |
| breed | VARCHAR(100) | NOT NULL | 宠物品种 |
| age | INT | NOT NULL | 宠物年龄 |
| weight | DOUBLE | NOT NULL | 宠物体重 |
| gender | VARCHAR(10) | NOT NULL | 宠物性别 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### health_records 表
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | BIGINT | PRIMARY KEY | 记录ID |
| pet_id | BIGINT | FOREIGN KEY | 宠物ID |
| date | DATE | NOT NULL | 记录日期 |
| weight | DOUBLE | NOT NULL | 体重 |
| temperature | DOUBLE | NOT NULL | 体温 |
| symptoms | TEXT | | 症状 |
| notes | TEXT | | 备注 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### consultations 表
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | BIGINT | PRIMARY KEY | 咨询ID |
| user_id | BIGINT | FOREIGN KEY | 用户ID |
| pet_id | BIGINT | FOREIGN KEY | 宠物ID |
| type | VARCHAR(50) | NOT NULL | 咨询类型 |
| symptoms | TEXT | NOT NULL | 症状描述 |
| status | VARCHAR(20) | NOT NULL | 状态 |
| response | TEXT | | 回复内容 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

#### articles 表
| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | BIGINT | PRIMARY KEY | 文章ID |
| title | VARCHAR(255) | NOT NULL | 文章标题 |
| content | TEXT | NOT NULL | 文章内容 |
| author | VARCHAR(100) | NOT NULL | 作者 |
| category | VARCHAR(50) | NOT NULL | 分类 |
| likes | INT | DEFAULT 0 | 点赞数 |
| collects | INT | DEFAULT 0 | 收藏数 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 更新时间 |

## 部署指南

### 本地开发环境

#### 1. 环境准备
- JDK 17+
- MySQL 8.x
- Maven 3.6+

#### 2. 数据库配置
1. 创建数据库 `pet_health_system`
2. 修改 `application.properties` 中的数据库连接信息

#### 3. 运行项目
1. 编译项目：`mvn clean package`
2. 运行项目：`mvn spring-boot:run`
3. 访问前端：`http://localhost:8080/api/index.html`

### 生产环境部署

#### 1. 构建项目
```bash
mvn clean package -DskipTests
```

#### 2. 部署到服务器
1. 将生成的 `pet-health-system-1.0.0.jar` 文件上传到服务器
2. 运行：`java -jar pet-health-system-1.0.0.jar`

#### 3. 配置反向代理（可选）
使用 Nginx 配置反向代理，将域名指向应用

## 功能测试

### 测试步骤

1. **用户注册**
   - 访问 `http://localhost:8080/api/login.html`
   - 填写注册信息，点击注册按钮
   - 验证注册成功并跳转到登录页

2. **用户登录**
   - 输入注册的邮箱和密码
   - 点击登录按钮
   - 验证登录成功并跳转到首页

3. **宠物管理**
   - 点击导航栏中的"我的宠物"
   - 点击"添加宠物"按钮
   - 填写宠物信息并保存
   - 验证宠物列表中显示新添加的宠物

4. **健康记录**
   - 点击快捷入口中的"健康监测"
   - 点击"添加健康记录"按钮
   - 填写健康信息并保存
   - 验证健康记录列表中显示新添加的记录

5. **在线咨询**
   - 点击导航栏中的"咨询"
   - 点击"提交咨询"按钮
   - 填写咨询信息并提交
   - 验证咨询列表中显示新提交的咨询

6. **健康方案**
   - 点击快捷入口中的"生成方案"
   - 选择宠物并点击"生成方案"按钮
   - 验证方案生成成功并显示详细内容

7. **文章资讯**
   - 首页浏览文章列表
   - 点击文章标题查看详情
   - 点击点赞和收藏按钮
   - 验证点赞和收藏功能正常

## 常见问题与解决方案

### 1. 端口冲突
**问题**: 启动时提示端口8080被占用
**解决方案**: 终止占用端口的进程或修改 `application.properties` 中的端口配置

### 2. 数据库连接失败
**问题**: 启动时提示数据库连接失败
**解决方案**: 检查数据库服务是否运行，数据库连接信息是否正确

### 3. 前端页面无响应
**问题**: 点击页面元素无反应
**解决方案**: 检查浏览器控制台是否有JavaScript错误，确保JavaScript文件正确加载

### 4. 登录失败
**问题**: 登录时提示"登录失败"
**解决方案**: 检查用户名和密码是否正确，检查后端日志是否有错误信息

### 5. 权限问题
**问题**: 访问某些接口时提示"401 Unauthorized"
**解决方案**: 确保已登录并携带有效的JWT令牌，检查接口权限配置

## 项目优势

1. **全栈技术栈**: 整合了Spring Boot后端和HTML/CSS/JavaScript前端
2. **响应式设计**: 支持桌面和移动设备
3. **完整的功能体系**: 涵盖宠物健康管理的各个方面
4. **安全认证**: 使用JWT进行身份验证
5. **模块化设计**: 代码结构清晰，易于维护和扩展
6. **用户友好界面**: 简洁美观的界面设计，良好的用户体验

## 未来扩展方向

1. **添加宠物社区功能**: 允许宠物主人之间交流和分享
2. **集成第三方API**: 接入宠物医疗服务、宠物用品购买等
3. **添加宠物定位功能**: 集成GPS定位，防止宠物走失
4. **开发移动应用**: 开发iOS和Android原生应用
5. **添加智能健康监测**: 集成智能设备，实时监测宠物健康状况
6. **优化数据分析**: 提供更详细的宠物健康分析报告

## 技术支持

如有任何问题或建议，请联系技术支持团队：
- 邮箱: support@pethealthsystem.com
- 电话: 400-123-4567
- 地址: 北京市朝阳区宠物健康大厦

---

**版本信息**: v1.0.0
**发布日期**: 2026-04-09
**开发者**: Pet Health System Team