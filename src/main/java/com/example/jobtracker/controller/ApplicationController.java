package com.example.jobtracker.controller;

import com.example.jobtracker.model.Application;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.ApplicationRepository;
import com.example.jobtracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationRepository repository;
    private final UserRepository userRepository;

    public ApplicationController(ApplicationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

    // Returns the application only if it belongs to the logged-in user, otherwise 404
    private Application getOwnedApplication(Long id, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Application app = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (app.getUser() == null || !app.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return app;
    }

    @GetMapping
    public List<Application> getAll(Authentication authentication) {
        return repository.findByUser(getCurrentUser(authentication));
    }

    @PostMapping
    public Application create(@RequestBody Application application, Authentication authentication) {
        application.setId(null);
        application.setUser(getCurrentUser(authentication));
        return repository.save(application);
    }

    @PutMapping("/{id}")
    public Application update(@PathVariable Long id, @RequestBody Application updated, Authentication authentication) {
        Application app = getOwnedApplication(id, authentication);
        app.setCompanyName(updated.getCompanyName());
        app.setPosition(updated.getPosition());
        app.setStatus(updated.getStatus());
        app.setAppliedDate(updated.getAppliedDate());
        app.setNotes(updated.getNotes());
        app.setJobUrl(updated.getJobUrl());
        return repository.save(app);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication authentication) {
        repository.delete(getOwnedApplication(id, authentication));
    }
}