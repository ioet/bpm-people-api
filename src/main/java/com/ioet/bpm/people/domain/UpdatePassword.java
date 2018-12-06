package com.ioet.bpm.people.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@DynamoDBTable(tableName = "update_password")

public class UpdatePassword extends AuditLog{
    @DynamoDBHashKey
    @DynamoDBAttribute
    private String oldPassword;

    @DynamoDBAttribute
    private String newPassword;

    @DynamoDBAttribute
    private String newPasswordConfirmation;

}

