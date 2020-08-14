package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author daofeng.xjf
 *
 * 服务端过滤器 可选接口 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {

  private Statistics statistics = Statistics.getInstance();

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    try {
      statistics.plusRequest();
      long b = System.currentTimeMillis();
      Result result = invoker.invoke(invocation);
      long cost = System.currentTimeMillis() - b;
      statistics.plusFinished();
      statistics.plusCost(cost);
//      System.out.println(invoker.getUrl().getAddress() + " cost: " + cost);
      return result;
    } catch (Exception e) {
      throw e;
    }

  }
}
