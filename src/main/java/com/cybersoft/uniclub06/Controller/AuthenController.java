package com.cybersoft.uniclub06.Controller;

import com.cybersoft.uniclub06.reponse.BaseReponse;
import com.cybersoft.uniclub06.request.AuthenRequest;
import com.cybersoft.uniclub06.service.AuthenService;
import com.cybersoft.uniclub06.utils.JwtHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/authen")
public class AuthenController {
    @Autowired
    private AuthenService authenService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper = new ObjectMapper();// tranfer object to json

    @PostMapping
    public ResponseEntity<?> authen(@Valid @RequestBody AuthenRequest authenRequest) throws JsonProcessingException {
        //Tạo key
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        //Biến key thành chuỗi để lưu trữ lại
        String key = Encoders.BASE64.encode(secretKey.getEncoded());
//        boolean result = authenService.checkLogin(authenRequest); c1
        //gọi thằng authenticationManager để kêu nó chạy cái provider mà mình custom

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(authenRequest.email(), authenRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) authentication.getAuthorities();

        String data = objectMapper.writeValueAsString(grantedAuthorityList); // tranfer object to JSON
        String token = jwtHelper.generateToken(data);
        System.out.println(data);
        BaseReponse reponse = new BaseReponse();
        reponse.setData(token);


        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }



}
