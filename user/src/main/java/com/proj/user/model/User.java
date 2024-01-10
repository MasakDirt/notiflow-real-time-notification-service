package com.proj.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Fill in the field with the name.")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
    @Column(name = "e-mail", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "The password cannot be 'blank'")
    @Column(nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = "^@.*", message = "The telegram account must started with '@' character")
    private String telegram;

    @NotNull
    private NotificationType notificationType;

//    @ManyToOne()
//    private final Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(fullName, user.fullName) &&
                Objects.equals(email, user.email) && Objects.equals(password, user.password)
                && Objects.equals(telegram, user.telegram) && notificationType == user.notificationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, password, telegram, notificationType);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telegram='" + telegram + '\'' +
                ", notificationType=" + notificationType +
                '}';
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return role;
        return null;
    }
}
