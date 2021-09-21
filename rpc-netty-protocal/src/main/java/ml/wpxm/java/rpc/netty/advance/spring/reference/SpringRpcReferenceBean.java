package ml.wpxm.java.rpc.netty.advance.spring.reference;

import ml.wpxm.java.IRegistryService;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class SpringRpcReferenceBean implements FactoryBean {

    private Object object;
//    private String serverHost ;
//    private int serverPort;
    private Class<?> interfaceClass;

    private IRegistryService registryService;

    @Override
    public Object getObject() throws Exception {
        return this.object;
    }

    //曾犯错二：虽然类似于 new Bean()构造函数，但是 此处并不需要 return object
    public void init(){
        this.object = Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},new RpcInvokerProxy(registryService));
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }
//
//    public void setServerHost(String serverHost) {
//        this.serverHost = serverHost;
//    }
//
//    public void setServerPort(int serverPort) {
//        this.serverPort = serverPort;
//    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setRegistryService(IRegistryService registryService) {
        this.registryService = registryService;
    }
}
