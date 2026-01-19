package com.codeb.ims.repository;
import com.codeb.ims.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectRepository extends JpaRepository<Project, Long> {}