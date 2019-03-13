echo "Travis pull_request job"

# Download dependencies and build
npm install
npm run build
pulumi stack select staging
pulumi update --yes

# Preview changes that would be made if the PR were merged.
# case ${TRAVIS_BRANCH} in
#    master)
#        pulumi stack select staging
#        pulumi update --yes
#        ;;
    #production)
    #    pulumi stack select acme/website-production
    #    pulumi preview
    #    ;;
#    *)
#        echo "No Pulumi stack targeted by pull request branch ${TRAVIS_BRANCH}."
#        ;;
# esac