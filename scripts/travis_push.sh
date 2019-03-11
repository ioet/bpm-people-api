echo "Travis push job"

# Download dependencies and build
npm install
npm run build

# Update the stack
case ${TRAVIS_BRANCH} in
    master)
        pulumi stack select staging
        pulumi update --yes
        ;;
    #production)
    #    pulumi stack select bpm-people-api-ops/website-production
    #    pulumi update --yes
    #    ;;
    *)
        echo "No Pulumi stack associated with branch ${TRAVIS_BRANCH}."
        ;;
esac