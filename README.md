# pc-service

Run the Spring boot application..

/planname is to search using plan name

/sponsorname is to search using sponsor name

/sponsorstate is to search using sponsor state

Uses JestClinet to connect to AWS ES.

Set AWS R53 Endpoint in properties file.

elasticSearchEndpoint=https://xxxxx-xx.aws.r53.xx.com/
searchIndexAlias=whatIsTheIndex
searchType=whatIsTheIndexType

ElasticSearchDataProcessor returns a Model with all fields returned from ES.
