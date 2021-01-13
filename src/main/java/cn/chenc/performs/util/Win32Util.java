package cn.chenc.performs.util;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/11 20:10
 *
 */
public class Win32Util {

    private static WinDef.HWND desktopWorkerw;
    private static WinDef.HWND windowhWnd;

    /**
     * 将窗口置于windows系统桌面与图表之间
     * @param title
     */
    public static void setWinIconAfter(String title){
        //获取程序经理句柄
        WinDef.HWND hWnd2 = User32.INSTANCE.FindWindow("Progman",null);
        //发送消息给程序管理员
        WinDef.DWORDByReference result=new WinDef.DWORDByReference();
        WinDef.LRESULT r= User32.INSTANCE.SendMessageTimeout(hWnd2, 0x052C,
                new WinDef.WPARAM(),
                new WinDef.LPARAM(),
                User32.SMTO_NORMAL,
                1000,
                result
        );
        //获取到新创建的窗口的句柄
        WinDef.HWND[] workerw = {new WinDef.HWND(Pointer.NULL)};
        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hwnd, Pointer pointer) {
                WinDef.HWND h = User32.INSTANCE.FindWindowEx(hwnd, null,"SHELLDLL_DefView",null);
                if(h!=null){
                    workerw[0] =User32.INSTANCE.FindWindowEx(null,hwnd,"WorkerW",null);
                }
                return true;
            }
        }, Pointer.NULL);
        //在图标和墙纸之间绘制图形
        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null,title);    //使用标题寻找窗体
        WinDef.HDC dc=User32.INSTANCE.GetDC(hWnd);
        User32.INSTANCE.ReleaseDC(workerw[0],dc);
        //将Windows窗体放在桌面图标后面
        User32.INSTANCE.SetParent(hWnd, workerw[0]);
        windowhWnd=hWnd;
        desktopWorkerw=workerw[0];
    }

    /**
     * 将窗口移动到桌面图标上层
     */
    public static void setWinIconTop(String title){
//        WinDef.HWND hWnd = User32.INSTANCE.FindWindow(null,title);    //使用标题寻找窗体
        WinDef.HWND hWnd = User32.INSTANCE.FindWindowEx(desktopWorkerw,null,null,title);
        User32.INSTANCE.SetParent(hWnd,  User32.INSTANCE.GetDesktopWindow());
    }

}
