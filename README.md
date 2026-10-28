<div align="center">

<h1>campus-admin · 学员管理系统</h1>

<p>面向<b>专门学校（矫治教育）</b>场景的开源学员管理系统</p>

<p>RuoYi-Vue 3.9.2 · Spring Boot 4.1.0 · Vue 3.5 · Element Plus 2.13 · 液态玻璃 UI（低配自动降级）</p>

<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F" alt="Spring Boot">
  <img src="https://img.shields.io/badge/JDK-17+-orange" alt="JDK 17">
  <img src="https://img.shields.io/badge/Vue-3.5-42b883" alt="Vue 3">
  <img src="https://img.shields.io/badge/license-MIT-blue" alt="MIT">
</p>

</div>

---

## 项目简介

一套为专门学校（矫治教育）日常教学管理设计的学员管理系统，在 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 3.9.2 基础上二次开发，覆盖**班级管理、学员档案、积分考核、调班记录**四大核心业务，开箱即用。

**界面采用液态玻璃（Liquid Glass）设计风格**：毛玻璃质感 + 圆角 + 扁平化，并内置低配设备自动降级策略——检测到性能不足的机器自动退回纯色背景与简化特效，保证老旧教学机上依然流畅。

## 功能清单

### 业务功能

| 模块 | 说明 |
| --- | --- |
| 班级管理 | 班级信息、负责老师、班级容量、停用/启用 |
| 学员档案 | 学籍信息（编号/姓名/性别/出生/入学日期）、家长联系人、在校/离校状态 |
| 积分管理 | 加分/扣分流水、变动后积分快照、事由备注，支持学员当前积分自动汇总 |
| 调班记录 | 学员调班全流程留痕（原班级 → 目标班级 + 事由） |

### 底座功能（继承自 RuoYi）

用户/角色/菜单/部门/岗位管理、字典参数、通知公告、操作与登录日志、在线用户、定时任务、代码生成、服务监控、缓存监控、连接池监控等。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Spring Boot 4.1.0 · MyBatis · PageHelper · Druid · Redis · JWT |
| 前端 | Vue 3.5 · Vite 6 · Element Plus 2.13 · Pinia 3 |
| 数据库 | MySQL 8.x |
| JDK | 17+ |

## 快速开始

### 环境要求

JDK 17+、Maven 3.8+、MySQL 8、Redis、Node.js 18+

### 1. 初始化数据库

```bash
mysql -u root -p < sql/ry_20260417.sql   # RuoYi 基础库（用户/菜单/字典）
mysql -u root -p < sql/quartz.sql        # 定时任务表
mysql -u root -p < sql/stu_menu.sql      # 业务菜单
mysql -u root -p < sql/stu_tables.sql    # 业务表结构
```

### 2. 修改后端配置

编辑 `ruoyi-admin/src/main/resources/application-druid.yml`：

- 数据库连接地址、用户名、密码

### 3. 启动后端

```bash
mvn clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

服务默认端口 `9090`（见 `application.yml`，与 RuoYi 默认 8080 不同）。

### 4. 启动前端

```bash
cd ruoyi-ui
npm install
npm run dev
```

开发模式访问 `http://localhost:80`（Vite 代理至后端 `9090`）。

### 5. 登录

默认账号 `admin / admin123`（首次部署后**立即修改**）。

## 生产部署安全清单

以下均为 RuoYi 开发默认值，**上线前必须修改**：

- [ ] `application.yml` → `token.secret`：更换为 32 位以上随机密钥
- [ ] `application-druid.yml` → 数据库账号密码（不要使用 root）
- [ ] `application-druid.yml` → Druid 控制台 `login-username / login-password`
- [ ] `application.yml` → `springdoc.swagger-ui.enabled` 改为 `false`
- [ ] `application.yml` → `ruoyi.profile` 上传目录路径
- [ ] MySQL/Redis 不要暴露公网，系统仅限内网部署使用

## 项目结构

```
campus-admin
├── ruoyi-admin          # 后端入口模块（端口 9090）
├── ruoyi-framework     # 框架核心
├── ruoyi-system        # 系统模块
├── ruoyi-quartz        # 定时任务
├── ruoyi-generator     # 代码生成
├── ruoyi-common        # 通用工具
├── ruoyi-ui            # 前端（Vue3 + Vite + Element Plus，液态玻璃风格）
├── sql                 # 初始化脚本（含业务表 stu_*）
├── doc                 # 参考文档
└── bin                 # 脚本
```

## 界面特性说明

- **液态玻璃 UI**：基于 `backdrop-filter` 的毛玻璃卡片、圆角、扁平化图标与动效
- **低配自动降级**（v2.1 起）：运行时检测设备渲染能力，低端机自动禁用模糊/透明特效并切换纯色背景，保证 60fps 操作体验
- **桌面/平板/大屏自适应**

## 许可与致谢

- 本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)（MIT License）二次开发，遵循并保留其开源协议，感谢若依团队的开源贡献。
- 业务代码部分同样以 **MIT** 协议开源，可自由用于个人与商业场景。

## Star History

如果这个项目对你有帮助，欢迎点一个 Star ⭐
