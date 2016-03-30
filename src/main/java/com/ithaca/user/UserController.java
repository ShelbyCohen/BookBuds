package com.ithaca.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 3/24/2016.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserHelper userHelper;

    @RequestMapping
    public List<User> all() {
        return userService.all();
    }

    @RequestMapping("/account")
    public User find(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        Integer id = (Integer) claims.get("id");

        return userService.find(id.longValue());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, String> create(String name, String password) {

        User user = userService.create(name, password);
        if (user == null) {
            return null;
        }
        return userHelper.generateToken(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, String> login(String name, String password) {

        User user = userService.checkValid(name, password);
        if (user == null) {
            return null;
        }
        return userHelper.generateToken(user);
    }
}