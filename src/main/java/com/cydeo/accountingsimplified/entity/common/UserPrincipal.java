package com.cydeo.accountingsimplified.entity.common;

import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(@Lazy User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().getDescription());
        authorityList.add(authority);
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getCompany().getCompanyStatus().equals(CompanyStatus.PASSIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Long getId() {
        return this.user.getId();
    }

    /**
     * to show logged-in user firstname and lastname in UI dropdown menu
      */
    public String getFullNameForProfile() {
        return this.user.getFirstname() + " " + this.user.getLastname();
    }

    /**
     * This method is defined to show logged-in user's company title for simplicity
     *
     * @return The title of logged-in user's Company in String
     */
    public String getCompanyTitleForProfile() {
        return this.user.getCompany().getTitle().toUpperCase();
    }
}
