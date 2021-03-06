package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author daofeng.xjf
 *
 * wrk -t4 -c1024 -d60s -T5 --script=./wrk.lua --latency http://localhost:8087/invoke mvn clean
 * install -Dmaven.test.skip=true 客户端过滤器 可选接口 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

  private static Context context = Context.getInstance();

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    try {
//      System.out.println("每次消息都调用我");
      Result result = invoker.invoke(invocation);

      return result;
    } catch (Exception e) {
      throw e;
    }

  }
}
