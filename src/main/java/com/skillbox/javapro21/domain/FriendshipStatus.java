package com.skillbox.javapro21.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.skillbox.javapro21.domain.enumeration.FriendshipStatusType;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friendship_statuses")
public class FriendshipStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time")
    private ZonedDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "friendship_status_type")
    private FriendshipStatusType friendshipStatusType;

    @JsonIgnoreProperties(value = {"friendshipStatus", "srcPerson", "dstPerson"}, allowSetters = true)
    @OneToOne(mappedBy = "friendshipStatus")
    private Friendship friendship;
}
