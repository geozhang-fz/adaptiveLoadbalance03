package com.aliware.tianchi;

/**
 * @author zrj CreateDate: 2019/5/25
 */
public class Context {

    private static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    // 静态权重
    private volatile int sWeight = 100;
    private volatile int mWeight = 400;
    private volatile int lWeight = 500;

    // 动态权重
    private volatile int sCurWeight = 500;
    private volatile int mCurWeight = 500;
    private volatile int lCurWeight = 500;

    private final Object lock = new Object();

    // s、m、l三个级别的provider服务器的工作状态
    private volatile ProviderStateEnum sState = ProviderStateEnum.NORMAL;
    private volatile ProviderStateEnum mState = ProviderStateEnum.NORMAL;
    private volatile ProviderStateEnum lState = ProviderStateEnum.NORMAL;

    public ProviderStateEnum getsState() {
        synchronized (lock) {
            return sState;
        }
    }

    public ProviderStateEnum getmState() {
        synchronized (lock) {
            return mState;
        }
    }

    public ProviderStateEnum getlState() {
        synchronized (lock) {
            return lState;
        }
    }

    enum Provider {
        // Provider的name
        S,
        M,
        L,
        ;

    }

    public int getsWeight() {
        return sWeight;
    }

    public int getmWeight() {
        return mWeight;
    }

    public int getlWeight() {
        return lWeight;
    }

    public void adjust(ProviderStateEnum pState, Provider provider) {
//    synchronized (lock) {
        int upperBoundry = 600;
        int lowerBoundry = 20;
        switch (provider) {
            case S: {
                // 若Busy，大于20的部分，value为负，sCurWeight减小；
                //// 小于20的部分，不做调整，直到变成notBusy，sCurWeight增加（又忙权重又小，就不懂它）
                // 若notBusy，小于500的部分，value为正，sCurWeight增加；
                //// 大于500的部分，不做调整，直到变成Busy，sCurWeight减小；
                if ((sCurWeight > lowerBoundry && pState.isBusy()) || (sCurWeight < upperBoundry && !pState.isBusy())) {
                    sCurWeight += pState.getValue();
                }
//                sCurWeight += pState.getValue();
//                if (lCurWeight > 20 || lCurWeight < 600) {
//                    sCurWeight += pState.getValue();
//                }
                this.sState = pState;
                break;
            }
            case M: {
                if ((mCurWeight > lowerBoundry && pState.isBusy()) || (mCurWeight < upperBoundry && !pState.isBusy())) {
                    mCurWeight += pState.getValue();
                }
//                mCurWeight += pState.getValue();

//                if (lCurWeight > 20 || lCurWeight < 600) {
//                    mCurWeight += pState.getValue();
//                }
                this.mState = pState;
                break;
            }
            case L: {
                if ((lCurWeight > lowerBoundry && pState.isBusy()) || (lCurWeight < upperBoundry && !pState.isBusy())) {
                    lCurWeight += pState.getValue();
                }
//                lCurWeight += pState.getValue();
//                if (lCurWeight > 20 || lCurWeight < 600) {
//                    lCurWeight += pState.getValue();
//                }
                this.lState = pState;
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
