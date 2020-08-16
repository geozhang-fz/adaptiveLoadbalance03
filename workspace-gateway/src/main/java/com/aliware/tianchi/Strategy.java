package com.aliware.tianchi;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.dubbo.rpc.Invoker;


public class Strategy {

    private static Context context = Context.getInstance();

    public static <T> Invoker<T> dynamicRandomWeight(List<Invoker<T>> invokers) {

        // 获取s、m、l当前的静态权重
        int sWeight = context.getsWeight();
        int mWeight = context.getmWeight();
        int lWeight = context.getlWeight();

        // 获取s、m、l当前的动态权重
        int sCurWeight = context.getsCurWeight();
        int mCurWeight = context.getmCurWeight();
        int lCurWeight = context.getlCurWeight();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int offsetWeight = random.nextInt(sWeight + mWeight + lWeight + sCurWeight + mCurWeight + lCurWeight);

        if (offsetWeight < sWeight + sCurWeight) {
            return invokers.get(0);
        } else if (offsetWeight < sWeight + sCurWeight + mWeight + mCurWeight) {
            return invokers.get(1);
        } else {
            return invokers.get(2);
        }
    }

    public static <T> Invoker<T> simpleRandomWeight(List<Invoker<T>> invokers) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int n = random.nextInt(6);
        if (n == 0) {
            return invokers.get(0);
        }
        if (n == 1 || n == 2) {
            return invokers.get(1);
        }
        if (n == 3 || n == 4 || n == 5) {
            return invokers.get(2);
        }
        return invokers.get(2);
    }

    public static <T> Invoker<T> simpleRandom(List<Invoker<T>> invokers) {
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }


}
