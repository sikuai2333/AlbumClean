# 项目开发规划 - 相册管理

**作者**: sikuai
**联系方式**: 2643927725#qq.com
**版本**: 1.0.0
**日期**: 2025-09-01

---

## 1. 项目概述

本项目旨在开发一个高效、美观、注重隐私的 Android 本地相册管理工具。应用将采用现代 Android 开发技术栈，以 Jetpack Compose 为核心构建 UI，实现流畅的用户体验和强大的照片分组管理功能。

## 2. 里程碑 (Milestones)

| 里程碑 | 目标 | 预计完成时间 |
| :--- | :--- | :--- |
| **M1: 项目初始化与基础架构** | - 搭建项目骨架<br>- 配置 Gradle、依赖注入 (Hilt)<br>- 设计并实现主题 (Theming)、路由 (Navigation)<br>- 完成空页面创建 | 2025-09-02 |
| **M2:核心数据层与权限** | - 实现 Room 数据库索引 (Entity, DAO)<br>- 封装 PhotoRepository，实现 MediaStore 数据读取<br>- 实现运行时权限请求 (Accompanist) | 2025-09-04 |
| **M3: 核心功能实现** | - 实现首页照片网格加载<br>- 实现沉浸式图片预览与手势操作<br>- 实现分组逻辑 (UseCase)<br>- 完成设置持久化 (DataStore) | 2025-09-07 |
| **M4: UI/UX 完善与测试** | - 实现确认删除页面<br>- 实现引导教程页<br>- 添加过渡动画与细节美化<br>- 编写单元测试和仪器测试 | 2025-09-10 |
| **M5: 发布准备** | - 全面自检 (`./gradlew clean build`)<br>- 准备截图和发布说明<br>- 代码审查和最终版本确定 | 2025-09-12 |

## 3. 功能拆解 (Feature Breakdown)

### 3.1. 数据层
- [ ] **Room 数据库**:
  - [ ] `PhotoEntity`: 定义照片/视频的表结构。
  - [ ] `PhotoDao`: 定义增、删、改、查接口。
- [ ] **Repository**:
  - [ ] `PhotoRepository`: 从 MediaStore 获取数据，与 Room 同步，对外提供统一数据源。
- [ ] **DataStore**:
  - [ ] `SettingsDataStore`: 持久化用户设置（主题、分组数量等）。

### 3.2. 领域层
- [ ] **Use Case**:
  - [ ] `GroupPhotosUseCase`: 根据设置将照片列表进行分组。

### 3.3. UI 层
- [ ] **导航 (Navigation)**:
  - [ ] `NavGraph`: 定义 `home`, `my`, `preview`, `confirm`, `guide` 5个页面的路由。
- [ ] **页面 (Screens)**:
  - [ ] **首页 (`HomeScreen`)**: `LazyVerticalStaggeredGrid` 展示缩略图，提供“开始”入口。
  - [ ] **我的 (`MyScreen`)**: 设置项列表，UI 与 ViewModel 绑定。
  - [ ] **预览页 (`PreviewScreen`)**: `HorizontalPager` + `pointerInput` 实现沉浸式手势滑动。
  - [ ] **确认页 (`ConfirmScreen`)**: `LazyRow` 分别展示保留和待删除的图片。
  - [ ] **引导页 (`GuideScreen`)**: `HorizontalPager` 实现功能引导。
- [ ] **主题 (`Theme`)**:
  - [ ] 支持动态主题色和深色/浅色模式切换。

### 3.4. 其他
- [ ] **依赖注入 (Hilt)**:
  - [ ] `AppModule`: 提供数据库、Repository、DataStore 等实例。
- [ ] **权限 (Permissions)**:
  - [ ] 使用 Accompanist `rememberPermissionState` 请求 `READ_MEDIA_IMAGES` 和 `READ_MEDIA_VIDEO`。

## 4. 测试计划 (Test Plan)

- **单元测试 (Unit Tests)**:
  - **ViewModel**: 测试业务逻辑、状态更新是否正确。
  - **Repository**: 测试数据同步和读取逻辑。
  - **UseCase**: 测试分组算法的正确性。
- **仪器测试 (Instrumentation Tests)**:
  - **Room DAO**: 测试数据库的增删改查操作是否符合预期。
  - **Navigation**: 测试页面跳转逻辑是否正确。
- **手动测试**:
  - **UI/UX**: 在不同尺寸、方向、主题下的显示效果。
  - **性能**: 大量图片加载时的流畅度和内存占用。
  - **兼容性**: 在不同 Android 版本上进行测试。
- **构建自检**:
  - 在每次功能开发完成后，执行 `./gradlew clean build` 确保项目可编译，无 Lint 错误。

---
*该计划由 Jules 制定。*
