package com.cdwater.movietimecommon.utils;

import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 雪花算法生成ID
 */
@Component
public class SnowflakeIdGenerator {

    @Value("${snowflake.datacenter-id}")
    private long datacenterId;

    @Value("${snowflake.machine-id}")
    private long machineId;

    //起始时间戳
    private final static long START_TIMESTAMP = 1767196800000L;
    //序列号位数
    private final static long SEQUENCE_BIT = 12L;
    //机器位数
    private final static long MACHINE_BIT = 5L;
    //数据中心位数
    private final static long DATACENTER_BIT = 5L;
    //最大支持的数据中心数量 31
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    //最大支持的机器数量 31
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    //最大支持的序列号数量 4095
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    //序列号
    private long sequence = 0L;
    //上次时间戳
    private long lastTimestamp = -1L;

    @PostConstruct
    public void init() {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_NUM) {
            throw new IllegalArgumentException("datacenterId must be between 0 and " + MAX_DATACENTER_NUM);
        }
        if (machineId < 0 || machineId > MAX_MACHINE_NUM) {
            throw new IllegalArgumentException("machineId must be between 0 and " + MAX_MACHINE_NUM);
        }
    }

    /**
     * 获取下一个ID
     */
    public synchronized long nextId() {
        //获取当前时间戳
        long currentTimestamp = getNewTimestamp();
        //当前时间戳小于上次时间戳（时间回拨）
        if (currentTimestamp < lastTimestamp) {
            throw new BusinessException(RetEnum.SYSTEM_BUSY);
        }
        //相同毫秒内
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列号数量达到最大
            if (sequence == 0L) {
                //获取下一时间戳
                currentTimestamp = getNextTimestamp();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        //记录上次时间戳
        lastTimestamp = currentTimestamp;
        return (currentTimestamp - START_TIMESTAMP) << (SEQUENCE_BIT + MACHINE_BIT + DATACENTER_BIT)
                | datacenterId << (SEQUENCE_BIT + MACHINE_BIT)
                | machineId << SEQUENCE_BIT
                | sequence;
    }

    private long getNewTimestamp() {
        return System.currentTimeMillis();
    }

    private long getNextTimestamp() {
        long timestamp = getNewTimestamp();
        //循环直到获取有效时间戳
        while (timestamp <= lastTimestamp) {
            timestamp = getNewTimestamp();
        }
        return timestamp;
    }
}
