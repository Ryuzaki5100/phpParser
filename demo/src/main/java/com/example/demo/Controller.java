package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/php")
public class Controller {

    @Autowired
    private PhpParserService phpParserService;

    @PostMapping("/parse")
    public PhpFileStructure parse(@RequestBody String phpCode) throws Exception {
        return phpParserService.parsePhpCode(phpCode);
    }

    @GetMapping("/lol")
    public String lol() {
        return "LOL";
    }
}