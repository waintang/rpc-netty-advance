package ml.wpxm.java.loadbalance;

import ml.wpxm.java.ServiceInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * 模板方法
 */
public abstract class AbstractLoadBalance implements ILoadBalance<ServiceInstance<ServiceInfo>> {

    @Override
    public ServiceInstance<ServiceInfo> select(List<ServiceInstance<ServiceInfo>> servers) {
        if(servers==null || servers.size()==0){
            return null;
        }
        if(servers.size() == 1){
            return servers.get(0);
        }
        return doSelect(servers);
    }

    protected  abstract ServiceInstance<ServiceInfo> doSelect(List<ServiceInstance<ServiceInfo>> servers);
}
