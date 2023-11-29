package com.kanvan.team.repository;

import com.kanvan.team.domain.Member;
import com.kanvan.team.domain.Team;
import com.kanvan.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberAndTeam(User user, Team team);

    List<Member> findByMember(User user);

    List<Member> findByTeam(Team team);
}
