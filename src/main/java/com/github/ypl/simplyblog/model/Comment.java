package com.github.ypl.simplyblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true, exclude = {"user", "entry"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comments")
public class Comment extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 320)
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "posted", nullable = false, columnDefinition = "timestamp(0) default now()")
    private LocalDateTime posted = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    @JsonIgnore
    private Entry entry;

    public Comment(Integer id, String text, LocalDateTime posted) {
        super(id);
        this.text = text;
        this.posted = posted;
    }
}
