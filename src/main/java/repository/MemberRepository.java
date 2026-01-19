package com.codeb.ims.repository;
import com.codeb.ims.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository extends JpaRepository<Member, Long> {}