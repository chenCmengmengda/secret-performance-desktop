# secret-performance-desktop

![JDK](https://img.shields.io/badge/JDK-11-green.svg)
![Maven](https://img.shields.io/badge/Maven-3.6.1-green.svg)
[![license](https://img.shields.io/badge/license-GPL%20v3-yellow.svg)](https://gitee.com/secret_C/secretBlogBoot/blob/master/LICENSE)


### 介绍
基于javafx的桌面个性化工具

起初只想在做个桌面的cpu和内存监控工具，连续迭代几个版本之后，成了一个桌面的个性化工具。

### 功能
* cpu和内存的实时监控
* 开机自动启动
* 自定义logo
* 自定义主题颜色
* 鼠标拖拽
* 设置纵向或横向布局
* 桌面时钟
* 黑客帝国代码雨
* 雪花飘落
* 樱花飘落
* 窗口显示在桌面与图标之间
* 动态壁纸 wallpaper(支持mp4视频、web)

### 使用方法
下载可执行文件:

**目前版本:v1.7.0**

* [点击下载windows最新版本](https://gitee.com/SecretOpen/secret-performance-desktop/attach_files/593462/download/secret-performance-desktop-1.7.0.rar)

* [更多历史发行版本下载](https://gitee.com/SecretOpen/secret-performance-desktop/releases)

**更新说明**

由于没有在线自动更新功能，更新只能手动覆盖exe文件，config目录中的config.properties可以不用覆盖

***视频壁纸下载***

链接：https://pan.baidu.com/s/119_HUU7zLoJ3E9_-tghY4g 

提取码：9rb0 

![avatar](http://secretOpen.gitee.io/secret-performance-desktop/media1.png)


***web壁纸***

默认提供的web壁纸在wallpaper/web目录下


***自定义动态壁纸安装***

建议将自定义的壁纸放置在 ./wallpaper/对应类型 目录中,方便管理

### 核心依赖
|  组件   | 版本  |
|  ----  | ----  |
| javafx  | 15.0.1 |
| oshi-core  | 5.3.7 |
| jna  | 5.6.0 |


### 源码打包说明
maven 执行 package

使用exe4j 将jar包打为exe文件，exe4j配置在doc/exe4j目录下

### 截图
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img1.png)
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img2.png)

### 局限性
* 窗口置于底层不兼容部分系统

使用spy++查看窗口层级关系

![avatar](http://secretOpen.gitee.io/secret-performance-desktop/limit1.png)

如果为以上层级，则说明桌面和图标融合在了一起，这种情况无法兼容窗口置于图标下层。

**下图情况大部分可以实现（还是存在特殊不兼容情况，这里就不举例了）:**

![avatar](http://secretOpen.gitee.io/secret-performance-desktop/limit2.png)

实现原理:[Draw Behind Desktop Icons in Windows 8+](https://www.codeproject.com/articles/856020/draw-behind-desktop-icons-in-windows)

### 联系作者
<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1029693356&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:1029693356:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a>

qq:1029693356

### 致谢

本项目有参考他人的代码，感谢各位大神的付出！
* 广大的开源爱好者
* [Oshi](https://github.com/oshi/oshi) OSHI是Java的免费的基于JNA的（本机）操作系统和硬件信息库。
