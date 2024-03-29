package com.lcwd.electronic.store.ElectronicStore.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.ElectronicStore.dtos.JwtRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.JwtResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exception.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.security.JwtHelper;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;



@RestController
@RequestMapping("/auth")
@Api(value = "AuthController",description = "Rest Apis for Authentication!!")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manger;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${googleClientId}")
    private  String googleClientId;

    @Value("${newPassword}")
    private String newPassword;

    private Logger logger = (Logger) LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(),request.getPassWord());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        UserDto userDto = modelMapper.map(userDetails,UserDto.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto).build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String passWord) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,passWord);
        try{
            manger.authenticate(authentication);

        }catch(BadCredentialsException e){
            throw new BadApiRequestException("Invalid Username or Password !!");

        }
    }


    @GetMapping("/current")
    public ResponseEntity<String> getCurrentUser(Principal principal)
    {
        String name = principal.getName();
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

    //login with google api
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {
        //get the id token from request
        String idToken = data.get("idToken").toString();

        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));

        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        logger.info("Payload: {}",payload);

        String email = payload.getEmail();

        User user = null;
        User user1 = userService.findUserByEmailOptional(email).orElseThrow(null);
        if(user==null){
            //create new user
            user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
            
        }

        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).passWord(newPassword).build());
        return jwtResponseResponseEntity;


    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);
        return this.modelMapper.map(user,User.class);
    }


}
