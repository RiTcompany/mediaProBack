package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.ERole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private ERole role;

    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @Column(name = "stars")
    private Integer stars = 0;

    @Column(name = "tg_id", nullable = true)
    private Long tgId;

    @Column(name = "streak")
    private Integer streak = 0;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    @Column(name = "is_subscribed_to_news", nullable = false)
    private Boolean newsSubscribed;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    public User(String email, String username, String password, ERole role, Boolean newsSubscribed) {
        super();
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.newsSubscribed = newsSubscribed;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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

    public String getUsername() {
        return email;
    }

    public String getName() {
        return username;
    }

    public int addStreak() {
        this.streak++;
        return this.streak;
    }

    public int addStars() {
        this.stars++;
        return this.stars;
    }

    public boolean hasTelegramId() {
        return tgId != null;
    }
}
