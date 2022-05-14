package mk.ukim.finki.emagazine.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "blog_users")
@Data
public class User implements UserDetails {

    @Id
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL)
    private Photo profilePic;


    public User(String username, String name, String surname, String password,
                String email, LocalDate birthday, Role role, Photo profilePic) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.profilePic = profilePic;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
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
}
