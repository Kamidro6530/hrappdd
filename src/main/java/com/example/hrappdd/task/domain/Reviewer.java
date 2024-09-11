package com.example.hrappdd.task.domain;

import com.example.hrappdd.Participant;

import java.util.UUID;


public class Reviewer extends Participant {
    private UUID id;
    private String email;

    public Reviewer(UUID id, String email) {
        super(id, email);
    }
}
