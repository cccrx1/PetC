# 宠物健康管理系统 - API接口文档

## 1. 认证接口

### 1.1 注册接口
- **URL**: `/api/auth/register`
- **方法**: `POST`
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | name | String | 是 | 用户名 |
  | username | String | 是 | 登录用户名 |
  | email | String | 是 | 邮箱 |
  | password | String | 是 | 密码 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "User registered successfully",
    "data": {
      "id": 1,
      "name": "测试用户",
      "username": "testuser",
      "email": "test@example.com"
    }
  }
  ```

### 1.2 登录接口
- **URL**: `/api/auth/login`
- **方法**: `POST`
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | username | String | 是 | 登录用户名 |
  | password | String | 是 | 密码 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Login successful",
    "data": {
      "token": "eyJhbGciOiJIUzI1NiJ9...",
      "user": {
        "id": 1,
        "name": "测试用户",
        "username": "testuser",
        "email": "test@example.com"
      }
    }
  }
  ```

### 1.3 获取用户资料接口
- **URL**: `/api/auth/profile`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Profile retrieved successfully",
    "data": {
      "id": 1,
      "name": "测试用户",
      "username": "testuser",
      "email": "test@example.com"
    }
  }
  ```

### 1.4 更新用户资料接口
- **URL**: `/api/auth/profile`
- **方法**: `PUT`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | name | String | 否 | 用户名 |
  | email | String | 否 | 邮箱 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Profile updated successfully",
    "data": {
      "id": 1,
      "name": "更新后的用户名",
      "username": "testuser",
      "email": "updated@example.com"
    }
  }
  ```

### 1.5 修改密码接口
- **URL**: `/api/auth/password`
- **方法**: `PUT`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | currentPassword | String | 是 | 当前密码 |
  | newPassword | String | 是 | 新密码 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Password updated successfully",
    "data": null
  }
  ```

## 2. 宠物接口

### 2.1 获取宠物列表接口
- **URL**: `/api/pets`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Pets retrieved successfully",
    "data": [
      {
        "id": 1,
        "name": "旺财",
        "breed": "金毛",
        "age": 2,
        "weight": 25.5,
        "gender": "公",
        "userId": 1
      }
    ]
  }
  ```

### 2.2 添加宠物接口
- **URL**: `/api/pets`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | name | String | 是 | 宠物名称 |
  | breed | String | 是 | 宠物品种 |
  | age | Integer | 是 | 宠物年龄 |
  | weight | Double | 是 | 宠物体重 |
  | gender | String | 是 | 宠物性别 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Pet added successfully",
    "data": {
      "id": 1,
      "name": "旺财",
      "breed": "金毛",
      "age": 2,
      "weight": 25.5,
      "gender": "公",
      "userId": 1
    }
  }
  ```

### 2.3 获取宠物详情接口
- **URL**: `/api/pets/{id}`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Pet retrieved successfully",
    "data": {
      "id": 1,
      "name": "旺财",
      "breed": "金毛",
      "age": 2,
      "weight": 25.5,
      "gender": "公",
      "userId": 1
    }
  }
  ```

### 2.4 更新宠物信息接口
- **URL**: `/api/pets/{id}`
- **方法**: `PUT`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 宠物ID |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | name | String | 否 | 宠物名称 |
  | breed | String | 否 | 宠物品种 |
  | age | Integer | 否 | 宠物年龄 |
  | weight | Double | 否 | 宠物体重 |
  | gender | String | 否 | 宠物性别 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Pet updated successfully",
    "data": {
      "id": 1,
      "name": "旺财",
      "breed": "金毛",
      "age": 3,
      "weight": 26.5,
      "gender": "公",
      "userId": 1
    }
  }
  ```

### 2.5 删除宠物接口
- **URL**: `/api/pets/{id}`
- **方法**: `DELETE`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Pet deleted successfully",
    "data": null
  }
  ```

## 3. 健康记录接口

### 3.1 获取健康记录列表接口
- **URL**: `/api/health-records`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **查询参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | petId | Long | 否 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Health records retrieved successfully",
    "data": [
      {
        "id": 1,
        "petId": 1,
        "date": "2023-04-01",
        "weight": 25.5,
        "temperature": 38.5,
        "symptoms": "正常",
        "notes": "无"
      }
    ]
  }
  ```

### 3.2 添加健康记录接口
- **URL**: `/api/health-records`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | petId | Long | 是 | 宠物ID |
  | date | String | 是 | 记录日期 (YYYY-MM-DD) |
  | weight | Double | 是 | 体重 |
  | temperature | Double | 是 | 体温 |
  | symptoms | String | 否 | 症状 |
  | notes | String | 否 | 备注 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Health record added successfully",
    "data": {
      "id": 1,
      "petId": 1,
      "date": "2023-04-01",
      "weight": 25.5,
      "temperature": 38.5,
      "symptoms": "正常",
      "notes": "无"
    }
  }
  ```

### 3.3 获取健康记录详情接口
- **URL**: `/api/health-records/{id}`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 记录ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Health record retrieved successfully",
    "data": {
      "id": 1,
      "petId": 1,
      "date": "2023-04-01",
      "weight": 25.5,
      "temperature": 38.5,
      "symptoms": "正常",
      "notes": "无"
    }
  }
  ```

### 3.4 更新健康记录接口
- **URL**: `/api/health-records/{id}`
- **方法**: `PUT`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 记录ID |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | date | String | 否 | 记录日期 (YYYY-MM-DD) |
  | weight | Double | 否 | 体重 |
  | temperature | Double | 否 | 体温 |
  | symptoms | String | 否 | 症状 |
  | notes | String | 否 | 备注 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Health record updated successfully",
    "data": {
      "id": 1,
      "petId": 1,
      "date": "2023-04-01",
      "weight": 26.0,
      "temperature": 38.5,
      "symptoms": "正常",
      "notes": "无"
    }
  }
  ```

### 3.5 删除健康记录接口
- **URL**: `/api/health-records/{id}`
- **方法**: `DELETE`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 记录ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Health record deleted successfully",
    "data": null
  }
  ```

## 4. 咨询接口

### 4.1 获取咨询列表接口
- **URL**: `/api/consultations`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Consultations retrieved successfully",
    "data": [
      {
        "id": 1,
        "userId": 1,
        "petId": 1,
        "type": "健康咨询",
        "symptoms": "食欲不振",
        "status": "待处理",
        "response": null
      }
    ]
  }
  ```

### 4.2 提交咨询接口
- **URL**: `/api/consultations`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | petId | Long | 是 | 宠物ID |
  | type | String | 是 | 咨询类型 |
  | symptoms | String | 是 | 症状描述 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Consultation submitted successfully",
    "data": {
      "id": 1,
      "userId": 1,
      "petId": 1,
      "type": "健康咨询",
      "symptoms": "食欲不振",
      "status": "待处理",
      "response": null
    }
  }
  ```

### 4.3 获取咨询详情接口
- **URL**: `/api/consultations/{id}`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 咨询ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Consultation retrieved successfully",
    "data": {
      "id": 1,
      "userId": 1,
      "petId": 1,
      "type": "健康咨询",
      "symptoms": "食欲不振",
      "status": "已处理",
      "response": "建议调整饮食，增加运动量"
    }
  }
  ```

### 4.4 更新咨询状态接口
- **URL**: `/api/consultations/{id}`
- **方法**: `PUT`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 咨询ID |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | status | String | 是 | 状态 |
  | response | String | 否 | 回复内容 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Consultation updated successfully",
    "data": {
      "id": 1,
      "userId": 1,
      "petId": 1,
      "type": "健康咨询",
      "symptoms": "食欲不振",
      "status": "已处理",
      "response": "建议调整饮食，增加运动量"
    }
  }
  ```

### 4.5 删除咨询接口
- **URL**: `/api/consultations/{id}`
- **方法**: `DELETE`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 咨询ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Consultation deleted successfully",
    "data": null
  }
  ```

## 5. 文章接口

### 5.1 获取文章列表接口
- **URL**: `/api/articles`
- **方法**: `GET`
- **查询参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | page | Integer | 否 | 页码 |
  | size | Integer | 否 | 每页数量 |
  | category | String | 否 | 分类 |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Articles retrieved successfully",
    "data": {
      "content": [
        {
          "id": 1,
          "title": "如何照顾金毛犬",
          "content": "金毛犬是一种友好、聪明的犬种...",
          "author": "宠物专家",
          "category": "狗狗养护",
          "likes": 100,
          "collects": 50
        }
      ],
      "totalElements": 1,
      "totalPages": 1,
      "number": 0,
      "size": 10
    }
  }
  ```

### 5.2 获取文章详情接口
- **URL**: `/api/articles/{id}`
- **方法**: `GET`
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 文章ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Article retrieved successfully",
    "data": {
      "id": 1,
      "title": "如何照顾金毛犬",
      "content": "金毛犬是一种友好、聪明的犬种...",
      "author": "宠物专家",
      "category": "狗狗养护",
      "likes": 100,
      "collects": 50
    }
  }
  ```

### 5.3 点赞文章接口
- **URL**: `/api/articles/{id}/like`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 文章ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Article liked successfully",
    "data": {
      "id": 1,
      "likes": 101
    }
  }
  ```

### 5.4 收藏文章接口
- **URL**: `/api/articles/{id}/collect`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **路径参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | id | Long | 是 | 文章ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Article collected successfully",
    "data": {
      "id": 1,
      "collects": 51
    }
  }
  ```

## 6. AI聊天接口

### 6.1 发送聊天消息接口
- **URL**: `/api/chat`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | message | String | 是 | 消息内容 |
  | petId | Long | 否 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Chat message processed successfully",
    "data": {
      "response": "您好！我是宠物健康助手，有什么可以帮助您的吗？"
    }
  }
  ```

## 7. 生活方案接口

### 7.1 生成生活方案接口
- **URL**: `/api/life-plan/generate`
- **方法**: `POST`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **请求参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | petId | Long | 是 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Life plan generated successfully",
    "data": {
      "id": 1,
      "petId": 1,
      "content": "根据您的宠物情况，我们为您制定了以下生活方案...",
      "createdAt": "2023-04-01T12:00:00"
    }
  }
  ```

### 7.2 获取生活方案列表接口
- **URL**: `/api/life-plan`
- **方法**: `GET`
- **请求头**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | Authorization | String | 是 | Bearer {token} |
- **查询参数**:
  | 参数名 | 类型 | 必需 | 描述 |
  |-------|------|------|------|
  | petId | Long | 否 | 宠物ID |
- **响应**:
  ```json
  {
    "code": 200,
    "message": "Life plans retrieved successfully",
    "data": [
      {
        "id": 1,
        "petId": 1,
        "content": "根据您的宠物情况，我们为您制定了以下生活方案...",
        "createdAt": "2023-04-01T12:00:00"
      }
    ]
  }
  ```

## 8. 响应格式

所有API接口的响应格式统一为：

```json
{
  "code": 200,           // 状态码
  "message": "成功",     // 消息
  "data": {}             // 数据
}
```

### 状态码说明
- **200**: 成功
- **400**: 请求参数错误
- **401**: 未授权
- **403**: 禁止访问
- **404**: 资源不存在
- **500**: 服务器内部错误

## 9. 认证流程

1. 用户注册/登录获取JWT令牌
2. 在后续请求的Authorization头中携带令牌
3. 服务器验证令牌的有效性
4. 验证通过后处理请求，返回响应

## 10. 注意事项

- 所有需要认证的接口都需要在请求头中携带有效的JWT令牌
- 请求参数需要符合接口定义的格式
- 响应数据结构可能会根据实际情况有所调整
- 接口版本可能会在后续更新中有所变更