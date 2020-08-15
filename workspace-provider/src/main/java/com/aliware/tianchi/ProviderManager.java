package com.aliware.tianchi;


/**
 * @author zrj CreateDate: 2019/5/26
 */
public class ProviderManager {

    private static ProviderManager instance = new ProviderManager();

    private static String quota = System.getProperty("quota");

    public String getQuota() {
        return quota;
    }

    public static ProviderManager getInstance() {
        return instance;
    }

    private final Object lockR = new Object();

    private final Object lockVR = new Object();

    private final Object lockTS = new Object();

    private volatile long request = 0;

    private volatile long validRequest = 0;

    private volatile long totalValidRequest = 0;

    private volatile long timeSpent = 0;

    private volatile long totalTimeSpent = 0;

    public void clear() {
        synchronized (lockR) {
            request = 0;
        }
        synchronized (lockVR) {
            validRequest = 0;
        }
        synchronized (lockTS) {
            timeSpent = 0;
        }
    }

    public void incrementRequest() {
        synchronized (lockR) {
            request++;
        }
    }

    public void incrementValidRequest() {
        synchronized (lockVR) {
            validRequest++;
        }
    }

    public void addTimeSpent(long n) {
        synchronized (lockTS) {
            timeSpent = timeSpent + n;
        }
    }

    public long getRequest() {
        synchronized (lockR) {
            return request;
        }
    }

    public long getValidRequest() {
        synchronized (lockVR) {
            totalValidRequest = totalValidRequest + validRequest;
            return validRequest;
        }
    }

    public long getTimeSpent() {
        synchronized (lockTS) {
            totalTimeSpent = totalTimeSpent + timeSpent;
            return timeSpent;
        }
    }

    public long getTotalValidRequest() {
        synchronized (lockVR) {
            return totalValidRequest;
        }
    }

    public long getTotalTimeSpent() {
        synchronized (lockTS) {
            return totalTimeSpent;
        }
    }


    private final Object lockState = new Object();

    private volatile ProviderStateEnum stateEnum = ProviderStateEnum.NORMAL;

    /**
     * 更新provider的状态state
     *
     * @param cur: schedule内平均每条消息的发送时间
     * @param total: 该provider总计，平均每条消息的发送时间
     * @return
     */
    public ProviderStateEnum updateState(double cur, double total) {
        synchronized (lockState) {
            // 根据cur和total的比较，获取对应的状态
            this.stateEnum = state(cur, total);
//    boolean tooHot = tooHot();
//      this.stateEnum = tooHot ? ProviderStateEnum.$1BUSY : tmp;
            return stateEnum;
        }
    }

    public ProviderStateEnum getStateEnum() {
        return stateEnum;
    }

    private ProviderStateEnum state(double cur, double total) {
        if (cur > total) {
            if ((cur - total) / total > 0.35) {
                return ProviderStateEnum.$6BUSY;
            }
            if ((cur - total) / total > 0.3) {
                return ProviderStateEnum.$5BUSY;
            }
            if ((cur - total) / total > 0.25) {
                return ProviderStateEnum.$4BUSY;
            }
            if ((cur - total) / total > 0.2) {
                return ProviderStateEnum.$3BUSY;
            }
            if ((cur - total) / total > 0.15) {
                return ProviderStateEnum.$2BUSY;
            }
            if ((cur - total) / total > 0.1) {
                return ProviderStateEnum.$1BUSY;
            }
            if ((cur - total) / total > 0.05) {
                return ProviderStateEnum.BUSY;
            }
        }

        if (cur < total) {
            if ((total - cur) / cur > 0.25) {
                return ProviderStateEnum.$$$$IDLE;
            }
            if ((total - cur) / cur > 0.2) {
                return ProviderStateEnum.$$$IDLE;
            }
            if ((total - cur) / cur > 0.15) {
                return ProviderStateEnum.$$IDLE;
            }
            if ((total - cur) / cur > 0.1) {
                return ProviderStateEnum.$IDLE;
            }
            if ((total - cur) / cur > 0.05) {
                return ProviderStateEnum.IDLE;
            }
        }

        return ProviderStateEnum.NORMAL;
    }


    private volatile int max = Integer.MAX_VALUE;

    private final Object lock_t = new Object();

    // 启动设置一次
    public void setMax(int max) {
        this.max = max;
    }

    public boolean tooHot(int active) {
        return max - active < 5;
    }
}
