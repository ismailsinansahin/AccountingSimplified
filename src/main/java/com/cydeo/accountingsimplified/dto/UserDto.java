package com.cydeo.accountingsimplified.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

// we don't use lombok getter setter to be able to validate confirm password field.
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    //    @NotBlank // @Size is enough to check if it is not blank
    @Size(min = 2, max = 50)
    private String firstname;

    //    @NotBlank // @Size is enough to check if it is not blank
    @Size(min = 2, max = 50)
    private String lastname;

    @NotBlank   // @Email is not enough to check if it is not blank
    @Email
//    @Pattern()
    private String username;

    //    @NotBlank   // @Pattern is enough to check if it is not blank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    private String password;

    @NotNull
    private String confirmPassword;

    //    @NotBlank // @Pattern is enough to check if it is not blank
//    @Pattern(regexp = "^1-[0-9]{3}?-[0-9]{3}?-[0-9]{4}$")     //  format "1-xxx-xxx-xxxx"
    @Pattern(regexp = "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$", message = "Phone should be in proper format")
    private String phone;

    @NotNull
    private RoleDto role;

    //    @NotNull // it should be null if current user is not root user
    private CompanyDto company;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassword();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (password != null && !password.equals(confirmPassword)) {
            this.confirmPassword = null;
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }
}
