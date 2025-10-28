package com.example.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jobhunter.domain.Company;
import com.example.jobhunter.domain.SkillRepository;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.service.SkillService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final SkillService skillService;
    private final SkillRepository skillRepository;

    public SkillController(SkillService skillService, SkillRepository skillRepository) {
        this.skillService = skillService;
        this.skillRepository = skillRepository;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter Specification<Skills> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(skillService.fetchAllSkill(spec, pageable));
    }

    @PostMapping()
    public ResponseEntity<List<Skills>> createSkill(@Valid @RequestBody List<Skills> skills) {

        List<Skills> newSkills =  skillService.handleSaveSkill(skills);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkills);   
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skills> updateSkill(@PathVariable("id") Long id, @Valid @RequestBody Skills skill) {
        Skills updatedSkill = skillService.handleUpdateSkill(id, skill);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSkill);
    }

    /**
     * Deletes a skill by its ID.
     * <p>
     * This method first retrieves the skill from the repository. If found, it removes
     * the skill from all associated jobs to maintain referential integrity, then deletes
     * the skill using the service layer. Returns a success message upon completion.
     *
     * @param id the ID of the skill to delete
     * @return ResponseEntity with a success message and HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable("id") long id) {
        Optional<Skills> skillOptional = skillRepository.findById(id);

        
        Skills currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> {
            job.getSkills().remove(currentSkill);
        });
        skillService.handleDeleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).body("Skill with ID " + id + " has been deleted successfully.");
    }
    
}
