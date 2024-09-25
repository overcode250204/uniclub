package com.cybersoft.uniclub06.filter;

import com.cybersoft.uniclub06.dto.AuthorityDTO;
import com.cybersoft.uniclub06.utils.JwtHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorHeader = request.getHeader("Authorization");
        if (authorHeader != null && authorHeader.startsWith("Bearer ")) {
            String token = authorHeader.substring(7);
            String data = jwtHelper.decodeToken(token);
            if (data != null) {
                List<AuthorityDTO> authorityDTOList = objectMapper.readValue(data, new TypeReference<List<AuthorityDTO>>() {
                });

                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (AuthorityDTO authorityDTO : authorityDTOList) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authorityDTO.getAuthority());
                    grantedAuthorities.add(simpleGrantedAuthority);
                }
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken("", "", grantedAuthorities);
                SecurityContext securityContext = SecurityContextHolder.getContext();// tạo ra giấy thông hành
                securityContext.setAuthentication(authenticationToken);
            }
            System.out.println("kiem tra " + token + " data" + data);
        }

        filterChain.doFilter(request, response);
    }
}
