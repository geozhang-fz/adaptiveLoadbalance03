package com.aliware.tianchi;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.dubbo.rpc.Invoker;

/**
 * @author zrj CreateDate: 2019/5/26
 */
public class Strategy {

  private static Context context = Context.getInstance();

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

  public static <T> Invoker<T> simpleRandomWeight(List<Invoker<T>> invokerList) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    int n = random.nextInt(6);
    if(n == 0) {
      return invokerList.get(0);
    }
    if(n == 1 || n == 2) {
      return invokerList.get(1);
    }
    if(n == 3 || n == 4 || n== 5) {
      return invokerList.get(2);
    }
    return invokerList.get(2);
  }

  public static <T> Invoker<T> simpleRandom(List<Invoker<T>> invokerList) {
    return invokerList.get(ThreadLocalRandom.current().nextInt(invokerList.size()));
  }


}
