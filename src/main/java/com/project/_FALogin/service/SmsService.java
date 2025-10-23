package com.project._FALogin.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service @Slf4j
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && authToken != null) {
            Twilio.init(accountSid, authToken);
        }
    }

    public void sendWhatsapp(String to, String content) {
        Message message = Message.creator(
                                new PhoneNumber("whatsapp:"+fromNumber),
                                new PhoneNumber("whatsapp:"+to),
                                (String) null
                        )
                        .setContentSid("HX229f5a04fd0510ce1b071852155d3e75")
                        .setContentVariables(new JSONObject(new HashMap<String, Object>() {{
                            put("1", content);
                        }}).toString())
                        .create();

        log.info("send wa to ={} sid={}", to, message.getSid());
    }
}