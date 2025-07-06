package com.example.jobhunter.controller;

import com.example.jobhunter.domain.Job;
import com.example.jobhunter.domain.Skills;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.job.ResCreateJobDTO;
import com.example.jobhunter.dto.response.job.ResUpdateJobDTO;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;
import com.example.jobhunter.service.JobService;
import com.example.jobhunter.util.error.IdInvalidException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.turkraft.springfilter.boot.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/jobs")

public class JobController {
    private final JobService jobService;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public JobController(JobService jobService, JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobService = jobService;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.fetchAllJob(spec, pageable));
    }

    @PostMapping
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.handleSaveJob(job));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@PathVariable("id") Long id, @RequestBody Job job)
            throws IdInvalidException {
        ResUpdateJobDTO updatedJob = jobService.handleUpdateJob(id, job);
        if (updatedJob == null) {
            throw new IdInvalidException("job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Job> job = jobService.fetchOneJob(id);
        if (!job.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(job.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") Long id) throws IdInvalidException {
        Optional<Job> job = jobService.fetchOneJob(id);
        if (!job.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
