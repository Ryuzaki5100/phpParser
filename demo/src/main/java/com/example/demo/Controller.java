package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    @GetMapping("/parse")
    public String parse(@RequestBody String phpCode) throws IOException, InterruptedException {
        return PhpParserBridge.runPhpParser(phpCode);
    }
}
