# 旅游导航网站 API 文档（前端对接版）

## 1. 基础说明

- Base URL：`{VITE_API_BASE_URL}`，默认示例为 `http://localhost:8080/api`
- 数据格式：`application/json`
- 认证方式：`Authorization: Bearer <token>`
- 统一响应建议：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

---

## 2. 认证模块

### 2.1 用户注册
- **POST** `/auth/register`
- Request:
```json
{
  "username": "demo_user",
  "password": "123456"
}
```
- Response `data`:
```json
{
  "userId": 1
}
```

### 2.2 用户登录
- **POST** `/auth/login`
- Request:
```json
{
  "username": "demo_user",
  "password": "123456"
}
```
- Response `data`:
```json
{
  "token": "jwt-token",
  "user": {
    "id": 1,
    "username": "demo_user",
    "status": "正常"
  }
}
```

### 2.3 获取当前用户信息
- **GET** `/auth/profile`
- Header：`Authorization`

---

## 3. 景点模块（前台）

### 3.1 景点列表（分类/关键词/分页）
- **GET** `/scenic`
- Query:
  - `category`：分类（可选）
  - `keyword`：关键词（可选）
  - `page`：页码（可选）
  - `pageSize`：每页数量（可选）
- Response `data`:
```json
{
  "list": [
    {
      "id": 1,
      "name": "云海山国家公园",
      "category": "自然风光",
      "price": 120,
      "address": "xx路xx号",
      "image": "https://...",
      "description": "..."
    }
  ],
  "total": 100
}
```

### 3.2 景点详情
- **GET** `/scenic/{id}`

---

## 4. 行程（购物车）模块

### 4.1 获取我的行程
- **GET** `/itinerary`

### 4.2 添加景点到行程
- **POST** `/itinerary/items`
- Request:
```json
{
  "spotId": 1,
  "note": "下午游玩"
}
```

### 4.3 修改行程项（备注/顺序）
- **PUT** `/itinerary/items/{id}`
- Request:
```json
{
  "note": "先去拍照",
  "sort": 1
}
```

### 4.4 删除行程项
- **DELETE** `/itinerary/items/{id}`

### 4.5 清空行程
- **POST** `/itinerary/clear`

---

## 5. 订单模块

### 5.1 生成订单（由当前行程生成）
- **POST** `/orders`

### 5.2 我的订单列表
- **GET** `/orders/my`

---

## 6. 后台管理模块

### 6.1 管理员登录
- **POST** `/admin/login`
- Request:
```json
{
  "username": "admin",
  "password": "123456"
}
```

### 6.2 后台景点管理
- **POST** `/admin/scenic` 新增景点
- **PUT** `/admin/scenic/{id}` 修改景点
- **DELETE** `/admin/scenic/{id}` 删除景点

### 6.3 后台订单管理
- **GET** `/admin/orders` 所有订单
- **PATCH** `/admin/orders/{id}/status` 修改状态
- Request:
```json
{
  "status": "已完成"
}
```

### 6.4 后台用户管理
- **GET** `/admin/users` 用户列表
- **PATCH** `/admin/users/{id}/status` 用户状态控制
- Request:
```json
{
  "status": "禁用"
}
```

---

## 7. 状态码建议

- `200`：成功
- `400`：参数错误
- `401`：未登录/Token 无效
- `403`：无权限
- `404`：资源不存在
- `409`：用户名重复等冲突
- `500`：服务异常

---

## 8. 前端预留文件说明

- `src/api/http.js`：统一请求封装（Base URL、超时、错误处理）
- `src/api/index.js`：API 模块出口
- `src/api/modules/auth.js`：认证接口
- `src/api/modules/scenic.js`：景点接口
- `src/api/modules/itinerary.js`：行程接口
- `src/api/modules/orders.js`：订单接口
- `src/api/modules/admin.js`：后台接口

后续把页面或 store 中的本地 mock 逻辑替换为这些 API 即可完成真实后端对接。
