package com.ioet.bpm.people.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdatePassword {

    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;

}

