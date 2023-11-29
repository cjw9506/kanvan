package com.kanvan.team.domain;

import com.kanvan.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private TeamRole role;

    @Enumerated(EnumType.STRING)
    private Invite inviteStatus;

    @Builder
    public Member(User member, Team team, TeamRole role, Invite inviteStatus) {
        this.member = member;
        this.team = team;
        this.role = role;
        this.inviteStatus = inviteStatus;
    }

    public void updateInviteStatus(Invite inviteStatus) {
        this.inviteStatus = inviteStatus != null ? inviteStatus : this.inviteStatus;
    }
}
