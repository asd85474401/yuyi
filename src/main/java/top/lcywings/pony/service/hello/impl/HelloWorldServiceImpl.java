package top.lcywings.pony.service.hello.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.lcywings.pony.service.common.BaseService;
import top.lcywings.pony.service.hello.HelloWorldService;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/02/23
 **/
@Service
@Slf4j
public class HelloWorldServiceImpl extends BaseService implements HelloWorldService {


    /**
     * sayHello
     *
     * @return hello
     * @author majuehao
     * @date 2022/2/23 10:28
     **/
    @Override
    public String sayHello() {
        return "hello";
    }
}
