package org.bibi.demo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片添加水印
 *
 * @author Tom（bibi8086@gmail.com）
 * @date 2018/5/7 15:58
 */
public class ImageMarkUtils {


    public static void main(String[] args) {

        Font font = new Font("宋体", Font.PLAIN, 18);
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字
        new ImageMarkUtils().mark("D:/111_471.jpg", "D:/111_471_text.jpg", "水印效果测试", font, Color.MAGENTA, 20, 20);
        // 增加图片水印
        new ImageMarkUtils().mark("D:/111_471.jpg", "D:/samoye.jpg", "D:/111_471_image.jpg", 100, 100, 20, 20);
    }

    /**
     * 给图片增加文字水印
     *
     * @param imgPath    -要添加水印的图片路径
     * @param outImgPath -输出路径
     * @param text-文字
     * @param font       -字体
     * @param color      -颜色
     * @param x          -文字位于当前图片的横坐标
     * @param y          -文字位于当前图片的竖坐标
     */
    public void mark(String imgPath, String outImgPath, String text, Font font, Color color, int x, int y) {
        try {
            // 读取原图片信息
            File imgFile = null;
            if (imgPath != null) {
                imgFile = new File(imgPath);
            }
            Image img;
            if (imgFile == null || !imgFile.exists() || !imgFile.isFile() || !imgFile.canRead()) {
                throw new RuntimeException("file not exist");
            } else {
                img = ImageIO.read(imgFile);
            }
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);
            // 加水印
            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            mark(bufImg, img, text, font, color, x, y);
            // 输出图片
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片增加图片水印
     *
     * @param inputImg  -源图片，要添加水印的图片
     * @param markImg   - 水印图片
     * @param outputImg -输出图片(可以是源图片)
     * @param width     - 水印图片宽度
     * @param height    -水印图片高度
     * @param x         -横坐标，相对于源图片
     * @param y         -纵坐标，同上
     */
    public void mark(String inputImg, String markImg, String outputImg, int width, int height, int x, int y) {
        try {
            // 读取原图片信息
            File inputImgFile = null;
            File markImgFile = null;
            if (inputImg != null && markImg != null) {
                inputImgFile = new File(inputImg);
                markImgFile = new File(markImg);
            }
            Image img;
            Image mark;
            if (inputImgFile == null || !inputImgFile.exists() || !inputImgFile.isFile() || !inputImgFile.canRead() || !markImgFile.exists() || !markImgFile.isFile() || !markImgFile.canRead()) {
                throw new RuntimeException("file not exist");
            } else {
                img = ImageIO.read(inputImgFile);
                mark = ImageIO.read(markImgFile);
            }
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);
            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            mark(bufImg, img, mark, width, height, x, y);
            FileOutputStream outImgStream = new FileOutputStream(outputImg);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加文字水印
     *
     * @param bufImg
     * @param img
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     */
    public void mark(BufferedImage bufImg, Image img, String text, Font font, Color color, int x, int y) {
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
        g.dispose();
    }


    /**
     * 添加图片水印
     *
     * @param bufImg
     * @param img
     * @param markImg
     * @param width
     * @param height
     * @param x
     * @param y
     */
    public void mark(BufferedImage bufImg, Image img, Image markImg, int width, int height, int x, int y) {
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        g.drawImage(markImg, x, y, width, height, null);
        g.dispose();
    }

}
