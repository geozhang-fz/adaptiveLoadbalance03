package com.aliware.tianchi;

/**
 * @author zrj CreateDate: 2019/5/25
 */
public class Context {

  private static Context instance = new Context();

  public static Context getInstance() {
    return instance;
  }

  private volatile int sCurWeight = 200;

  private volatile int mCurWeight = 200;

  private volatile int lCurWeight = 200;

  private final Object lock = new Object();

  // s、m、l三个级别的provider服务器的工作状态
  private volatile ProviderStateEnum s = ProviderStateEnum.NORMAL;
  private volatile ProviderStateEnum m = ProviderStateEnum.NORMAL;
  private volatile ProviderStateEnum l = ProviderStateEnum.NORMAL;

  public ProviderStateEnum getS() {
    synchronized (lock) {
      return s;
    }
  }

  public ProviderStateEnum getM() {
    synchronized (lock) {
      return m;
    }
  }

  public ProviderStateEnum getL() {
    synchronized (lock) {
      return l;
    }
  }

  enum Provider {
    // Provider的name
    S,
    M,
    L,
    ;

  }

  public void adjust(ProviderStateEnum pState, Provider provider) {
//    synchronized (lock) {
    switch (provider) {
      case S: {
        if ((sCurWeight > 20 && pState.isBusy()) || (sCurWeight < 500 && !pState.isBusy())) {
          sCurWeight += pState.getValue();
        }
        this.s = pState;
        break;
      }
      case M: {
        if ((mCurWeight > 20 && pState.isBusy()) || (mCurWeight < 500 && !pState.isBusy())) {
          mCurWeight += pState.getValue();
        }
        this.m = pState;
        break;
      }
      case L: {
        if ((lCurWeight > 20 && pState.isBusy()) || (lCurWeight < 500 && !pState.isBusy())) {
          lCurWeight += pState.getValue();
        }
        this.l = pState;
        break;
      }
//      }
    }
  }

  public int getTotalWeight() {
    synchronized (lock) {
      return sCurWeight + mCurWeight + lCurWeight;
    }
  }

  public int getsCurWeight() {
//    synchronized (lock) {
    return sCurWeight;
//    }
  }

  public int getmCurWeight() {
//    synchronized (lock) {
    return mCurWeight;
//    }
  }

  public int getlCurWeight() {
//    synchronized (lock) {
    return lCurWeight;
//    }
  }


}
