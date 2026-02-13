package com.ChirayuTech.journalApp.controller;

import com.ChirayuTech.journalApp.entity.User;
import com.ChirayuTech.journalApp.repository.UserRepository;
import com.ChirayuTech.journalApp.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> handleGoogleCallBack(@RequestParam String code){
        try {
           String tokenEndPoint= "https://oauth2.googleapis.com/token";

            Map<String, String> params = new HashMap<>();
            params.put("code",code);
            params.put("client_id",clientId);
            params.put("client_secret",clientSecret);
            params.put("redirect_uri","https://developers.google.com/oauthplayground");
            params.put("grant_type","authorization_code");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED)   ;

            HttpEntity<Map<String,String>> request =new HttpEntity<>(params,httpHeaders);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndPoint, request, Map.class);
            assert tokenResponse.getBody() != null;
            String idToken = tokenResponse.getBody().get("id_token").toString();
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token= " + idToken;

            ResponseEntity<Map> userInfo = restTemplate.getForEntity(userInfoUrl, Map.class);
            if(userInfo.getStatusCode() == HttpStatus.OK){
                Map<String,String> userInfoBody = userInfo.getBody();
                String email = userInfoBody.get("email");
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if(userDetails == null){
                    User user=new User();
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRoles(Arrays.asList("USER"));
                    userRepository.save(user);
                    userDetails=userDetailsService.loadUserByUsername(email);
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }catch (Exception e){
            log.error("Error Occurred during Google AuthLogin... ",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

}
