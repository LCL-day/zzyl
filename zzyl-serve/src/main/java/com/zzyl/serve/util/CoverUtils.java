package com.zzyl.serve.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封面图工具：当用户发布/新增美食没有上传封面时，
 * 自动以「美食名称 + 分类名称」生成一张封面图（按分类配色）。
 *
 * @author admin
 * @date 2026-09-15
 */
public class CoverUtils
{
    private static final Logger log = LoggerFactory.getLogger(CoverUtils.class);

    /** 图片访问前缀，与框架 RuoYiConfig.getProfile() 的映射保持一致 */
    private static final String URL_PREFIX = "/profile/upload/cover/";

    /** 绘制尺寸 */
    private static final int WIDTH = 640;
    private static final int HEIGHT = 420;

    /**
     * 生成封面图
     *
     * @param uploadRoot 上传根目录（ruoyi.profile）
     * @param title 美食名称
     * @param categoryName 分类名称（可空）
     * @return 可访问的资源路径；生成失败返回 null
     */
    public static String generate(String uploadRoot, String title, String categoryName)
    {
        try
        {
            String safeTitle = (title == null || title.trim().isEmpty()) ? "美食" : title.trim();
            String safeCategory = (categoryName == null || categoryName.trim().isEmpty()) ? "精选" : categoryName.trim();

            String dir = uploadRoot + File.separator + "upload" + File.separator + "cover";
            File dirFile = new File(dir);
            if (!dirFile.exists() && !dirFile.mkdirs())
            {
                log.warn("创建封面目录失败: {}", dir);
                return null;
            }

            Color[] palette = paletteOf(safeCategory);
            BufferedImage image = draw(safeTitle, safeCategory, palette);

            // 文件名用「分类-标题哈希-时间戳」，保证同一标题重复生成也不会互相覆盖
            String fileName = "cover_" + shortHash(safeCategory + "-" + safeTitle) + "_"
                    + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg";
            File target = new File(dirFile, fileName);
            ImageIO.write(image, "jpg", target);
            log.debug("自动生成封面: {} -> {}", safeTitle, target.getAbsolutePath());
            return URL_PREFIX + fileName;
        }
        catch (Exception e)
        {
            // 封面生成失败不应影响业务主流程
            log.warn("自动生成封面失败: title={}, category={}, error={}", title, categoryName, e.getMessage());
            return null;
        }
    }

    /**
     * 绘制封面：渐变底 + 装饰圆 + 菜名 + 分类名
     */
    private static BufferedImage draw(String title, String category, Color[] palette)
    {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 渐变背景
        g.setPaint(new GradientPaint(0, 0, palette[0], WIDTH, HEIGHT, palette[1]));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 装饰圆
        g.setColor(new Color(255, 255, 255, 26));
        g.fillOval(-60, -70, 260, 260);
        g.fillOval(WIDTH - 190, HEIGHT - 170, 240, 240);
        g.setColor(new Color(255, 255, 255, 18));
        g.fillOval(WIDTH / 2 - 90, HEIGHT / 2 - 70, 180, 180);

        // 美食名称（自动缩字号以适应宽度）
        g.setColor(Color.WHITE);
        Font font = new Font("Microsoft YaHei UI", Font.BOLD, 54);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        while (textWidth > WIDTH - 70 && font.getSize() > 22)
        {
            font = font.deriveFont((float) font.getSize() - 3);
            g.setFont(font);
            fm = g.getFontMetrics();
            textWidth = fm.stringWidth(title);
        }
        g.drawString(title, (WIDTH - textWidth) / 2, HEIGHT / 2 + fm.getAscent() / 2 - 6);

        // 分隔线
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRoundRect((WIDTH - 90) / 2, HEIGHT / 2 + 32, 90, 5, 4, 4);

        // 分类名称
        g.setColor(new Color(255, 255, 255, 215));
        g.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        FontMetrics fm2 = g.getFontMetrics();
        g.drawString(category, (WIDTH - fm2.stringWidth(category)) / 2, HEIGHT / 2 + 76);

        g.dispose();
        return img;
    }

    /**
     * 按分类名称取配色；未匹配到时按名称哈希生成稳定颜色
     */
    private static Color[] paletteOf(String category)
    {
        switch (category)
        {
            case "川菜": return new Color[] { new Color(226, 74, 60), new Color(150, 24, 30) };
            case "粤菜": return new Color[] { new Color(46, 160, 130), new Color(16, 92, 80) };
            case "鲁菜": return new Color[] { new Color(198, 132, 58), new Color(126, 72, 26) };
            case "甜品": return new Color[] { new Color(240, 138, 178), new Color(190, 76, 132) };
            case "小吃": return new Color[] { new Color(238, 150, 58), new Color(196, 92, 26) };
            case "湘菜": return new Color[] { new Color(216, 62, 62), new Color(140, 22, 42) };
            case "家常菜": return new Color[] { new Color(122, 168, 88), new Color(64, 108, 52) };
            case "汤羹": return new Color[] { new Color(112, 158, 196), new Color(52, 96, 140) };
            case "面食": return new Color[] { new Color(206, 168, 96), new Color(146, 106, 44) };
            case "饮品": return new Color[] { new Color(96, 176, 190), new Color(40, 116, 140) };
            case "素食": return new Color[] { new Color(132, 186, 116), new Color(70, 128, 78) };
            case "烘焙": return new Color[] { new Color(178, 140, 196), new Color(118, 84, 148) };
            default:
                int hash = Math.abs(category.hashCode());
                float hue = (hash % 360) / 360f;
                return new Color[] {
                    Color.getHSBColor(hue, 0.55f, 0.92f),
                    Color.getHSBColor((hue + 0.06f) % 1f, 0.72f, 0.62f)
                };
        }
    }

    /**
     * 名称短哈希，用于文件名
     */
    private static String shortHash(String text)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++)
            {
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            return String.valueOf(Math.abs(text.hashCode()));
        }
    }
}
