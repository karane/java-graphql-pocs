package org.karane.graphql.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class UserController {

    @QueryMapping
    public String me(Principal principal) {
        if (principal == null) {
            return "anonymous";
        }
        return principal.getName();
    }
}
