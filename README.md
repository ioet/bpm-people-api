# bpm-people-api

## Build Status and Code coverage

[![Build Status](https://travis-ci.org/ioet/bpm-people-api.svg?branch=master)](https://travis-ci.org/ioet/bpm-people-api)
[![Code Coverage](https://codecov.io/gh/ioet/bpm-people-api/branch/master/graph/badge.svg)](https://codecov.io/gh/ioet/bpm-people-api)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=ioet_bpm-people-api&metric=alert_status)](https://sonarcloud.io/dashboard?id=ioet_bpm-people-api)

## Run it locally

Make sure you have an AWS account and you have exported your credentials in the environment:

```
export AWS_ACCESS_KEY_ID="YOUR_ACCESS_KEY"
export AWS_SECRET_ACCESS_KEY="YOUR_SECRET_KEY"
export AWS_REGION="us-east-1"
```

You should write them into the bashrc-file as well, so that they are loaded each time you open a terminal.
```
vim ~/.bashrc
```
Then you will be able to pull the docker images using the following command:

```
docker-compose up
```
Then start the people-api using your IDE or the following command. This should also work if `docker-compose up` failed.
```
./gradlew bootRun
```

If you have run `docker-compose up`, you will be able to see the people-api service registered in eureka here:
```
http://localhost:8761/
```

And you can directly access the people-api and it's swagger here: 
```
http://localhost:8081/people
http://localhost:8081/swagger-ui.html
```
For accessing both via the edge server use:
```
http://localhost:9081/people-service/people
http://localhost:9081/people-service/swagger-ui.html
```

## Postman
There is a Postman Collection included to test the api.

To run it from the command line install Newman in your machine.

```
npm install -g newman
```
  
You can use Newman to run it with this command:
```
newman run postman/collection.json -e postman/env.json
```

## Docker

The project has integrated a docker plugin so you can generate a docker image using the following Gradle task:

```
./gradlew build docker
```

Don't forget to pass the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` to make it work locally.
For any other environment the credentials should be provided by the CI server.



## Dynamo

In order to make the API work and establish a connection with Dynamo (Cloud DB provided by AWS) you'll need to export the following environment variables:

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
curl -X GET http://localhost:8081/people
```


Create a new person

```
curl -X POST http://localhost:8081/people -H 'Content-Type: application/json' -d '{ "name":"Your Name", "authenticationIdentity": "youremail@domain.com"}'
```
## Configuring IntelliJ IDE
If you want to run the application from IntelliJ you must configure the required environment variables following the next steps:

Setting the environment variables

```
1. Go to menu Run and choose Edit Configurations.
2. It will show a configurations window. Go to the tab Configuration.
3. Add the required environment variables AWS_SECRET_ACCESS_KEY, AWS_ACCESS_KEY_ID, and AWS_REGION with the correspondent values.
```

Run or Debug

```
Now, you can run or debug the app from the IntelliJ terminal. You can use JRbel to debug and redeploy the app.
```

## Examining Code Quality locally precommit

Included in this repository is a tool (shell script) for examining the code as is in your current branch without requiring a commit first. To use this tool, you will need to create a GitHub token with which SonarCloud can integrate with GitHub on your behalf. To create said token, navegate to https://sonarcloud.io/account/security (log in as necessary with your GitHub account that is tied to this repository), enter a token name and click on the Generate button. A string will be displayed, which you should copy and store as your token (this string will never be displayed again, so be sure to save it in a safe place). Now you are ready to execute (in a Bash compatible shell):

```bash
SONAR_GITHUB_TOKEN=<my personal token created above> ./sonarcloud_analyze.sh
```

