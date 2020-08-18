package com.aliware.tianchi;

import com.aliware.tianchi.Context.Provider;
import org.apache.dubbo.rpc.listener.CallbackListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器 可选接口 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

  private static Context context = Context.getInstance();

  @Override
  public void receiveServerMsg(String msg) {
    try {
      String[] msgs = msg.split(",");

      String quota = msgs[0];
      int stateID = Integer.valueOf(msgs[1]);

      ProviderStateEnum stateEnum = ProviderStateEnum.getStateFromID(stateID);

      int sCurWeight = context.getsCurWeight();
      int mCurWeight = context.getmCurWeight();
      int lCurWeight = context.getlCurWeight();

      Date now = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String nowStr = sdf.format(now);

      System.out.println(String.format(
              "【%s】Gateway说，provider级别：%s，当前状态：%s",
              nowStr, quota, stateEnum.toString())
      );

      System.out.println(String.format(
              "当前动态权重，sCurWeight = %s, mCurWeight = %s, lCurWeight = %s",
              sCurWeight, mCurWeight, lCurWeight)
      );

      /* 调整动态权重 */
      // 如果级别是small，那就调整对应的动态权重sCurWeight
      if ("small".equals(quota)) {
        context.adjust(stateEnum, Provider.S);
      }
      if ("medium".equals(quota)) {
        context.adjust(stateEnum, Provider.M);
      }
      if ("large".equals(quota)) {
        context.adjust(stateEnum, Provider.L);
      }
    } catch (Exception e) {
      System.out.println(msg);
    }
  }

}
