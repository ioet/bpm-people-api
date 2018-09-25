# bpm-people-api

## Swagger

Swagger is integrated and available in this URL:

```
http://IP_ADDRESS:PORT/swagger-ui.html
```

## Docker

The project has integrated a docker plugin so you can generate a docker image using the following Gradle task:

```
$ ./gradlew build docker
```

Don't forget to pass the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` to make it work locally.
For any other environment the credentials should be provided by the CI server.



## Dynamo

In order to make the API works and establish a connection with Dynamo (Cloud DB provided by AWS) you'll need to export the following environment variables:

```
export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"
export AWS_SECRET_ACCESS_KEY="YOUR_SECRET_KEY"
export AWS_REGION="us-east-1"
```

If you don't have AWS Credentials, feel free to ask Juan Garcia, Roland or Rene Enriquez for it. Your account needs to be part of the group bpm-people to have enough permission to access Dynamo tables which are part of this project. 

## Playing with the API
So far you can create, query and delete people using the API. 

Query people

```
curl -X GET http://localhost:8080/people
```


Create a new person

```
curl -X POST http://localhost:8080/people -H 'Content-Type: application/json' -d '{ "name":"Your Name", "authenticationIdentity": "youremail@domain.com"}'
```

Delete an existing person

```
curl -X DELETE http://localhost:8080/people/{PERSON_ID}
```

