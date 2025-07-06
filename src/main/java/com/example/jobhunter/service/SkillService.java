package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skills> handleSaveSkill(List<Skills> skills) {
        return skillRepository.saveAll(skills);
    }

    public void handleDeleteSkill(long id) {
        skillRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllSkill(Specification<Skills> spec, Pageable pageable) {
        Page<Skills> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageSkill.getNumber() + 1); // Page numbers are 0-based, so we add 1 for user-friendly display
        mt.setPageSize(pageSkill.getSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());

        return rs;

    }

    public Skills fetchOneSkill(long id) {
        Optional<Skills> skillOptional = skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public Skills handleUpdateSkill(Long id, Skills skill) {
        Skills curSkills = fetchOneSkill(id);
        if (curSkills != null) {
            curSkills.setName(skill.getName());
            curSkills = skillRepository.save(curSkills);
        }
        return curSkills;
    }
}
