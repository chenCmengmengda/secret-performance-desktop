# secret-performance-desktop

![JDK](https://img.shields.io/badge/JDK-11-green.svg)
![Maven](https://img.shields.io/badge/Maven-3.6.1-green.svg)
[![license](https://img.shields.io/badge/license-GPL%20v3-yellow.svg)](https://gitee.com/secret_C/secretBlogBoot/blob/master/LICENSE)


#### 介绍
secret-performance-desktop(桌面性能监控)

#### 功能
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

#### 使用方法
下载可执行文件:

***目前版本:v1.5.0***

* [点击下载windows最新版本](https://gitee.com/SecretOpen/secret-performance-desktop/attach_files/581746/download/secret-performance-desktop-1.5.0.rar)

* [更多历史发行版本下载](https://gitee.com/SecretOpen/secret-performance-desktop/releases)


#### 核心依赖
|  组件   | 版本  |
|  ----  | ----  |
| javafx  | 15.0.1 |
| oshi-core  | 5.3.7 |

#### 截图
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img1.png)
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img2.png)
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img3.png)
![avatar](http://secretOpen.gitee.io/secret-performance-desktop/img4.png)

### 局限性
* 窗口置于底层不兼容部分系统

使用spy++查看窗口层级关系

![avatar](http://secretOpen.gitee.io/secret-performance-desktop/limit1.png)

如果为以上层级，则说明桌面和图标融合在了一起，这种情况无法兼容窗口置于图标下层。

**下图情况大部分可以实现（还是存在特殊不兼容情况，这里就不举例了）:**

![avatar](http://secretOpen.gitee.io/secret-performance-desktop/limit2.png)

实现原理:[Draw Behind Desktop Icons in Windows 8+](https://www.codeproject.com/articles/856020/draw-behind-desktop-icons-in-windows)

### 致谢

本项目有参考他人的代码，感谢各位大神的付出！
* 广大的开源爱好者
* [Oshi](https://github.com/oshi/oshi) OSHI是Java的免费的基于JNA的（本机）操作系统和硬件信息库。
