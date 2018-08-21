package org.bibi.demo;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.*;

import javax.swing.*;

/**
 * 转换流
 *
 * @author Tom（bibi8086@gmail.com）
 * @date 2018/5/7 15:58
 */
public class JavaCvTest4 {


    public static void main(String[] args) throws Exception {
        String inputFile = "rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0";
        String outputFile = "rtmp://192.168.30.21/live/pushFlow";
        recordPush(inputFile, outputFile, 25);
    }


    /**
     * 转流器
     *
     * @param inputFile
     * @param outputFile
     * @throws Exception
     */
    public static void recordPush(String inputFile, String outputFile, int v_rs) throws Exception {
        Loader.load(opencv_objdetect.class);
        long startTime = 0;
        FrameGrabber grabber = FFmpegFrameGrabber.createDefault(inputFile);
        try {
            grabber.start();
        } catch (Exception e) {
            try {
                grabber.restart();
            } catch (Exception e1) {
                throw e;
            }
        }

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        Frame grabframe = grabber.grab();
        opencv_core.IplImage grabbedImage = null;
        if (grabframe != null) {
            System.out.println("取到第一帧");
            grabbedImage = converter.convert(grabframe);
        } else {
            System.out.println("没有取到第一帧");
        }
        //如果想要保存图片,可以使用 opencv_imgcodecs.cvSaveImage("hello.jpg", grabbedImage);来保存图片
        FrameRecorder recorder;
        try {
            recorder = FrameRecorder.createDefault(outputFile, 1280, 720);
        } catch (FrameRecorder.Exception e) {
            throw e;
        }
        // avcodec.AV_CODEC_ID_H264
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setFrameRate(v_rs);
        recorder.setGopSize(v_rs);
        System.out.println("准备开始推流...");
        try {
            recorder.start();
        } catch (FrameRecorder.Exception e) {
            try {
                System.out.println("录制器启动失败，正在重新启动...");
                if (recorder != null) {
                    System.out.println("尝试关闭录制器");
                    recorder.stop();
                    System.out.println("尝试重新开启录制器");
                    recorder.start();
                }

            } catch (FrameRecorder.Exception e1) {
                throw e;
            }
        }
        System.out.println("开始推流");
        CanvasFrame frame = new CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        while (frame.isVisible() && (grabframe = grabber.grab()) != null) {
            System.out.println("推流...");
            frame.showImage(grabframe);
            grabbedImage = converter.convert(grabframe);
            Frame rotatedFrame = converter.convert(grabbedImage);

            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            //时间戳
            recorder.setTimestamp(1000 * (System.currentTimeMillis() - startTime));
            if (rotatedFrame != null) {
                recorder.record(rotatedFrame);
            }

            Thread.sleep(40);
        }
        frame.dispose();
        recorder.stop();
        recorder.release();
        grabber.stop();
        System.exit(2);
    }
}
