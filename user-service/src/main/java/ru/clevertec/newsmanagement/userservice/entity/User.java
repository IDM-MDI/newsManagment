package ru.clevertec.newsmanagement.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * An entity class representing a user.
 * @author Dayanch
 */
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User implements UserDetails {

    /**
     * The username of the user. It's unique and cannot be null.
     */
    @Id
    @Column(name = "username",nullable = false, unique = true)
    private String username;


    /**
     * The password of the user. Cannot be null.
     */
    @Column(name = "password",nullable = false)
    private String password;


    /**
     * The role of the user. Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false)
    private Role role;


    /**
     * Returns the authorities granted to the user.
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }


    /**
     * Returns the password used to authenticate the user.
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }


    /**
     * Returns the username used to authenticate the user.
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }


    /**
     * Returns whether the user's account has expired.
     * @return true if the user's account is valid (ie non-expired), false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * Returns whether the user is locked or not.
     * @return true if the user is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * Returns whether the user's credentials (password) have expired.
     * @return true if the user's credentials are valid (ie non-expired), false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * Returns whether the user is enabled or disabled.
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getUsername() != null && Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
