"use strict";
const cloud = require("@pulumi/cloud");
const pulumi = require("@pulumi/pulumi");

const config = new pulumi.Config("bpm-people-api-ops");
let service = new cloud.Service("PeopleService", {
    containers: {
        Api: {
            image: "524819651720.dkr.ecr.us-east-1.amazonaws.com/bpm-people-api:latest",
            memory: 512,
            ports: [{ port: 8081 }],
            environment: {
                "AWS_ACCESS_KEY_ID":   config.require("AWS_ACCESS_KEY_ID"),
                "AWS_SECRET_ACCESS_KEY": config.require("AWS_SECRET_ACCESS_KEY"),
            },
        },
    },
    replicas: 1,
});

// export just the hostname property of the container frontend
exports.url = service.defaultEndpoint.apply(e => `http://${e.hostname}`);