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

    @Value("${twilio_accountSid}")
    private String accountSid;

    @Value("${twilio_authToken}")
    private String authToken;

    @Value("${twilio_fromNumber}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && authToken != null) {
            Twilio.init(accountSid, authToken);
        }
    }

    public void sendWhatsapp(String to, String content) {
        String from = "whatsapp:"+fromNumber;
        to = "whatsapp:"+to;
        try {

            Message message = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(from),
                            (String) null
                    )
                    .setContentSid("HX229f5a04fd0510ce1b071852155d3e75")
                    .setContentVariables(new JSONObject(new HashMap<String, Object>() {{
                        put("1", content);
                    }}).toString())
                    .create();

            log.info("send wa to ={} sid={}", to, message.getSid());
        }catch (Exception e){
            log.error("fail to send wa {} {} {} {}", accountSid, authToken, fromNumber, to, e);
        }

    }
}