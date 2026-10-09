package com.foodwaste.food_waste.dto;

import com.foodwaste.food_waste.entity.User;

public class AuthResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String address;
    private String organizationType;
    private String message;

    public AuthResponse() {}

    public AuthResponse(User user, String message) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.phone = user.getPhone();
            this.address = user.getAddress();
            this.organizationType = user.getOrganizationType();
        }
        this.message = message;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
