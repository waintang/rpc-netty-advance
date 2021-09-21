package ml.wpxm.java.rpc.netty.advance.spring.service;

import ml.wpxm.java.rpc.netty.advance.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 【服务方 中介者模式】
 * 上游只管生成/收集，下游只管调用
 *
 * 此处，负责 撮合/中介 运行（process）
 */
public class ProviderMediator {

    // 不妥当二：这里用ConcurrentHashMap似乎 没太大必要？
    public static Map<String,ProviderBeanMethod> providerBeanMethodMap = new ConcurrentHashMap<>();

    // 多线程 都用到这个变量，则要考虑  主内存/工作内存 关系
    private volatile static ProviderMediator instance = null;

    private ProviderMediator(){}

    // 不妥当三： 这里单例模式 似乎没什么太大必要？直接 process 变成static静态方法不好么？
    public static ProviderMediator getInstance(){
        if(instance == null){
            synchronized (ProviderMediator.class){
                if(instance == null){
                    instance = new ProviderMediator();
                }
            }
        }
        return instance;
    }

    /**
     * 承接 远程调用（请求格式 RpcRequest）
     * @param rpcRequest
     * @return
     */
    public static Object process(RpcRequest rpcRequest){
        String key = rpcRequest.getClassName()+"."+rpcRequest.getMethodName();
        ProviderBeanMethod providerBeanMethod = providerBeanMethodMap.get(key);

        if(providerBeanMethod != null){
            try {
                return providerBeanMethod.getMethod().invoke(providerBeanMethod.getBean(),rpcRequest.getParams());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
