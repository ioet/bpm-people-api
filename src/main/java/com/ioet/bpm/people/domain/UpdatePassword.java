package com.ioet.bpm.people.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class )

public class UpdatePassword {

    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirmation;

}

