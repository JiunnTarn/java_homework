package com.team9.project.controllers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.team9.project.models.User;
import com.team9.project.models.UserType;
import com.team9.project.payload.request.SignupRequest;
import com.team9.project.repository.UserRepository;
import com.team9.project.repository.UserTypeRepository;
import com.team9.project.models.EUserType;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.util.CommonMethod;
import com.team9.project.payload.request.LoginRequest;
import com.team9.project.payload.response.JwtResponse;
import com.team9.project.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team9.project.security.jwt.JwtUtils;
import com.team9.project.security.services.UserDetailsImpl;
import org.yaml.snakeyaml.Yaml;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles.get(0)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();

        if (strRoles == null) {
            UserType userRole = userTypeRepository.findByName(EUserType.ROLE_USER);
            user.setUserType(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserType adminRole = userTypeRepository.findByName(EUserType.ROLE_ADMIN);
                        user.setUserType(adminRole);

                        break;
                    default:
                        UserType userRole = userTypeRepository.findByName(EUserType.ROLE_USER);
                        user.setUserType(userRole);
                }
            });
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/getUimsConfig")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getUimsConfig(@Valid @RequestBody DataRequest dataRequest) {
        Map data = new HashMap();;
        InputStream in = null;
        try {
            Yaml yaml = new Yaml();
            Resource resource = resourceLoader.getResource("classpath:uims.yml");
            in = resource.getInputStream();
            data =(Map)yaml.load(in);
        }catch(Exception e){

        }
        return CommonMethod.getReturnData(data);
    }

}