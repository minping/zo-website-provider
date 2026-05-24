package com.zode.web.article.util;

import java.util.Random;

/**
 * @author zhoump
 * @date 2026/5/17
 * @purpose
 */
public class ColorGenerator {

    private static final Random random = new Random();

    /**
     * 生成随机RGB颜色（格式：rgb(r, g, b)）
     */
    public static String generateRandomRGB() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("rgb(%d, %d, %d)", red, green, blue);
    }

    /**
     * 生成随机十六进制颜色（格式：#RRGGBB）
     */
    public static String generateRandomHex() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("#%02x%02x%02x", red, green, blue).toUpperCase();
    }

    /**
     * 生成带透明度的随机RGBA颜色（格式：rgba(r, g, b, a)）
     * @param alpha 透明度，范围0.0-1.0
     */
    public static String generateRandomRGBA(float alpha) {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return String.format("rgba(%d, %d, %d, %.2f)", red, green, blue, alpha);
    }

    /**
     * 生成随机HSL颜色（格式：hsl(h, s%, l%)）
     */
    public static String generateRandomHSL() {
        int hue = random.nextInt(361); // 色相 0-360
        int saturation = random.nextInt(101); // 饱和度 0-100%
        int lightness = random.nextInt(101); // 亮度 0-100%
        return String.format("hsl(%d, %d%%, %d%%)", hue, saturation, lightness);
    }

    /**
     * 生成较亮丽的随机颜色（避免灰暗色调）
     */
    public static String generateVibrantHex() {
        // 限制颜色分量在较亮范围[50, 255]，避免灰暗色调[5](@ref)
        int red = random.nextInt(206) + 50;
        int green = random.nextInt(206) + 50;
        int blue = random.nextInt(206) + 50;
        return String.format("#%02x%02x%02x", red, green, blue).toUpperCase();
    }

    /**
     * 生成随机颜色数组
     * @param count 需要生成的颜色数量
     * @param format 颜色格式（hex, rgb, hsl）
     */
    public static String[] generateRandomColors(int count, String format) {
        String[] colors = new String[count];
        for (int i = 0; i < count; i++) {
            switch (format.toLowerCase()) {
                case "hex":
                    colors[i] = generateVibrantHex();
                    break;
                case "rgb":
                    colors[i] = generateRandomRGB();
                    break;
                case "hsl":
                    colors[i] = generateRandomHSL();
                    break;
                default:
                    colors[i] = generateRandomHex();
            }
        }
        return colors;
    }
}
