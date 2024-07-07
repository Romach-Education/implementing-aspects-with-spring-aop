package org.example;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MessageService {
    private Logger logger = Logger.getLogger(MessageService.class.getName());

    @ArgumentsLog
    public void processMessage(final String message) {
        logger.info("Processing message: " + message);
    }
}
