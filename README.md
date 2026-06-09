# 宁波自然景观信息展示网站

## 技术栈
- 前端：Vue 3 + Vite + Vue Router
- 后端：Spring Boot 2.7 + MyBatis
- 数据库：MySQL 8.0
- 认证：JWT

## 启动方式
1. 导入 `travel-theme-backend/sql/travel_web.sql` 到 MySQL
2. 启动后端：`cd travel-theme-backend && mvn spring-boot:run`
3. 启动前端：`cd Web && npm install && npm run dev`

## 功能
- 浏览8个宁波真实自然/人文景观
- 用户注册/登录（JWT认证）
- 行程规划（加入/排序/备注）
- 订单生成与管理
- 管理员后台（景点CRUD/订单管理/用户管理）
