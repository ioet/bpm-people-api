package com.ioet.bpm.people.boundaries;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @RequestMapping(value = "/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
