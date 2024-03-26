package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zrc
 * @date 2024/03/18
 * 专门用于RedisLimiter 限流基础服务
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     * @param key 区分不同的限流器，比如不同用户id分别统计
     */
    public void  doRateLimit(String key){
        // 创建一个限流器，设置每秒最多访问2次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        // RateType.OVERALL 表示速率校址作用于整个令牌桶，限制所有请求的速率
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        boolean canOp = rateLimiter.tryAcquire();
        // 如果没有获取到令牌，就抛出异常
        if(!canOp){
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
