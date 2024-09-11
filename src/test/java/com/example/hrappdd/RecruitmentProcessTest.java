package com.example.hrappdd;

import com.example.hrappdd.interview.domain.Interview;
import com.example.hrappdd.interview.domain.InterviewCandidate;
import com.example.hrappdd.interview.domain.InterviewRecruiter;
import com.example.hrappdd.process.domain.*;
import com.example.hrappdd.process.domain.Process;
import com.example.hrappdd.process.domain.step.PreparedStep;
import com.example.hrappdd.task.domain.Candidate;
import com.example.hrappdd.task.domain.Reviewer;
import com.example.hrappdd.task.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RecruitmentProcessTest {

    private ProcessCandidate candidate;
    private ProcessRecruiter techRecruiter;
    private ProcessRecruiter teamLead;
    private ProcessRecruiter peopleAndCulture;
    private List<ProcessRecruiter> recruiters;

    @BeforeEach
    void setUp() {
        initializeParticipants();
    }

    @Test
    void testFullRecruitmentProcess() {

        var recruitmentProcess = new Process(recruiters, candidate);
        assertEquals(ProcessState.INITIAL, recruitmentProcess.getProcessState());

        // Initial Interview Step
        var interviewStep = prepareInitialInterviewStep();
        recruitmentProcess.addStep(interviewStep);
        recruitmentProcess.start();
        assertNotNull(recruitmentProcess.getCurrentStep());

        var hrInterview = (Interview) recruitmentProcess.getCurrentStep().getStep();
        hrInterview.acceptParticipant(peopleAndCulture.getId());
        hrInterview.acceptParticipant(candidate.getId());
        hrInterview.process();
        hrInterview.finish(peopleAndCulture.getId(), true);

        // Task Step
        var task = prepareTask();
        recruitmentProcess.addStep(task);
        recruitmentProcess.nextStep();
        var taskStep = recruitmentProcess.getCurrentStep().getStep();
        assertInstanceOf(Task.class, taskStep);
        taskStep.process();
        taskStep.finish(teamLead.getId(), true);

        // Technical Interview Step
        var techInterviewStep = prepareTechInterviewStep();
        recruitmentProcess.addStep(techInterviewStep);
        recruitmentProcess.nextStep();
        var techInterview = (Interview) recruitmentProcess.getCurrentStep().getStep();
        techInterview.acceptParticipant(techRecruiter.getId());
        techInterview.acceptParticipant(candidate.getId());
        techInterview.process();
        techInterview.finish(techRecruiter.getId(), true);

        // Final Interview Step
        var finalInterviewStep = prepareFinalInterviewStep();
        recruitmentProcess.addStep(finalInterviewStep);
        recruitmentProcess.nextStep();
        var finalInterview = (Interview) recruitmentProcess.getCurrentStep().getStep();
        finalInterview.acceptParticipant(teamLead.getId());
        finalInterview.acceptParticipant(candidate.getId());
        finalInterview.process();
        finalInterview.finish(teamLead.getId(), true);
    }

    private void initializeParticipants() {
        candidate = new ProcessCandidate(UUID.randomUUID(), "candidate@example.com");
        techRecruiter = new ProcessRecruiter(UUID.randomUUID(), "techRecruiter@example.com");
        teamLead = new ProcessRecruiter(UUID.randomUUID(), "teamLead@example.com");
        peopleAndCulture = new ProcessRecruiter(UUID.randomUUID(), "peopleAndCulture@example.com");
        recruiters = List.of(techRecruiter, teamLead, peopleAndCulture);
    }

    private PreparedStep prepareTask() {
        var taskCandidate = new Candidate(candidate.getId(), candidate.getEmail());
        List<Reviewer> taskReviewers = recruiters.stream().map(e -> new Reviewer(e.getId(), e.getEmail())).toList();
        var taskDeadline = LocalDate.now().plusDays(1);
        var task = new Task(taskCandidate, taskReviewers, taskDeadline);
        return new PreparedStep(List.of(new Question("Solve this coding challenge.")), task);
    }

    private PreparedStep prepareInitialInterviewStep() {
        var interviewCandidate = new InterviewCandidate(candidate.getId(), candidate.getEmail());
        var interviewRecruiters = List.of(new InterviewRecruiter(peopleAndCulture.getId(), peopleAndCulture.getEmail()));
        var scheduleDate = LocalDateTime.now().plusDays(1);
        var interview = new Interview(scheduleDate, interviewCandidate, interviewRecruiters);
        return new PreparedStep(List.of(new Question("Tell me about yourself.")), interview);
    }

    private PreparedStep prepareTechInterviewStep() {
        var interviewCandidate = new InterviewCandidate(candidate.getId(), candidate.getEmail());
        var interviewRecruiters = List.of(new InterviewRecruiter(techRecruiter.getId(), techRecruiter.getEmail()));
        var scheduleDate = LocalDateTime.now().plusDays(1);
        var interview = new Interview(scheduleDate, interviewCandidate, interviewRecruiters);
        return new PreparedStep(List.of(new Question("What is your experience with software development?")), interview);
    }

    private PreparedStep prepareFinalInterviewStep() {
        var interviewCandidate = new InterviewCandidate(candidate.getId(), candidate.getEmail());
        var interviewRecruiters = List.of(new InterviewRecruiter(teamLead.getId(), teamLead.getEmail()));
        var scheduleDate = LocalDateTime.now().plusDays(1);
        var interview = new Interview(scheduleDate, interviewCandidate, interviewRecruiters);
        return new PreparedStep(List.of(new Question("What are your long-term career goals?")), interview);
    }
}
