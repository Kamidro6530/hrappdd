package com.example.hrappdd;

import com.example.hrappdd.interview.domain.InvitationState;

import java.util.UUID;

public abstract class Participant {
    private final UUID id;
    private final String email;
    private InvitationState invitationState;
    private boolean willingToContinue;

    public Participant(UUID id, String email) {
        this.id = id;
        this.email = email;
        this.invitationState = InvitationState.INITIAL;
    }

    public UUID getId() {
        return id;
    }


    public boolean isInvitationAccepted() {
        return invitationState == InvitationState.ACCEPTED;
    }

    public void acceptInvitation() {
        if (invitationState != InvitationState.PENDING) throw new IllegalStateException();

        invitationState = InvitationState.ACCEPTED;
    }

    public void sendInvitation() {
        if (invitationState != InvitationState.INITIAL) throw new IllegalStateException();

        invitationState = InvitationState.PENDING;
    }
}
