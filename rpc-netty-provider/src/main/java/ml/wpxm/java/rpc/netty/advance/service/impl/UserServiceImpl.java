package ml.wpxm.java.rpc.netty.advance.service.impl;

import ml.wpxm.java.rpc.netty.advance.annotation.WpRemoteService;
import ml.wpxm.java.rpc.netty.advance.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 不妥当一：WpRemoteService 不能替代Service吧？本身自己调用不到本service了
 */
@WpRemoteService
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        return "保存用户成功："+name;
    }
}
