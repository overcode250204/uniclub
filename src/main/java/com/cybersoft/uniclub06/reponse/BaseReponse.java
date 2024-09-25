package com.cybersoft.uniclub06.reponse;

import lombok.Data;

@Data
public class BaseReponse {
    private int statusCode = 200;
    private String statusMessage;
    private Object data;
}
