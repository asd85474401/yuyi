package top.lcywings.pony.controller.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lcywings.pony.common.result.Result;
import top.lcywings.pony.controller.common.BaseController;
import top.lcywings.pony.service.hello.HelloWorldService;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/02/23
 **/
@Slf4j
@RestController
@RequestMapping("/HelloWorld")
public class HelloWorldController extends BaseController {

    @Resource
    private HelloWorldService helloWorldService;

    /**
     * HelloWorld
     *
     * @return 介绍
     * @author majuehao
     * @date 2021/12/21 13:31
     */
    @GetMapping(value = "/sayHello")
    public Result<String> sayHello() {
        this.saveInfoLog();
        return new Result<String>().successMsg(helloWorldService.sayHello());
    }
}
