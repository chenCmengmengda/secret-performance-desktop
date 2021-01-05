package cn.chenc.performs;

import cn.chenc.performs.state.SetupState;
import cn.chenc.performs.util.FileUtil;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * 　@description: 系统托盘
 * 　@author secret
 * 　@date 2020/12/26 4:47
 *
 */
public class MySystemTray {

    private static MySystemTray instance;
    private static MenuItem showItem;
    private static MenuItem exitItem;
    private static CheckboxMenuItem autoStartItem;//开机自启设置
    private static MenuItem setupItem;//打开设置窗口
    private static TrayIcon trayIcon;
    private static ActionListener showListener;
    private static ActionListener exitListener;
    private static ItemListener autoStartListener;
    private static ActionListener setupListener;
    private static MouseListener mouseListener;

    private static String autoStartPath=System.getProperty("user.home")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
    private static String exeName="secret-performance-desktop";//exe程序名


    //右小角,最小化.
    //菜单项(打开)中文乱码的问题是编译器的锅,如果使用IDEA,需要在Run-Edit Configuration在LoginApplication中的VM Options中添加-Dfile.encoding=GBK
    //如果使用Eclipse,需要右键Run as-选择Run Configuration,在第二栏Arguments选项中的VM Options中添加-Dfile.encoding=GBK
    //打包成exe安装后打开不会乱码
    static{
        //执行stage.close()方法,窗口不直接退出
        Platform.setImplicitExit(false);
        //菜单项(打开)中文乱码的问题是编译器的锅,如果使用IDEA,需要在Run-Edit Configuration在LoginApplication中的VM Options中添加-Dfile.encoding=GBK
        //如果使用Eclipse,需要右键Run as-选择Run Configuration,在第二栏Arguments选项中的VM Options中添加-Dfile.encoding=GBK
        showItem = new MenuItem("打开");
        //菜单项(退出)
        exitItem = new MenuItem("退出");
        autoStartItem = new CheckboxMenuItem("开机自动启动");
        setupItem = new MenuItem("设置");

        if(FileUtil.exists(autoStartPath+exeName+".lnk")){//如果存在开机启动快捷链接，则勾选
            autoStartItem.setState(true);
        }

        //此处不能选择ico格式的图片,要使用16*16的png格式的图片
        URL url = MySystemTray.class.getResource("/images/icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        //系统托盘图标
        trayIcon = new TrayIcon(image);
        //初始化监听事件(空)
        showListener = e -> Platform.runLater(() -> {});
        exitListener = e -> {};
        autoStartListener = e -> {};
        setupListener = e -> {};
        mouseListener = new MouseAdapter() {};
    }

    public static MySystemTray getInstance(Stage stage){
        if(instance == null){
            instance = new MySystemTray(stage);
        }
        return instance;
    }

    private MySystemTray(Stage stage){
        try {
            //检查系统是否支持托盘
            if (!SystemTray.isSupported()) {
                //系统托盘不支持
                return;
            }
            //设置图标尺寸自动适应
            trayIcon.setImageAutoSize(true);
            //系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            //弹出式菜单组件
            final PopupMenu popup = new PopupMenu();
            popup.add(showItem);
            popup.add(setupItem);
            popup.add(exitItem);
            popup.add(autoStartItem);
            trayIcon.setPopupMenu(popup);
            //鼠标移到系统托盘,会显示提示文本
            trayIcon.setToolTip("performance");
            listen(stage);
            tray.add(trayIcon);
        } catch (Exception e) {
            //系统托盘添加失败
            e.printStackTrace();
        }
    }

    /**
     * 更改系统托盘所监听的Stage
     */
    public void listen(Stage stage){
        //防止报空指针异常
        if(showListener == null || exitListener == null || autoStartListener == null || setupListener == null
                || mouseListener == null || showItem == null || exitItem == null || setupItem == null
                || autoStartItem==null || trayIcon == null){
            return;
        }
        //移除原来的事件
        showItem.removeActionListener(showListener);
        exitItem.removeActionListener(exitListener);
        autoStartItem.removeItemListener(autoStartListener);
        setupItem.removeActionListener(setupListener);
        trayIcon.removeMouseListener(mouseListener);
        //行为事件: 点击"打开"按钮,显示窗口
        showListener = e -> Platform.runLater(() -> showStage(stage));
        //行为事件: 点击"退出"按钮, 就退出系统
        exitListener = e -> {
            System.exit(0);
        };
        //行为事件: 开机自动启动
        autoStartListener = e -> {
            try {

                if(autoStartItem.getState()){//如果勾选开机自启
                    HashMap<String,String> hashMap = new HashMap<String,String>();

                    String exeRunPath = new File("").getCanonicalPath();

//                    System.out.println(exeRunPath);
                    hashMap.put(exeName,exeName+".exe");
                    CreateShortcut shortcut=new CreateShortcut(exeRunPath+File.separator,hashMap);
                    shortcut.createAutoStart(CreateShortcut.startup);
                } else {
                    CreateShortcut.delete(CreateShortcut.startup,exeName+".lnk");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };
        //行为事件，打开设置窗口
        setupListener = e -> {
            SetupState setupState=new SetupState();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //更新JavaFX的主线程的代码放在此处
                    try {
                        setupState.start(new Stage());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }

            });
        };
        //鼠标行为事件: 单机显示stage
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showStage(stage);
                }
            }
        };
        //给菜单项添加事件
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
        autoStartItem.addItemListener(autoStartListener);
        setupItem.addActionListener(setupListener);
        //给系统托盘添加鼠标响应事件
        trayIcon.addMouseListener(mouseListener);
    }

    /**
     * 关闭窗口
     */
    public void hide(Stage stage){
        Platform.runLater(() -> {
            //如果支持系统托盘,就隐藏到托盘,不支持就直接退出
            if (SystemTray.isSupported()) {
                //stage.hide()与stage.close()等价
                stage.hide();
            } else {
                System.exit(0);
            }
        });
    }

    /**
     * 点击系统托盘,显示界面(并且显示在最前面,将最小化的状态设为false)
     */
    private void showStage(Stage stage){
        //点击系统托盘,
        Platform.runLater(() -> {
            if(stage.isIconified()){ stage.setIconified(false);}
            if(!stage.isShowing()){ stage.show(); }
            stage.toFront();
        });
    }
}
