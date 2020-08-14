package com.aliware.tianchi;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务 可选接口 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

  private ProviderManager providerManager = ProviderManager.getInstance();

  //
  private Timer timer = new Timer();

  /**
   * key: listener type value: callback listener
   */
  private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

  public CallbackServiceImpl() {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (!listeners.isEmpty()) {
          long finished = providerManager.getValidRequest();
          long cost = providerManager.getTimeSpent();
          long totalFinished = providerManager.getTotalValidRequest();
          long totalCost = providerManager.getTotalTimeSpent();

          ProviderStateEnum stateEnum = providerManager.setState(cost * 1.0 / finished, totalCost * 1.0 / totalFinished);

          for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
            try {
              // 发送：provider服务器的级别、工作状态ID
              String notifyStr = String.format("%s,%s", providerManager.getQuota(), stateEnum.getStateID());
              // entry.getValue()：获取到该provider服务器对应的监听器
              // 调用receiveServerMsg()方法推送provider服务器信息
              entry.getValue().receiveServerMsg(notifyStr);
            } catch (Throwable t1) {
              listeners.remove(entry.getKey());
            }
          }
        }
        providerManager.clear();
      }
    }, 250, 250);
  }


  @Override
  public void addListener(String key, CallbackListener listener) {
    listeners.put(key, listener);
    listener.receiveServerMsg(new Date().toString()); // send notification for change
  }

}
