package cn.chenc.performs.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2020/12/29 8:47
 *
 */
public class ImageUtil {

    public static Image backGroundImg;

    /**
     * @Description javafx中图片透明度设置。
     *
     * @param image 需要 改变透明度的图片对象
     * @param opacity 透明度，介于0-1之间
     * @return 新的可写的image对象
     * @author LIu Mingyao
     */
    public static WritableImage imgOpacity(Image image, double opacity) {

        if (opacity < 0 || opacity > 1) {
            throw new RuntimeException("透明度需要介于0-1之间,请重新设置透明度!");
        }

        // 获取PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // 创建WritableImage
        WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        double imgHeight = image.getHeight();
        double tempHeight = imgHeight % 3;

        // 将原来的单个线程改变透明度(下面注释的代码)改为了三个线程，解决了大图片更改透明度缓慢的问题
        FutureTask<Boolean> futureTask1 = new FutureTask<Boolean>(() -> {
            for (int readY = 0; readY < tempHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    if(color.getOpacity() == 0){//如果是透明的，就不改变
                        continue;
                    }
                    // 最后一个参数是透明设置。需要设置透明不能改变原来的，只能重新创建对象赋值，
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            opacity);
//					c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }
            return true;
        });
        new Thread(futureTask1, "第一个透明度渲染线程").start();

        FutureTask<Boolean> futureTask2 = new FutureTask<Boolean>(() -> {
            for (int readY = (int) tempHeight; readY < 2 * tempHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    if(color.getOpacity() == 0){//如果是透明的，就不改变
                        continue;
                    }
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            opacity);
//					c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }

            return true;
        });
        new Thread(futureTask2, "第二个透明度渲染线程").start();
        FutureTask<Boolean> futureTask3 = new FutureTask<Boolean>(() -> {
            for (int readY = (int) (2 * tempHeight); readY < imgHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    if( color.getOpacity() == 0){//如果是rgb为0且透明的，就不改变
                        continue;
                    }
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            opacity);
//                    c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }

            return true;
        });
        new Thread(futureTask3, "第三个透明度渲染线程").start();

		 // 单线程更改透明度，得到每个坐标像素点的color，并重新设值，赋予透明度，最后将新color设给新的image对象(wImage的pixelWriter)
//		 for (int readY = 0; readY < image.getHeight(); readY++) {
//		 	for (int readX = 0; readX < image.getWidth(); readX++) {
//				 Color color = pixelReader.getColor(readX, readY);
//				 System.out.println("\nPixel color at coordinates (" + readX + "," + readY + ") "
//				 + color.toString());
//				 System.out.println("R = " + color.getRed());
//				 System.out.println("G = " + color.getGreen());
//				 System.out.println("B = " + color.getBlue());
//				 System.out.println("Opacity = " + color.getOpacity());
//				 System.out.println("Saturation = " + color.getSaturation());
//
//				 // 现在写入一个更为明亮的颜色到PixelWriter中
////				  color = color.brighter();
//
//				 // 更暗
//				color=color.darker();
//
//                 // 最后一个参数是透明设置。需要设置透明不能改变原来的，只能重新创建对象赋值，
//                 Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
//
//                 pixelWriter.setColor(readX, readY, c1.brighter());
//		 	}
//		 }

        //这部分代码可以自主选用。用了可以保证全部图片全部刷新完再展示，不然图片是先渲染上部分，再是中下部分
		try {
			// 等待三个线程全部执行完毕
			if (futureTask1.get() && futureTask2.get() && futureTask3.get()) {
//				backGroundImg = wImage;
                return wImage;
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return wImage;
    }

}
