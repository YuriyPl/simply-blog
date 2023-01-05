package com.github.ypl.simplyblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true, exclude = {"password", "entries", "comments"})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name = "user_unique_email_idx")})
public class User extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 32)
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @NotBlank
    @Size(max = 64)
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Size(min = 5, max = 128)
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 128)
    @Column(name = "description")
    private String description;

    @Column(name = "registered", nullable = false, columnDefinition = "date default now()", updatable = false)
    private LocalDate registered = LocalDate.now();

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Entry> entries;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<RefreshToken> tokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<ConfirmationToken> confirmationTokens;

    public User(Integer id, String name, String email, String password, String description, Role... roles) {
        this(id, name, email, password, description, Arrays.asList(roles), Collections.emptyList(), Collections.emptyList());
    }

    public User(String name, String email, String password, boolean enabled, Role... roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        setRoles(Arrays.asList(roles));
    }

    public User(Integer id, String name, String email, String password, String description,
                Collection<Role> roles, List<Entry> entries, List<Comment> comments) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.description = description;
        this.entries = entries;
        this.comments = comments;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
}