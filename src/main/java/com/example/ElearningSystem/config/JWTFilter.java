package com.example.ElearningSystem.config;


import com.example.ElearningSystem.service.JWTService;
import com.example.ElearningSystem.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
@Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
     String authHeader=request.getHeader("Authorization");
     String token=null;
     String username=null;
     List<GrantedAuthority> authorities =null;

     if(authHeader!=null && authHeader.startsWith("Bearer ")){
         token=authHeader.substring(7);
         username=jwtService.extractUserName(token);
         List<String> roles=jwtService.extractRole(token);
          authorities = roles.stream()
                 .map(SimpleGrantedAuthority::new)  // <- important
                 .collect(Collectors.toList());
     }
     if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
         UserDetails userDetails=context.getBean(UserService.class).loadUserByUsername(username);
         if(jwtService.validateToken(token,userDetails)){
             UsernamePasswordAuthenticationToken authToken
                     =new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
             authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(authToken);

         }
     }
     filterChain.doFilter(request,response);
    }
}
