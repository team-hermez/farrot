package com.hermez.farrot.admin.repository;

import com.hermez.farrot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Member, Integer>, AdminRepositoryCustom {
    List<Member> findMemberByOrderById();
}