package ml.wpxm.java.rpc.netty.advance.spring.reference;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.advance.annotation.WpRemoteReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpringRpcReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext applicationContext;
    private ClassLoader classLoader;

    private RpcClientProperties rpcClientProperties ;
    private static Map<String,BeanDefinition> rpcRefBeanDefinition = new ConcurrentHashMap<String,BeanDefinition>();

    public SpringRpcReferencePostProcessor(RpcClientProperties rpcClientProperties ){
        this.rpcClientProperties = rpcClientProperties;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 功能：spring扫描到 bean的定义，但还没实例化bean前 调用的方法
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        for (String beanDefinitionName : configurableListableBeanFactory.getBeanDefinitionNames()){
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            // 不妥当四：这里的beanClassName会为空么？
            if(beanClassName != null){
                Class<?> clz = ClassUtils.resolveClassName(beanClassName,this.classLoader);
                ReflectionUtils.doWithFields(clz,this::parseRpcReference);
            }
        }

        // 类实现了两个接口：BeanDefinitionRegistry、ConfigurableListableBeanFactory
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) configurableListableBeanFactory;
        rpcRefBeanDefinition.forEach((beanName,BeanDefinition)->{
            if(applicationContext.containsBean(beanName)){
                log.info("Spring Context already has bean {}",beanName);
                return ;
            }
            registry.registerBeanDefinition(beanName,BeanDefinition);
            log.info("Spring Context registered bean {}",beanName);
        });

    }

    private void parseRpcReference(Field field){

        WpRemoteReference wpRemoteReference = AnnotationUtils.findAnnotation(field, WpRemoteReference.class);
        if(wpRemoteReference!=null){
            // 按我理解：这里是 除new以外，第二种实例化bean的方法
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SpringRpcReferenceBean.class);
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass",field.getType());
            builder.addPropertyValue("serverHost",rpcClientProperties.getServerHost());
            builder.addPropertyValue("serverPort",rpcClientProperties.getServerPort());
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinition.put(field.getName(),beanDefinition);
        }

    }
}
