package com.ioet.bpm.people.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
@DynamoDBTable(tableName = "people_person")
public class Person extends AuditLog {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @NotBlank
    @DynamoDBAttribute
    private String name;

    @NotBlank
    @DynamoDBAttribute
    private String authenticationIdentity;

    @DynamoDBAttribute
    private String password;

}
