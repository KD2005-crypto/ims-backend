package com.codeb.ims.controller;

import com.codeb.ims.dto.ProjectRequest;
import com.codeb.ims.entity.Member;
import com.codeb.ims.entity.Project;
import com.codeb.ims.repository.MemberRepository;
import com.codeb.ims.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class ProjectController {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private MemberRepository memberRepository;

    // 1. GET ALL
    @GetMapping
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    // 2. CREATE PROJECT (With Members!)
    @PostMapping
    public ResponseEntity<?> addProject(@RequestBody ProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setBudget(request.getBudget());
        project.setCompletion(request.getCompletion());
        // Default Logo for now
        project.setLogoPath("/logo-slack.svg");

        // Find the members by their IDs and attach them
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<Member> members = memberRepository.findAllById(request.getMemberIds());
            project.setMembers(members);
        }

        return ResponseEntity.ok(projectRepository.save(project));
    }

    // 3. UPDATE PROJECT (Change Progress / Members)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        return projectRepository.findById(id).map(project -> {
            project.setName(request.getName());
            project.setBudget(request.getBudget());
            project.setCompletion(request.getCompletion());

            // Update Members list
            if (request.getMemberIds() != null) {
                List<Member> members = memberRepository.findAllById(request.getMemberIds());
                project.setMembers(members);
            }

            return ResponseEntity.ok(projectRepository.save(project));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. DELETE PROJECT
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }
}