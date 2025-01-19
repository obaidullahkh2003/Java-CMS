package com.obaidullah.cms.dto;

import com.obaidullah.cms.model.Roles;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDto {
    private int id;
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Password is required", groups = {Update.class})
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = {Update.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one number", groups = {Update.class})
    private String password;
    @NotNull(message = "Password confirm is required", groups = {Update.class})
    private String confirmPassword;

    private Set<Long> roleIds;

    private boolean isActive;
    @NotNull(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    private Set<Roles> roles;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        } else {
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        if (confirmPassword != null && !confirmPassword.isEmpty()) {
            this.confirmPassword = confirmPassword;
        } else {
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
    public Set<Long> getRoleIds() {
        return roleIds;
    }
    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }


}
