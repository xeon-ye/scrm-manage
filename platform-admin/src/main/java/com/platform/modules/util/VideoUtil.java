/*
 * 项目名称:platform-plus
 * 类名称:VideoUtil.java
 * 包名称:com.platform
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021/6/24 16:58            liuqianru    初版做成
 *
 */
package com.platform.modules.util;

import com.platform.modules.oss.cloud.UploadFactory;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * VideoUtil
 *
 * @author liuqianru
 * @date 2021/6/24 16:58
 */
public class VideoUtil {

    /**
     * 将截取的帧生成文件或者生成图片保存到oss
     * @param frame
     * @return
     */
    private static String getBase64FromFrame(Frame frame) {
        String imgFormat = ".jpeg";
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            // ImageIO工具类提供了将图片写成文件或者outputStream中
            ImageIO.write(srcBi, imgFormat, output);
            // 保存图片到oss
            String url = UploadFactory.build().uploadSuffix(output.toByteArray(), imgFormat);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param fileUrl  源视频文件Url路径
     * @return 返回截取的帧的Base64串列表
     * @throws Exception
     */
    public static String fetchPicFromVideo(String fileUrl) throws Exception {
        String url = "";
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(fileUrl);
        ff.start();
        long timeLength = ff.getLengthInTime();
        Frame frame = ff.grabImage();
        long startTime = frame.timestamp;
        long timestamp = 0;
        int second = 0;
        while (timestamp <= timeLength) {
            timestamp = startTime + second * 1000000L;
            ff.setTimestamp(timestamp);
            frame = ff.grabImage();
            if (frame != null) {
                if (frame.image != null) {
                    url = getBase64FromFrame(frame);
                    break;
                }
            }
        }
        ff.stop();
        return url;
    }


    public static void main(String[] args) {
        try {
            String videoPath = "http://images.qkjebiz.qkjchina.com//qkjebiz/20210624/cc082ebe758c4ea181d1d1d8e36db495.mp4";
            String url = VideoUtil.fetchPicFromVideo(videoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
