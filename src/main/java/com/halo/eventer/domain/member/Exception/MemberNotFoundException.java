package com.halo.eventer.domain.member.Exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {
    public MemberNotFoundException() {
        super("Member not found");
    }
}
