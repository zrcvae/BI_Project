package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zrc
 * @date 2024/03/14
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChar() {
        long biModelId = 1651468516836098050L;
        String answer = aiManager.doChar(biModelId, "许嵩");
        System.out.println(answer);
    }
}