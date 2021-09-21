package ml.wpxm.java.rpc.netty.advance.spring.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.IRegistryService;
import ml.wpxm.java.RegistryFactory;
import ml.wpxm.java.RegistryType;
import ml.wpxm.java.ServiceInfo;
import ml.wpxm.java.rpc.netty.advance.annotation.WpRemoteService;
import ml.wpxm.java.rpc.netty.advance.protocol.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * 两个职责：
 * 1、启动服务端
 * 2、收集 有WpRemoteService的Bean实例
 */
@Slf4j
public class SpringRpcProviderBean implements InitializingBean, BeanPostProcessor {

    private final int serverPort;

    private final String serverHost;

    private final IRegistryService registryService;

    public SpringRpcProviderBean(String serverHost, int serverPort,IRegistryService registryService) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
        this.registryService = registryService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("begin deploy Netty Server to host{} ,on port{}",this.serverHost,this.serverPort);
        // 注意：new Runnable写法 不识别this.serverHost属性
        new Thread(()->{
                new NettyServer(this.serverHost,this.serverPort).startNettyServer();
            }).start();
    }

    /**
     * 筛选有 WpRemoteService注解的 类
     * 其方法 都收集起来（收集到 内存/中介者、zookeeper注册中心）
     *
     * 缺点：
     * 1、收集方法名 只能按 第一个interface去收集
     * 2、收集方法体 不支持重载
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(WpRemoteService.class)){
            Method[] methods = bean.getClass().getMethods();
            String interfaceName = bean.getClass().getInterfaces()[0].getName();
            for(Method method : methods){
                String key = interfaceName+"."+method.getName();
                ProviderBeanMethod providerBeanMethod = new ProviderBeanMethod();
                providerBeanMethod.setBean(bean);
                providerBeanMethod.setMethod(method);
                //收集到 中介者（负责 调用）
                ProviderMediator.providerBeanMethodMap.put(key,providerBeanMethod);

            }
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setServiceAddress(this.serverHost);
            serviceInfo.setServicePort(this.serverPort);
            serviceInfo.setServiceName(interfaceName);
            // 收集 注解接口 到 注册中心
            try {
                registryService.register(serviceInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
