package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

// we don't use lombok getter setter to be able to validate confirm password field.
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastname;

    @NotBlank
    @Email
//    @Pattern("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
//    @Pattern("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@(?!-)[a-zA-Z0-9.-]+$")    // email validation permitted by RFC 5322
    private String username;


    /*
      @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])" +
                    "(?=.*([ -/]|[:-@]|[Z-_]|[{-~]))(?=\\S+$).{4,}$",
            message = "Password must contain at least: " +
                    "1 lowercase, " +
                    "1 uppercase, " +
                    "1 special, " +
                    "1 digit")
     */
//    @NotBlank   // @Pattern is enough to check if it is not blank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}")
    private String password;

    @NotNull
    private String confirmPassword;

//    @NotBlank // @Pattern is enough to check if it is not blank
//    @Pattern(regexp = "^1-[0-9]{3}?-[0-9]{3}?-[0-9]{4}$")                         //  format "1-xxx-xxx-xxxx"
//    imported from https://www.baeldung.com/java-regex-validate-phone-numbers :
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")                     // +111 123 45 67 89
    private String phone;

//    @NotNull
    private RoleDto role;

//    @NotNull
    private CompanyDto company;

    private boolean isOnlyAdmin;


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

    public Boolean getIsOnlyAdmin() {
        return isOnlyAdmin;
    }

    public void setIsOnlyAdmin(Boolean isOnlyAdmin) {
        this.isOnlyAdmin = isOnlyAdmin;
    }
}
