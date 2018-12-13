#! /bin/bash

# set reasonable defaults that can be overridden
SONAR_HOST_URL="${SONAR_HOST_URL:-https://sonarcloud.io}"
GITHUB_API_URL="${GITHUB_API_URL:-https://api.github.com}"
PROJECT_KEY="${PROJECT_KEY:-ioet_bpm-people-api}"
ORGANIZATION="${ORGANIZATION:-ioet}"

if [[ -z "${SONAR_GITHUB_TOKEN}" ]]; then
  echo 'SONAR_GITHUB_TOKEN should be defined.'
  echo 'Visit: https://sonarcloud.io/account/security to generate your own token.'
else
  ./gradlew sonarqube -Dsonar.projectKey=$PROJECT_KEY -Dsonar.organization=$ORGANIZATION -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_GITHUB_TOKEN
  echo ""
  echo "See: https://sonarcloud.io/dashboard?id=ioet_bpm-people-api"
fi
