package com.example.hrappdd.task.domain;


import com.example.hrappdd.Participant;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

public class Candidate extends Participant {
    private  UUID id;
    private  String email;

    public Candidate(UUID id, String email) {
        super(id, email);
    }
}
