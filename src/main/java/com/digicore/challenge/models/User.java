package com.digicore.challenge.models;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private String acoountNumber;

    private String accountPassword;

    private String accountName;

    public User(String username) {
	this.acoountNumber = username;
    }

    @Override
    public String getUsername() {
	return acoountNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
	return false;
    }

    @Override
    public boolean isAccountNonLocked() {
	return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return new ArrayList<>();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
	return accountPassword;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

}
