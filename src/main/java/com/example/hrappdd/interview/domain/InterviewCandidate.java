package com.example.hrappdd.interview.domain;

import com.example.hrappdd.Participant;

import java.util.UUID;

public class InterviewCandidate extends Participant {

    public InterviewCandidate(UUID id, String email) {
        super(id, email);
    }
}
