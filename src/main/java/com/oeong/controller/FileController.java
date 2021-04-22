package com.oeong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping("/index")
    public String fileIndex() {
        return "file/list";
    }
}
