package com.example.jobhunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.job.ResCreateJobDTO;
import com.example.jobhunter.dto.response.job.ResUpdateJobDTO;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResultPaginationDTO fetchAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageJob.getNumber() + 1);
        mt.setPageSize(pageJob.getSize());
        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());
        return rs;
    }

    public ResCreateJobDTO handleSaveJob(Job job) {
        // check job is exist
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skills> dbskills = skillRepository.findAllById(reqSkills);
            job.setSkills(dbskills);
        }
        Job curJob = jobRepository.save(job);
        // convert to dto
        ResCreateJobDTO rs = new ResCreateJobDTO();
        rs.setId(curJob.getId());
        rs.setName(curJob.getName());
        rs.setLocation(curJob.getLocation());
        rs.setSalary(curJob.getSalary());
        rs.setQuantity(curJob.getQuantity());
        rs.setLevel(curJob.getLevel());
        rs.setStartDate(curJob.getStartDate());
        rs.setEndDate(curJob.getEndDate());
        rs.setActive(curJob.isActive());
        rs.setCreatedAt(curJob.getCreatedAt());
        rs.setCreatedBy(curJob.getCreatedBy());

        if (curJob.getSkills() != null) {
            List<String> skillNames = curJob.getSkills()
                    .stream().map(x -> x.getName())
                    .collect(Collectors.toList());
            rs.setSkills(skillNames);
        }
        return rs;
    }

    public Optional<Job> fetchOneJob(Long id) {
        return jobRepository.findById(id);
    }

    public ResUpdateJobDTO handleUpdateJob(Long id, Job job) {
        Optional<Job> curJobOpt = fetchOneJob(id);
        if (!curJobOpt.isPresent()) {
            return null;
        }

        Job curJob = curJobOpt.get();
        curJob.setName(job.getName());
        curJob.setLocation(job.getLocation());
        curJob.setSalary(job.getSalary());
        curJob.setQuantity(job.getQuantity());
        curJob.setLevel(job.getLevel());
        curJob.setDescription(job.getDescription());
        curJob.setStartDate(job.getStartDate());
        curJob.setEndDate(job.getEndDate());
        curJob.setActive(job.isActive());

        // Handle skills similar to create
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skills> dbskills = skillRepository.findAllById(reqSkills);
            curJob.setSkills(dbskills);
        }

        Job updatedJob = jobRepository.save(curJob);

        // Convert to DTO similar to create
        ResUpdateJobDTO rs = new ResUpdateJobDTO();
        rs.setId(updatedJob.getId());
        rs.setName(updatedJob.getName());
        rs.setLocation(updatedJob.getLocation());
        rs.setSalary(updatedJob.getSalary());
        rs.setQuantity(updatedJob.getQuantity());
        rs.setLevel(updatedJob.getLevel());
        rs.setDescription(updatedJob.getDescription());
        rs.setStartDate(new java.sql.Date(updatedJob.getStartDate().getTime()));
        rs.setEndDate(new java.sql.Date(updatedJob.getEndDate().getTime()));
        rs.setActive(updatedJob.isActive());
        rs.setCreatedAt(updatedJob.getCreatedAt());
        rs.setCreatedBy(updatedJob.getCreatedBy());

        if (updatedJob.getSkills() != null) {
            List<String> skillNames = updatedJob.getSkills()
                    .stream().map(x -> x.getName())
                    .collect(Collectors.toList());
            rs.setSkills(skillNames);
        }
        return rs;
    }

    public void handleDeleteJob(Long id) {
        jobRepository.deleteById(id);
        
    }
}
