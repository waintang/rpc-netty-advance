package ml.wpxm.java.rpc.netty.advance;

import ml.wpxm.java.rpc.netty.advance.service.IUserService;

public class App {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        IUserService userService = rpcClientProxy.clientProxy(IUserService.class,"127.0.0.1",8888);
        System.out.println(userService.saveUser("TWP"));

    }
}
