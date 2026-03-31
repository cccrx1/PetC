# AI宠物健康管理系统 - 产品需求文档

## Overview
- **Summary**: 一个轻量级的AI驱动宠物健康管理系统，集成了三个Dify工作流，支持Text2SQL自然语言查询、AI聊天助手和生活方案定制，提供PC+移动端响应式界面。
- **Purpose**: 为宠物主人提供一站式的宠物健康管理服务，包括健康监测、兽医咨询、喂养指南和行为训练等功能，通过AI技术提升用户体验。
- **Target Users**: 宠物主人、兽医、宠物爱好者

## Goals
- 构建完整的宠物健康管理系统，包括用户、宠物档案、健康监测、兽医咨询等核心模块
- 集成三个预配置的Dify工作流，实现自然语言查询、智能聊天和生活方案定制
- 提供PC+移动端响应式界面，确保良好的用户体验
- 确保数据安全和权限控制，实现用户数据隔离

## Non-Goals (Out of Scope)
- 不开发原生移动应用，仅通过响应式网页适配移动端
- 不实现实时视频咨询功能
- 不集成第三方支付系统
- 不开发复杂的数据分析功能，仅提供基础的数据趋势展示

## Background & Context
- 系统基于Spring Boot 3.x框架和MySQL 8.x数据库构建
- 集成三个预配置的Dify工作流，所有API配置均已预填
- 采用分层架构，前端仅作为交互窗口，核心逻辑由后端工作流处理

## Functional Requirements
- **FR-1**: 用户模块 - 实现账号注册登录、JWT身份认证、个人资料管理
- **FR-2**: 宠物档案模块 - 基于pets表构建数字化档案，包括基础信息和医疗档案
- **FR-3**: 健康监测模块 - 支持手动录入体重、饮食、运动数据，提供数据趋势图表展示
- **FR-4**: 兽医咨询模块 - 提供在线图文问诊、线下医院预约、电子病历自动归档
- **FR-5**: 喂养指南模块 - 实现品种适配的热量计算、个性化食谱建议、动态调整喂养方案
- **FR-6**: 行为训练模块 - 提供常见行为问题纠正方案、周训练计划制定
- **FR-7**: 社区资讯模块 - 实现精准科普推送、用户交流圈子
- **FR-8**: 首页数据与资讯模块 - 展示用户的宠物概览、最近的健康提醒、系统通知和养宠科普文章
- **FR-9**: Text2SQL功能 - 将自然语言转为MySQL SQL语句，自动注入user_id条件，防止越权
- **FR-10**: 聊天助手功能 - 驱动宠物健康助手的对话能力，支持多轮对话管理和意图识别
- **FR-11**: 生活方案定制功能 - 通过Dify工作流生成个性化的一周生活方案

## Non-Functional Requirements
- **NFR-1**: 性能 - 系统响应时间不超过2秒，页面加载时间不超过3秒
- **NFR-2**: 安全 - 采用JWT身份认证，确保数据传输加密，防止SQL注入和XSS攻击
- **NFR-3**: 可扩展性 - 系统架构应支持未来功能扩展和性能优化
- **NFR-4**: 可用性 - 系统可用性达到99.9%
- **NFR-5**: 兼容性 - 支持主流浏览器和移动设备

## Constraints
- **Technical**: 基于Spring Boot 3.x、MySQL 8.x、Tailwind CSS
- **Business**: 预配置的Dify工作流API，无需修改配置
- **Dependencies**: 依赖Dify工作流API和MySQL数据库

## Assumptions
- MySQL数据库已搭建，表结构已创建
- Dify工作流已配置完成，API可正常访问
- 系统运行环境已准备就绪

## Acceptance Criteria

### AC-1: 用户注册登录
- **Given**: 用户访问系统
- **When**: 用户输入注册信息并提交
- **Then**: 系统创建新用户账号并返回成功提示
- **Verification**: `programmatic`

### AC-2: 宠物档案管理
- **Given**: 用户已登录系统
- **When**: 用户添加宠物信息
- **Then**: 系统保存宠物信息并关联用户ID
- **Verification**: `programmatic`

### AC-3: 健康数据录入
- **Given**: 用户已添加宠物
- **When**: 用户录入宠物健康数据
- **Then**: 系统保存数据并展示趋势图表
- **Verification**: `programmatic`

### AC-4: 兽医咨询
- **Given**: 用户已登录系统
- **When**: 用户发起在线咨询
- **Then**: 系统记录咨询内容并提供回复
- **Verification**: `programmatic`

### AC-5: 喂养指南生成
- **Given**: 用户已添加宠物
- **When**: 用户请求喂养指南
- **Then**: 系统根据宠物信息生成个性化喂养方案
- **Verification**: `programmatic`

### AC-6: 生活方案定制
- **Given**: 用户已添加宠物
- **When**: 用户提交宠物信息并请求生活方案
- **Then**: 系统通过Dify工作流生成一周生活方案
- **Verification**: `programmatic`

### AC-7: Text2SQL功能
- **Given**: 用户已登录系统
- **When**: 用户输入自然语言查询
- **Then**: 系统生成SQL语句并返回查询结果
- **Verification**: `programmatic`

### AC-8: 聊天助手功能
- **Given**: 用户已登录系统
- **When**: 用户与聊天助手交互
- **Then**: 系统理解用户意图并提供相关回复
- **Verification**: `human-judgment`

### AC-9: 响应式界面
- **Given**: 用户使用不同设备访问系统
- **When**: 用户在PC端和移动端浏览系统
- **Then**: 系统自动适配不同屏幕尺寸
- **Verification**: `human-judgment`

### AC-10: 数据安全
- **Given**: 用户访问系统
- **When**: 用户进行敏感操作
- **Then**: 系统进行身份验证和权限检查
- **Verification**: `programmatic`

## Open Questions
- [ ] 数据库表结构的具体设计细节
- [ ] 前端页面的具体实现细节
- [ ] 系统部署和运维方案
- [ ] 数据备份和恢复策略