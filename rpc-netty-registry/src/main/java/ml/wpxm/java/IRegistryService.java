package ml.wpxm.java;

public interface IRegistryService {

    /**
     * 服务注册
     */
    void register(ServiceInfo serviceInfo) throws Exception;

    /**
     * 服务发现
     * @param serviceName
     * @return ServiceInfo
     */
    ServiceInfo discovery(String serviceName) throws Exception;

}
