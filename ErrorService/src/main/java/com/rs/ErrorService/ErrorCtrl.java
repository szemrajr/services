package com.rs.ErrorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ErrorCtrl {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public List<String> list(){
        return fileService.readFromFile();
    }
}
