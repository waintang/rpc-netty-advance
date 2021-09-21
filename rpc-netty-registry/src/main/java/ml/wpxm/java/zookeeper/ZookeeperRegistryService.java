package ml.wpxm.java.zookeeper;

import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.IRegistryService;
import ml.wpxm.java.ServiceInfo;
import ml.wpxm.java.loadbalance.ILoadBalance;
import ml.wpxm.java.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ZookeeperRegistryService implements IRegistryService {

    private static final String REGISTRY_PATH = "/registry";

    // 官方封装的 注册中心 工具类
    private final ServiceDiscovery<ServiceInfo> serviceDiscovery;

    private ILoadBalance<ServiceInstance<ServiceInfo>> loadBalance;

    public ZookeeperRegistryService(String registryAddress) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddress,new ExponentialBackoffRetry(1000,3));
        client.start();
        JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).client(client).serializer(serializer).basePath(REGISTRY_PATH).build();
        serviceDiscovery.start();
        this.loadBalance = new RandomLoadBalance();
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        log.info("begin registry serviceInfo to Zookeeper server");
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .address(serviceInfo.getServiceAddress())
                .port(serviceInfo.getServicePort())
                .name(serviceInfo.getServiceName())
                .payload(serviceInfo)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        log.info("begin discovery serviceInfo from Zookeeper server");
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = this.serviceDiscovery.queryForInstances(serviceName);
        ServiceInstance<ServiceInfo> serviceInstance = this.loadBalance.select((List)serviceInstances);
        if(serviceInstance == null){
            return null;
        }
        // 负载-动态路由
        return serviceInstance.getPayload();
    }
}
