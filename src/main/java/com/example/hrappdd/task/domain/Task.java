package com.example.hrappdd.task.domain;

import com.example.hrappdd.Participant;
import com.example.hrappdd.interview.domain.InterviewState;
import com.example.hrappdd.process.domain.step.Step;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task implements Step {
    private Candidate candidate;
    private List<Reviewer> reviewers;
    private LocalDate deadline;
    private TaskState taskState;
    private boolean pass;
    private Set<UUID> acceptedByParticipants;

    @Override
    public boolean isCompleted() {
        return taskState == TaskState.COMPLETED;
    }

    public boolean isPass() {
        return this.pass;
    }

    @Override
    public void process() {
        taskState = TaskState.IN_PROGRESS;
    }

    @Override
    public void start() {
        sendTaskToCandidate();
    }

    @Override
    public void finish(UUID recruiterId, boolean pass) {
        if (taskState != TaskState.IN_PROGRESS) {
            throw new IllegalStateException("Interview didn't start");
        }

        reviewers.stream()
                .filter(p -> p.getId().equals(recruiterId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found: " + recruiterId));

        this.pass = pass;
        taskState = TaskState.COMPLETED;
    }

    @Override
    public List<Participant> getParticipants() {
        return Stream.concat(Stream.of(candidate), reviewers.stream())
                .collect(Collectors.toList());
    }

    public Task(Candidate candidate, List<Reviewer> reviewers, LocalDate deadline) {
        this.candidate = candidate;
        this.reviewers = reviewers;
        this.deadline = deadline;
        this.taskState = TaskState.INITIALIZED;
        this.acceptedByParticipants = new HashSet<>();
    }

    @Override
    public void acceptParticipant(UUID participantId) {
        if (getParticipants().stream().noneMatch(p -> p.getId().equals(participantId))) {
            throw new IllegalArgumentException("Participant with ID " + participantId + " is not part of this task.");
        }

        if (acceptedByParticipants.contains(participantId)) {
            return;
        }

        acceptedByParticipants.add(participantId);

        if (acceptedByParticipants.size() == getParticipants().size()) {
            taskState = TaskState.READY;
        }
    }

    public void sendTaskToCandidate() {
        taskState = TaskState.PENDING;
    }


    public boolean isPastDeadline() {
        return LocalDate.now().isAfter(deadline);
    }

    public void manuallyCompleteTaskByRecruiter() {

    }
}

