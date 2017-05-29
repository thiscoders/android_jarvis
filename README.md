# Android手机卫士
##### &nbsp;&nbsp;&nbsp;&nbsp;APP名称：Jarvis

***

## 1.功能模块
#### 1. 手机防盗
#### 2. 通信卫士
#### 3. 软件管理
#### 4. 进程管理
#### 5. 流量统计
#### 6. 手机杀毒
#### 7. 缓存清理
#### 8. 高级工具
#### 9. 设置中心

***

## 2.自定义模块
##### &nbsp;&nbsp;&nbsp;&nbsp;暂时定于将这些模块挂载在高级工具模块下
#### 1. CPU信息
#### 2. 网络测速
#### 3. 局域网间谍(黑客初步)

***

## 3. 每日任务
**2017-05-18**
1. 手机防盗模块获取联系人是一个耗时操作，将其放入SyncTask中
2. 基本完成手机模块的编写
* 附加任务
    1. 手机防盗模块可以加上图片动态效果

## 4. 任务进度
**2017-05-19**
1. 完成了第三天的内容

**2017-05-22**
1. 删除了Splash界面，添加了About界面
2. 修复了自动更新

**2017-05-23**
1. 主要完成了Android N无法监听短信广播的问题
    * 解决方案： 既然无法监听短信广播，那么改为监听短信数据库的变化


## 5. 有待了解
1. Android联系人数据库的设计，如何风骚的获取联系人
    * 如何判断联系人是否删除
    * 联系人有多个号码的获取号码的方式
    
## 6. 待完成任务
1. 为整个APP添加手势监听