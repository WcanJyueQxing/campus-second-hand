package com.nie.secondhub.config;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 数学验证码文本生成器
 * 生成简单的数学计算题（如：1+2=? 或 6-3=?）
 * 答案附加在验证码文本末尾，格式为 "题目@答案"
 *
 * @author nie
 */
public class KaptchaTextCreator extends DefaultTextCreator {

    private static final String[] CNUMBERS = "0,1,2,3,4,5,6,7,8,9,10".split(",");

    /**
     * 生成数学计算验证码文本
     * 格式：算术表达式@答案
     * 例如：3+5=?@8
     */
    @Override
    public String getText() {
        Integer result = 0;
        Random random = new Random();

        // 生成两个随机数
        int x = random.nextInt(10);
        int y = random.nextInt(10);

        StringBuilder suChinese = new StringBuilder();

        // 随机选择运算类型：0=乘法, 1=除法或加法, 2=减法
        int randomoperands = random.nextInt(3);

        if (randomoperands == 0) {
            // 乘法运算：a * b
            result = x * y;
            suChinese.append(CNUMBERS[x]);
            suChinese.append("*");
            suChinese.append(CNUMBERS[y]);
        } else if (randomoperands == 1) {
            // 如果 x 不为0且 y能被x整除，则做除法；否则做加法
            if ((x != 0) && y % x == 0) {
                result = y / x;
                suChinese.append(CNUMBERS[y]);
                suChinese.append("/");
                suChinese.append(CNUMBERS[x]);
            } else {
                result = x + y;
                suChinese.append(CNUMBERS[x]);
                suChinese.append("+");
                suChinese.append(CNUMBERS[y]);
            }
        } else {
            // 减法运算：确保结果为非负数
            if (x >= y) {
                result = x - y;
                suChinese.append(CNUMBERS[x]);
                suChinese.append("-");
                suChinese.append(CNUMBERS[y]);
            } else {
                result = y - x;
                suChinese.append(CNUMBERS[y]);
                suChinese.append("-");
                suChinese.append(CNUMBERS[x]);
            }
        }

        // 拼接 "?@" 和答案
        suChinese.append("=?@");
        suChinese.append(result);

        return suChinese.toString();
    }
}
