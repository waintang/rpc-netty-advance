package ml.wpxm.java.rpc.netty.advance.spring.service;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ProviderBeanMethod {
    private Object bean;
    private Method method;
}
