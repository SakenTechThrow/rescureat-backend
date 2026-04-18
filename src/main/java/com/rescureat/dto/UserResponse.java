package com.rescureat.dto;

import com.rescureat.model.Role;
import com.rescureat.model.User;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public static UserResponse from(User user) {
        UserResponse u = new UserResponse();
        u.id = user.getId();
        u.name = user.getName();
        u.email = user.getEmail();
        u.role = user.getRole();
        return u;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
