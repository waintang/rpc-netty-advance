package ml.wpxm.java.rpc.netty.advance.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RequestHolder {
    public static final AtomicLong REQUEST_ID = new AtomicLong();
    public static final ConcurrentHashMap<Long,RpcFuture> REQUEST_MAP = new ConcurrentHashMap();
}
