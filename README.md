# pc-service

Service is running at: https://obscure-springs-85349.herokuapp.com/

Deployed using heroku:
heroku create
git push heroku master

Available paths:
/planname is to search using plan name
/sponsorname is to search using sponsor name
/sponsorstate is to search using sponsor state

Uses JestClinet to connect to ES.

elasticSearchEndpoint=https://xxxxx-xx.aws.r53.xx.com/
searchIndexAlias=whatIsTheIndex
searchType=whatIsTheIndexType

ElasticSearchDataProcessor returns a Model with all fields returned from ES.

Uploaded Test data using readCSVDataAndUploadToES method under Utilities.
Reads csv file from given path and uploads to ES. 
Bulk Api to upload 10k rows. Continues until all rows are done.
For me it took 4minutes to upload the given test data.

Does it support million+ sets to upload?
Yes It does which might take more time as I am not using big cluster with IO intensive and High Network Performance.

Used Postman to test

Get request: https://obscure-springs-85349.herokuapp.com/planname?planName=FS%20LEHIGH%20VALLEY,%20INC.%20PROFIT%20SHARING%20PLAN
with header: Accept: application/vnd.heroku+json; version=3

Response:
{
    "plan_NAME": "FS LEHIGH VALLEY, INC. PROFIT SHARING PLAN",
    "ack_ID": "20171002112920P040196921377001",
    "spons_DFE_MAIL_US_STATE": "PA",
    "sponsor_DFE_NAME": "FS LEHIGH VALLEY, INC."
}
