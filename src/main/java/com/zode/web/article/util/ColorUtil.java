package com.zode.web.article.util;

import java.util.Random;

/**
 * @author zhoump
 * @date 2026/8/6
 * @purpose
 */
public class ColorUtil {

    private static final Random RANDOM = new Random();

    /**
     * 获取一组适配渐变的两个十六进制颜色
     * @return String[0] 起始色, String[1] 结束色 格式 #xxxxxx
     * 如果你想要风格变化，可以修改这几个值：
     * 1. `offset = 30~90`：调小更接近单色渐变；调大对比更强
     * 2. `saturation`：提高颜色更鲜艳，降低更素雅
     * 3. `lightness`：调高整体偏亮，调低偏深色
     */
    public static String[] getRandomGradientHexColors() {
        // 随机基础色相 0~360
        float baseHue = RANDOM.nextFloat() * 360f;
        // 色相偏移 30~90度，保证色系相近，渐变协调
        float offset = 30 + RANDOM.nextFloat() * 120f;
        float hue2 = (baseHue + offset) % 360f;

        // 饱和度 0.4~0.75 避免太灰/刺眼
        float saturation = 0.4f + RANDOM.nextFloat() * 0.4f;
        // 明度 0.35~0.75 不会过亮发白、不会过暗发黑
        float lightness = 0.35f + RANDOM.nextFloat() * 0.4f;

        String color1 = hslToHex(baseHue, saturation, lightness);
        String color2 = hslToHex(hue2, saturation, lightness);
        return new String[]{color1, color2};
    }

    /**
     * HSL 转 #十六进制颜色
     */
    private static String hslToHex(float h, float s, float l) {
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs((h / 60f) % 2 - 1));
        float m = l - c / 2;

        float r, g, b;
        if (h < 60) {
            r = c; g = x; b = 0;
        } else if (h < 120) {
            r = x; g = c; b = 0;
        } else if (h < 180) {
            r = 0; g = c; b = x;
        } else if (h < 240) {
            r = 0; g = x; b = c;
        } else if (h < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        int red = Math.round((r + m) * 255);
        int green = Math.round((g + m) * 255);
        int blue = Math.round((b + m) * 255);

        // 格式化 #xxxxxx 小写
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    // 测试
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            String[] colors = getRandomGradientHexColors();
            System.out.println("渐变颜色：" + colors[0] + " → " + colors[1]);
        }
    }
}
