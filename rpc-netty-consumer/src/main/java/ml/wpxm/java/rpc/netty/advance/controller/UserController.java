package ml.wpxm.java.rpc.netty.advance.controller;

import ml.wpxm.java.rpc.netty.advance.annotation.WpRemoteReference;
import ml.wpxm.java.rpc.netty.advance.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @WpRemoteReference
    IUserService userService;

    @GetMapping("/save-user")
    public String saveUser(String userName){
        return userService.saveUser(userName);
    }

}
