package com.aliware.tianchi;

import com.aliware.tianchi.comm.ProviderLoadInfo;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 * <p>
 * 使用随机权重算法(2): 可用线程数作为权重计算依据
 */
public class UserLoadBalance implements LoadBalance {
    private static Context context = Context.getInstance();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {

//    return simpleRandom(invokers);
        return dynamicRandomWeight(invokers);

    }

    public static <T> Invoker<T> dynamicRandomWeight(List<Invoker<T>> invokerList) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int s = context.small();
        int m = context.mid();
        int l = context.large();

        int n = random.nextInt(s + m + l);

        if (n < s) {
            return invokerList.get(0);
        }
        if (n < s + m) {
            return invokerList.get(1);
        } else {
            return invokerList.get(2);
        }
    }


    public static <T> Invoker<T> simpleRandom(List<Invoker<T>> invokerList) {
        return invokerList.get(ThreadLocalRandom.current().nextInt(invokerList.size()));
    }
}