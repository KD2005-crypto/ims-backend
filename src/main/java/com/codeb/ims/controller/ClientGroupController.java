package com.codeb.ims.controller;

import com.codeb.ims.entity.ClientGroup;
import com.codeb.ims.service.ClientGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "https://ims-frontend-psi.vercel.app")
public class ClientGroupController {

    @Autowired
    private ClientGroupService service;

    @GetMapping
    public List<ClientGroup> getAll() {
        return service.getAllGroups();
    }

    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody Map<String, String> payload) {
        try {
            return ResponseEntity.ok(service.addGroup(payload.get("groupName")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable Long id) {
        service.deleteGroup(id);
    }
}