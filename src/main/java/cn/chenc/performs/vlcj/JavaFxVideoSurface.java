package cn.chenc.performs.vlcj;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

/**
 * 　@description: TODO
 * 　@author secret
 * 　@date 2021/1/23 16:54
 *
 */
public class JavaFxVideoSurface {
    private int bufferWidth;
    private int bufferHeight;
    private PixelBuffer pixelBuffer;
    /**
     * 视频缓冲区的标准像素格式
     */
    private WritablePixelFormat<ByteBuffer> pixelFormat;
    private Rectangle2D updatedBuffer;
    private VideoSurface surface;

    private static WritableImage img;

    public static VideoSurface videoSurfaceForImageView() {
        return (new JavaFxVideoSurface()).surface;
    }

    public JavaFxVideoSurface(){
        pixelFormat = PixelFormat.getByteBgraPreInstance();
        surface=new JavaFxCallbackVideoSurface();
    }


    private class JavaFxCallbackVideoSurface extends CallbackVideoSurface {
        JavaFxCallbackVideoSurface() {
            super(new JavaFxBufferFormatCallback(), new JavaFxRenderCallback(), true, VideoSurfaceAdapters.getVideoSurfaceAdapter());
        }

    }



    private class JavaFxBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            bufferWidth = sourceWidth;
            bufferHeight = sourceHeight;

            //可以将视频表面尺寸设置为与原始视频尺寸匹配
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            //本机视频缓冲区直接用于图像缓冲区
            pixelBuffer = new PixelBuffer(bufferWidth, bufferHeight, buffers[0], pixelFormat);
            img = new WritableImage(pixelBuffer);
            ///每帧都会更新整个缓冲区，因此我们可以通过在此处缓存结果来进行优化
            updatedBuffer = new Rectangle2D(0, 0, bufferWidth, bufferHeight);
        }

    }

    //计时器来进行平滑渲染
    private class JavaFxRenderCallback implements RenderCallback {
        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            //只需要告诉像素缓冲区哪些像素已更新（在本例中为所有像素）-使用了预缓存的值
            Platform.runLater(() -> pixelBuffer.updateBuffer(pixBuf -> updatedBuffer));
        }
    }

    public static WritableImage getImg(){
        return img;
    }

}
