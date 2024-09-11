package com.example.hrappdd.process.domain;


import com.example.hrappdd.process.domain.step.PreparedStep;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Process {
    private Queue<PreparedStep> queue;
    private PreparedStep currentStep;
    private ProcessState processState;
    private List<ProcessRecruiter> processRecruiters;
    private ProcessCandidate processCandidate;

    public Process(List<ProcessRecruiter> processRecruiters, ProcessCandidate processCandidate) {
        queue = new LinkedList<>();
        this.processRecruiters = processRecruiters;
        this.processCandidate = processCandidate;
        processState = ProcessState.INITIAL;
    }

    public void addStep(PreparedStep step) {
        queue.add(step);
    }

    public void start() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Cannot start the process because no steps are defined.");
        }

        processState = ProcessState.STARTED;
        currentStep = queue.poll();
        currentStep.getStep().start();
    }

    public List<ProcessRecruiter> getProcessRecruiters() {
        return processRecruiters;
    }

    public PreparedStep getCurrentStep() {
        return currentStep;
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public void nextStep() {
        if (processState == ProcessState.COMPLETED_PASS || processState == ProcessState.COMPLETED_FAIL) {
            throw new IllegalStateException("Process already completed.");
        }


        if (!currentStep.getStep().isCompleted()) {
            throw new IllegalStateException("Current step is not completed yet.");
        }

        if (!currentStep.getStep().isPass()) {
            processState = ProcessState.COMPLETED_FAIL;
            throw new RuntimeException("Step is fail");
        }

        if (queue.isEmpty()) {
            processState = ProcessState.COMPLETED_PASS;
            return;
        }

        currentStep = queue.poll();
        currentStep.getStep().start();
    }

    public void cancelProcess() {
        processState = ProcessState.CANCELLED;
    }


}

