package com.example.hrappdd.process.domain.step;

import com.example.hrappdd.Participant;

import java.util.List;
import java.util.UUID;

public interface Step {
    boolean isCompleted();
    boolean isPass();
    void process();
    void start();
    void finish(UUID recruiterId, boolean pass);
    List<Participant> getParticipants();
    void acceptParticipant(UUID participantId);
}
