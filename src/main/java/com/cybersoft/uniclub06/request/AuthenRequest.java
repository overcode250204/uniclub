package com.cybersoft.uniclub06.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenRequest (
        @NotNull(message = "Email cannot null")
        @NotEmpty(message = "Email cannot empty") // dành cho chuỗi
        @Email(message = "Not fomat email")
        String email,
        @NotNull(message = "Password cannot null")
        @NotEmpty(message = "Password cannot empty")
        String password) {

}
