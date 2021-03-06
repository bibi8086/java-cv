package org.bibi.demo;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

/**
 * 按帧录制视频
 *
 * @author Tom（bibi8086@gmail.com）
 * @date 2018/5/7 15:58
 */
public class JavaCvTest3 {

    /**
     * 按帧录制视频
     *
     * @param inputFile-该地址可以是网络直播/录播地址，也可以是远程/本地文件路径
     * @param outputFile                              -该地址只能是文件地址，如果使用该方法推送流媒体服务器会报错，原因是没有设置编码格式
     * @throws Exception
     */
    public static void frameRecord(String inputFile, String outputFile, int audioChannel) throws Exception {

        //该变量建议设置为全局控制变量，用于控制录制结束
        boolean isStart = true;
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel);
        // 开始取视频源
        recordByFrame(grabber, recorder, isStart);
    }

    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder, Boolean status) throws Exception {
        try {//建议在线程中使用该方法
            grabber.start();
            recorder.start();
            Frame frame = null;
            while (status && (frame = grabber.grabFrame()) != null) {
                recorder.record(frame);
            }
            recorder.stop();
            grabber.stop();
        } finally {
            if (grabber != null) {
                grabber.stop();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //String inputFile = "rtsp://admin:admin@192.168.2.236:37779/cam/realmonitor?channel=1&subtype=0";
        String inputFile = "C:\\work\\idea_work\\tom-framework\\output.flv";
        String outputFile = "recorde.mp4";
        frameRecord(inputFile, outputFile, 1);
    }
}
