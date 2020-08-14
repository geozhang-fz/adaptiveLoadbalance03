package com.aliware.tianchi;

import com.aliware.tianchi.Context.Provider;
import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器 可选接口 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

  private static Context context = Context.getInstance();

  @Override
  public void receiveServerMsg(String msg) {
    try {

      int s = context.small();
      int m = context.mid();
      int l = context.large();

      String[] strings = msg.split(" ");

      int state = Integer.valueOf(strings[1]);
      ProviderStateEnum stateEnum = ProviderStateEnum.getFromValue(state);

      System.out.println(strings[0]
          + " " + stateEnum.toString()
          + " " + s
          + " " + m
          + " " + l);

      String provider = strings[0];

      if ("small".equals(provider)) {
        context.adjust(stateEnum, Provider.S);
      }
      if ("medium".equals(provider)) {
        context.adjust(stateEnum, Provider.M);
      }
      if ("large".equals(provider)) {
        context.adjust(stateEnum, Provider.L);
      }
    } catch (Exception e) {
      System.out.println(msg);
      // ignore
    }
  }

}
