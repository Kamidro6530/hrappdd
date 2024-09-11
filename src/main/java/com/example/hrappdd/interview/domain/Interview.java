package com.example.hrappdd.interview.domain;

import com.example.hrappdd.Participant;
import com.example.hrappdd.process.domain.step.Step;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Interview implements Step {
    private LocalDateTime scheduleDate;
    InterviewCandidate interviewCandidate;
    List<InterviewRecruiter> interviewRecruiters;
    private InterviewState interviewState;
    private boolean pass;
    private final Set<UUID> acceptedByParticipants;

    public Interview(LocalDateTime scheduleDate, InterviewCandidate interviewCandidate, List<InterviewRecruiter> interviewRecruiters) {
        this.scheduleDate = scheduleDate;
        this.interviewCandidate = interviewCandidate;
        this.interviewRecruiters = interviewRecruiters;
        this.acceptedByParticipants = new HashSet<>();
        this.interviewState = InterviewState.INITIAL;
    }

    @Override
    public boolean isCompleted() {
        return interviewState == InterviewState.COMPLETED;
    }

    @Override
    public boolean isPass() {
        return this.pass;
    }

    @Override
    public void process() {
        interviewState = InterviewState.IN_PROGRESS;
    }

    @Override
    public void start() {
        sendInvitation();
    }

    @Override
    public void finish(UUID recruiterId, boolean pass) {
        if (interviewState != InterviewState.IN_PROGRESS) {
            throw new IllegalStateException("Task didn't start");
        }

        interviewRecruiters.stream()
                .filter(p -> p.getId().equals(recruiterId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found: " + recruiterId));

        this.pass = pass;
        interviewState = InterviewState.COMPLETED;
    }

    @Override
    public List<Participant> getParticipants() {
        return Stream.concat(Stream.of(interviewCandidate), interviewRecruiters.stream())
                .collect(Collectors.toList());
    }

    @Override
    public void acceptParticipant(UUID participantId) {
        acceptInvitation(participantId);
    }

    void sendInvitation() {
        if (scheduleDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule an interview in the past.");
        }

        getParticipants().forEach(Participant::sendInvitation);
        interviewState = InterviewState.PENDING;
    }

    void acceptInvitation(UUID participantId) {
        if (interviewState != InterviewState.PENDING) {
            throw new IllegalStateException("Invitations can only be accepted in the PENDING state.");
        }

        var participant = findParticipant(participantId);
        participant.acceptInvitation();

        if (allInvitationsAccepted()) {
            interviewState = InterviewState.ACCEPTED;
        }
    }

    private Participant findParticipant(UUID participantId) {
        return getParticipants().stream()
                .filter(p -> p.getId().equals(participantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + participantId));
    }

    private boolean allInvitationsAccepted() {
        return getParticipants().stream().allMatch(Participant::isInvitationAccepted);
    }
}

