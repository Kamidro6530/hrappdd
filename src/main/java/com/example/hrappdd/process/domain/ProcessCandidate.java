package com.example.hrappdd.process.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class ProcessCandidate {
    private UUID id;
    private String email;

}
