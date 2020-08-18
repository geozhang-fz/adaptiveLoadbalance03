package com.aliware.tianchi;

import java.text.SimpleDateFormat;
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
                    long validRequest = providerManager.getValidRequest();
                    long timeSpent = providerManager.getTimeSpent();
                    long totalValidRequest = providerManager.getTotalValidRequest();
                    long totalTimeSpent = providerManager.getTotalTimeSpent();

                    System.out.println(String.format(
                            "validRequest:%s, timeSpent:%s, totalValidRequest:%s, totalTimeSpent:%s",
                            validRequest, timeSpent, totalValidRequest, totalTimeSpent));

                    ProviderStateEnum stateEnum = providerManager.updateState(
                            timeSpent * 1.0 / validRequest,
                            totalTimeSpent * 1.0 / totalValidRequest);

                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowStr = sdf.format(now);

                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            // 发送：provider服务器的级别、工作状态ID
                            String quota = providerManager.getQuota();
                            int stateID = stateEnum.getStateID();

                            String notifyStr = String.format("%s,%s", quota, stateID);
                            System.out.println(String.format(
                                    "【%s】%s级的服务器，当前处于%s级的状态。",
                                    nowStr, quota, stateID));

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
        }, 0, 2000);
    }


    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
//        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }

}
