package com.rs.ErrorService;

import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageListener implements MessageListener<RuntimeException> {

    @Autowired
    private FileService fileService;

    @Override
    public void onMessage(CharSequence charSequence, RuntimeException o) {
           fileService.writeToFile(o.getMessage());
    }
}
