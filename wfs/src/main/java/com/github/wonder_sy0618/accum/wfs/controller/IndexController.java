package com.github.wonder_sy0618.accum.wfs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shiying on 2018/2/4.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @ResponseBody
    @RequestMapping("/index")
    public String index() {
        return "web file service running";
    }

}
