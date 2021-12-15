package com.skillbox.javapro21.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "is_blocked")
    private Integer isBlocked;

    @JsonIgnoreProperties(value = { "post", "comment", "person" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BlockHistory block;

    @ManyToOne
    @JsonIgnoreProperties(value = { "block", "likes", "files", "comments", "tags", "author" }, allowSetters = true)
    private Post post;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "blocksLists",
            "outFriendshipRequests",
            "incFriendshipRequests",
            "outMessages",
            "incMessages",
            "posts",
            "postLikes",
            "comments",
            "notifications",
        },
        allowSetters = true
    )
    private Person person;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostComment that = (PostComment) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
