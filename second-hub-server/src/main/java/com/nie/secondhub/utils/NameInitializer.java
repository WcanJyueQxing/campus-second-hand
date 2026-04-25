package com.nie.secondhub.utils;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 姓名初始化工具类
 * 用于将常用姓氏和名字存入Redis
 */
@Component
public class NameInitializer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 常用姓氏
    private static final List<String> SURNAMES = List.of(
            "王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴",
            "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗",
            "郑", "梁", "谢", "宋", "唐", "许", "韩", "冯", "邓", "曹",
            "彭", "曾", "肖", "田", "董", "袁", "潘", "于", "蒋", "蔡",
            "余", "杜", "叶", "程", "苏", "魏", "吕", "丁", "任", "沈",
            "姚", "卢", "姜", "崔", "钟", "谭", "陆", "汪", "范", "金"
    );

    // 常用男名
    private static final List<String> BOY_NAMES = List.of(
            "伟", "强", "军", "勇", "杰", "涛", "磊", "超", "明", "辉",
            "军", "阳", "刚", "平", "鹏", "杰", "飞", "峰", "宇", "健",
            "涛", "华", "强", "军", "杰", "磊", "超", "明", "辉", "阳",
            "宇", "俊", "轩", "泽", "浩", "宇", "轩", "泽", "浩", "宇",
            "轩", "泽", "浩", "宇", "轩", "泽", "浩", "宇", "轩", "泽"
    );

    // 常用女名
    private static final List<String> GIRL_NAMES = List.of(
            "芳", "娜", "静", "丽", "艳", "敏", "静", "丽", "艳", "敏",
            "静", "丽", "艳", "敏", "静", "丽", "艳", "敏", "静", "丽",
            "婷", "芳", "娜", "静", "丽", "艳", "敏", "静", "丽", "艳",
            "婷", "芳", "娜", "静", "丽", "艳", "敏", "静", "丽", "艳",
            "婷", "芳", "娜", "静", "丽", "艳", "敏", "静", "丽", "艳"
    );

    /**
     * 初始化姓名库到Redis
     */
    @PostConstruct
    public void initNameLibrary() {
        try {
            // 初始化姓氏库
            initSurnames();
            // 初始化男名库
            initBoyNames();
            // 初始化女名库
            initGirlNames();
            System.out.println("姓名库初始化完成！");
        } catch (Exception e) {
            System.out.println("Redis不可用，姓名库将使用内存中的数据");
        }
    }

    /**
     * 初始化姓氏库
     */
    private void initSurnames() {
        try {
            String key = "random:name:first";
            // 清空现有数据
            stringRedisTemplate.delete(key);
            // 添加姓氏
            for (String surname : SURNAMES) {
                stringRedisTemplate.opsForList().rightPush(key, surname);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 初始化男名库
     */
    private void initBoyNames() {
        try {
            String key = "random:name:boy";
            // 清空现有数据
            stringRedisTemplate.delete(key);
            // 添加男名
            for (String name : BOY_NAMES) {
                stringRedisTemplate.opsForList().rightPush(key, name);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 初始化女名库
     */
    private void initGirlNames() {
        try {
            String key = "random:name:girl";
            // 清空现有数据
            stringRedisTemplate.delete(key);
            // 添加女名
            for (String name : GIRL_NAMES) {
                stringRedisTemplate.opsForList().rightPush(key, name);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 生成随机姓名
     * @param gender 性别：male/female/random
     * @return 随机姓名
     */
    public String generateName(String gender) {
        // 随机获取姓氏
        String surname = getRandomSurname();
        // 根据性别获取名字
        String name = getRandomName(gender);
        return surname + name;
    }

    /**
     * 批量生成随机姓名
     * @param gender 性别：male/female/random
     * @param count 生成数量
     * @return 随机姓名列表
     */
    public List<String> generateNames(String gender, int count) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            names.add(generateName(gender));
        }
        return names;
    }

    /**
     * 获取随机姓氏
     * @return 随机姓氏
     */
    private String getRandomSurname() {
        try {
            String key = "random:name:first";
            Long size = stringRedisTemplate.opsForList().size(key);
            if (size == null || size == 0) {
                initSurnames();
                size = (long) SURNAMES.size();
            }
            long index = RandomUtil.randomLong(0, size);
            return stringRedisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            // Redis不可用时使用内存中的数据
            return SURNAMES.get(RandomUtil.randomInt(0, SURNAMES.size()));
        }
    }

    /**
     * 根据性别获取随机名字
     * @param gender 性别：male/female/random
     * @return 随机名字
     */
    private String getRandomName(String gender) {
        try {
            String key;
            if ("male".equals(gender)) {
                key = "random:name:boy";
            } else if ("female".equals(gender)) {
                key = "random:name:girl";
            } else {
                // 随机性别
                key = RandomUtil.randomBoolean() ? "random:name:boy" : "random:name:girl";
            }

            Long size = stringRedisTemplate.opsForList().size(key);
            if (size == null || size == 0) {
                if ("random:name:boy".equals(key)) {
                    initBoyNames();
                    size = (long) BOY_NAMES.size();
                } else {
                    initGirlNames();
                    size = (long) GIRL_NAMES.size();
                }
            }

            long index = RandomUtil.randomLong(0, size);
            return stringRedisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            // Redis不可用时使用内存中的数据
            if ("male".equals(gender)) {
                return BOY_NAMES.get(RandomUtil.randomInt(0, BOY_NAMES.size()));
            } else if ("female".equals(gender)) {
                return GIRL_NAMES.get(RandomUtil.randomInt(0, GIRL_NAMES.size()));
            } else {
                // 随机性别
                return RandomUtil.randomBoolean() ? 
                    BOY_NAMES.get(RandomUtil.randomInt(0, BOY_NAMES.size())) : 
                    GIRL_NAMES.get(RandomUtil.randomInt(0, GIRL_NAMES.size()));
            }
        }
    }
}