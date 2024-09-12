package com.hermez.farrot.admin.repository;

import com.hermez.farrot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Member, Integer> {
    List<Member> findMemberByOrderById();
}