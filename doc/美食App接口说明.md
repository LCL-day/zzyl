# 美食分享平台 App 端接口说明

面向 C 端用户（首页、搜索、收藏、发布、我的）。所有接口都**需要登录**（请求头 `Authorization: Bearer {token}`），
统一前缀 `/food/app`，返回结构与若依其它接口一致：

- 列表：`{ "total": 6, "rows": [...], "code": 200, "msg": "查询成功" }`
- 单对象：`{ "code": 200, "msg": "操作成功", "data": {...} }`
- 分页参数：`pageNum`、`pageSize`

## 1. 首页与搜索

| 方法 | 路径 | 说明 | 参数 |
| --- | --- | --- | --- |
| GET | `/food/app/list` | 首页美食列表，支持搜索与分类筛选 | `title` 标题/简介关键字（可空）、`categoryId` 分类（可空）、`pageNum`、`pageSize` |
| GET | `/food/app/categories` | 分类列表（首页快筛） | 无 |
| GET | `/food/app/detail/{id}` | 美食详情 | 路径参数 `id` |

列表/详情返回字段（`FoodAppVo`）：

```json
{
  "id": 100,
  "categoryId": 100,
  "categoryName": "川菜",
  "title": "麻婆豆腐",
  "image": "",
  "description": "麻辣鲜香，下饭神器",
  "content": "经典川菜，豆腐嫩滑……",
  "likeCount": 2,
  "favoriteCount": 1,
  "commentCount": 2,
  "status": "0",
  "authorName": "admin",
  "createTime": "2026-09-15 03:38:51",
  "liked": true,
  "favorited": true
}
```

`liked` / `favorited` 是**当前登录用户**的互动状态，由子查询实时得出。

## 2. 点赞与收藏

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/food/app/like/{foodId}` | 点赞；已点赞则取消（切换） |
| POST | `/food/app/favorite/{foodId}` | 收藏；已收藏则取消（切换） |
| GET | `/food/app/favorites` | 我的收藏列表（分页） |
| DELETE | `/food/app/favorite/{foodId}` | 直接取消收藏（收藏页移除按钮） |

切换类接口统一返回：

```json
{ "code": 200, "msg": "操作成功", "data": { "active": true, "count": 3 } }
```

`active` 为切换后的状态，`count` 为切换后的总数，前端无需再查一次详情。

## 3. 发布

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/food/app/publish` | 发布美食，成功返回新内容 ID |
| GET | `/food/app/mine` | 我发布的内容（分页） |
| GET | `/food/app/mine/{foodId}` | 我发布的某条内容详情（编辑回显用，仅限本人） |
| PUT | `/food/app/mine/{foodId}` | 修改我发布的内容（仅限本人） |
| DELETE | `/food/app/mine/{foodId}` | 删除我发布的内容（仅限本人） |

**修改接口说明**（请求体与发布一致）：

- 可改字段：`title`、`categoryId`、`description`、`content`、`image`
- `image` 为空或不传时**保留原封面**（包括系统自动生成的文字封面），传值则替换为新封面
- `likeCount` / `favoriteCount` / `commentCount` / `status` / 创建信息**保持不变**
- 校验：标题必填且 ≤100 字、内容必填、分类必须存在；非本人操作返回「只能修改自己发布的内容」
- 前端：`我的` 页面每条发布都有「编辑」按钮，跳转到 `/app/publish?id=xxx`（发布页同时作为编辑页）

发布请求体：

```json
{
  "title": "麻婆豆腐",
  "categoryId": 100,
  "description": "一句话简介",
  "content": "详细做法",
  "image": "/profile/upload/2026/09/15/xxx.png"
}
```

**封面图片上传**：复用若依通用上传接口 `POST /common/upload`（表单字段 `file`），
返回的 `fileName` 即上面 `image` 字段要填的值（形如 `/profile/upload/2026/09/15/xxx.png`）。
图片访问地址为 `{VITE_APP_BASE_API}` + `fileName`，开发环境下 vite 已把 `/profile` 代理到后端。

上传目录由 `ruoyi.profile` 配置（当前为相对路径 `./uploadPath`）。

### 未上传封面时的自动封面（重要）

用户发布时若 `image` 为空，**后端会自动以「美食名称 + 分类名称」生成一张封面图**，
无需前端处理，返回的 `image` 已经是生成后的路径：

```
POST /food/app/publish
{ "title": "红烧排骨", "categoryId": 100, "content": "...", "image": "" }

→ 落库 image = /profile/upload/cover/cover_afab3c3d_20260915185853224.jpg
   文件位置   = {ruoyi.profile}/upload/cover/cover_afab3c3d_20260915185853224.jpg
```

生成规则（`com.zzyl.serve.util.CoverUtils`）：

| 项 | 说明 |
| --- | --- |
| 尺寸 | 640 × 420 |
| 内容 | 渐变底 + 装饰圆 + **美食名称**（超长自动缩字号） + **分类名称** |
| 配色 | 按分类名匹配：川菜=红、粤菜=青绿、鲁菜=棕、甜品=粉、小吃=橙、湘菜=深红、家常菜=绿、汤羹=蓝、面食=麦色、饮品=青蓝、素食=草绿、烘焙=紫；未匹配的分类按名称哈希生成稳定颜色 |
| 文件名 | `cover_<名称哈希>_<时间戳>.jpg`，重复发布不会互相覆盖 |
| 容错 | 生成失败只记录日志并返回空串，不阻断发布流程 |

> 说明：删除内容时不会连带删除已生成的封面文件（可能被其他内容引用），
> 如需清理可定期清理 `{ruoyi.profile}/upload/cover` 下不再被引用的文件。

## 4. 评论与回复

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/food/app/comments/{foodId}` | 某美食的评论列表（分页） |
| POST | `/food/app/comment` | 发表评论 / 回复评论 |
| DELETE | `/food/app/comment/{id}` | 删除我的评论（连同其下回复） |

请求体（`parentId` 为 0 表示直接评论，否则为被回复的评论 ID）：

```json
{ "foodId": 100, "content": "味道超赞", "parentId": 0 }
```

评论返回字段（`CommentVo`）：

```json
{
  "id": 100,
  "foodId": 100,
  "foodTitle": "麻婆豆腐",
  "foodImage": "",
  "userId": 2,
  "nickName": "若依",
  "avatar": "",
  "content": "味道超赞，下次还来！",
  "parentId": 0,
  "parentContent": null,
  "parentNickName": null,
  "createTime": "2026-09-15 03:38:51"
}
```

## 5. 注册与登录

| 方法 | 路径 | 匿名访问 | 说明 |
| --- | --- | --- | --- |
| GET | `/food/app/auth/registerEnabled` | 是 | 是否开放注册，前端据此显示「注册账号」入口 |
| POST | `/food/app/auth/register` | 是 | 注册新账号，成功后默认授予「普通用户」角色 |
| POST | `/login` | 是 | 登录。验证码已关闭，`code`/`uuid` 可省略 |
| GET | `/captchaImage` | 是 | 返回 `captchaEnabled:false`，前端据此隐藏验证码输入框 |

注册请求体：

```json
{
  "username": "foodie01",
  "nickName": "吃货一号",
  "password": "abc12345",
  "confirmPassword": "abc12345"
}
```

校验规则（后端统一校验，前端表单同步）：

| 字段 | 规则 |
| --- | --- |
| `username` | 必填，2–20 位**字母、数字或下划线**（不允许中文，避免与 `create_by` 匹配规则冲突） |
| `nickName` | 可空，留空默认取账号；最长 30 字符 |
| `password` | 必填，5–20 位 |
| `confirmPassword` | 与 `password` 一致 |

成功返回：`{ "code": 200, "msg": "注册成功，请登录", "userName": "...", "nickName": "..." }`；
失败返回 `code 500` 与具体原因（账号已存在 / 格式不合法 / 密码过短 / 两次不一致）。

**登录验证码开关**：由参数 `sys.account.captchaEnabled` 控制（见 `sql/food_app_account.sql`）。
关闭后 `/login` 不再校验验证码，`/captchaImage` 返回 `captchaEnabled:false`，前端自动隐藏验证码输入框。
该参数在 Redis 中缓存 30 天，修改后需**重启后端**或清理 `sys_config:*` 缓存才会立即生效。

## 6. 我的

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/food/app/profile` | 用户信息 + 统计 |
| GET | `/food/app/profile/comments?type=mine` | 我发表的评论 |
| GET | `/food/app/profile/comments?type=received` | 我收到的评论（他人评论我发布的内容，不含自己的） |

`/food/app/profile` 返回：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "userId": 1,
    "userName": "admin",
    "nickName": "lcl",
    "avatar": "",
    "phonenumber": "18218690953",
    "publishedCount": 0,
    "favoriteCount": 2,
    "commentCount": 1,
    "receivedCount": 6
  }
}
```

## 7. 数据表与归属口径（重要）

| 表 | 作用 |
| --- | --- |
| `food_category` | 美食分类 |
| `food_info` | 美食内容，`create_by` 存发布者**账号**（如 `admin`） |
| `food_like` / `food_favorite` | 点赞 / 收藏记录（`food_id` + 数字 `user_id`） |
| `food_comment` | 评论，`parent_id` 支持回复，`user_id` 存评论人**数字 ID** |

口径约定（**不要混用**）：

- **内容归属**（我发布的、我收到的评论）→ 按 `food_info.create_by = 当前账号` 匹配；
- **互动归属**（点赞、收藏、我的评论）→ 按 `user_id = 当前用户ID` 匹配。

> ⚠️ 踩坑记录：`create_by` 是 `varchar`，若拿它跟用户 ID（数字）比较，MySQL 会把 `'admin'` 转成数字 `0`，
> 结果永远匹配不上，表现为「发布后统计一直是 0」「我的发布列表为空」。因此归属判断一律按账号比较。
>
> 另外，发布时写入的 `create_by` 也必须是账号（`getUsername()`），不能写用户 ID。

## 8. 数据一致性

- 发布 / 删除内容时会同步维护 `comment_count`、`like_count`、`favorite_count`；
- 删除我的发布时，会**连带清理**该内容的评论（含回复）、点赞、收藏记录；
- 删除评论时会连带删除其下所有回复；
- 若历史数据出现脏记录，可执行 `sql/food_app_clean.sql` 清理孤立互动并重算计数。

## 9. 前端页面与路由

| 路由 | 页面 | 说明 |
| --- | --- | --- |
| `/login` | 登录 | 已关闭验证码；开放注册时显示「注册账号」入口 |
| `/register` | 注册 | 账号/昵称/密码/确认密码，注册成功后跳回登录并预填账号 |
| `/app/home` | 首页 | 搜索 + 分类快筛 + 卡片列表 + 触底加载 |
| `/app/favorite` | 收藏 | 我的收藏、取消收藏 |
| `/app/publish` | 发布 | 标题/分类/简介/封面上传/详细内容 |
| `/app/mine` | 我的 | 用户信息、统计、我的发布/我的评论/收到评论 |
| `/app/detail/:id` | 详情 | 图文详情、点赞收藏、评论与回复 |

底部导航（首页/收藏/发布/我的）由 `src/views/food/app/components/AppTabbar` 提供，详情页自动隐藏。
