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

  public void incrementFinished() {
    synchronized (lockVR) {
      validRequest++;
    }
  }

  public void addCost(long n) {
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

  public ProviderStateEnum setState(double curr, double avg) {
    ProviderStateEnum tmp = state(curr, avg);
//    boolean tooHot = tooHot();
    synchronized (lockState) {
//      this.stateEnum = tooHot ? ProviderStateEnum.$1BUSY : tmp;
      this.stateEnum = tmp;
      return stateEnum;
    }
  }

  public ProviderStateEnum getStateEnum() {
    return stateEnum;
  }

  private ProviderStateEnum state(double c, double avg) {
    if (c > avg) {
      if ((c - avg) / avg > 0.35) {
        return ProviderStateEnum.$6BUSY;
      }
      if ((c - avg) / avg > 0.3) {
        return ProviderStateEnum.$5BUSY;
      }
      if ((c - avg) / avg > 0.25) {
        return ProviderStateEnum.$4BUSY;
      }
      if ((c - avg) / avg > 0.2) {
        return ProviderStateEnum.$3BUSY;
      }
      if ((c - avg) / avg > 0.15) {
        return ProviderStateEnum.$2BUSY;
      }
      if ((c - avg) / avg > 0.1) {
        return ProviderStateEnum.$1BUSY;
      }
      if ((c - avg) / avg > 0.05) {
        return ProviderStateEnum.BUSY;
      }
    }

    if (c < avg) {
      if ((avg - c) / c > 0.25) {
        return ProviderStateEnum.$$$$IDLE;
      }
      if ((avg - c) / c > 0.2) {
        return ProviderStateEnum.$$$IDLE;
      }
      if ((avg - c) / c > 0.15) {
        return ProviderStateEnum.$$IDLE;
      }
      if ((avg - c) / c > 0.1) {
        return ProviderStateEnum.$IDLE;
      }
      if ((avg - c) / c > 0.05) {
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
