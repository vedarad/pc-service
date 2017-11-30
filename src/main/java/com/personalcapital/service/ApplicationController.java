package com.personalcapital.service;

import com.google.gson.Gson;
import com.personalcapital.service.client.ElasticsearchDataProcessor;
import com.personalcapital.service.model.PlanModel;
import com.personalcapital.service.utils.ConfigLoader;
import io.searchbox.client.JestResult;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller to accept API Calls and to route to specific API Gateway
 */

@RestController
public class ApplicationController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ApplicationController.class);
    private ConfigLoader configLoader = new ConfigLoader();

    /**
     * Search based on plan name in AWS ES
     * @param planName Takes Request Body
     * @return model
     */
    @RequestMapping(value = "/planname", method = RequestMethod.GET)
    @ResponseBody
    public PlanModel planName(@RequestParam("planName") String planName)  {
        if(planName == null || planName.isEmpty()){
            throw new IllegalArgumentException("'Plan Name' parameter must not be null or empty");
        }

        JestResult result = ElasticsearchDataProcessor.getData(configLoader.getFields().get("planFieldName"),
                planName, configLoader.getElasticConfig().get("searchIndexAlias"), configLoader.getElasticConfig().get("searchType"));

        if(result == null){
            throw new IllegalArgumentException("No results found for this Plan " + planName);
        }

        PlanModel model = new Gson().fromJson(result.getSourceAsString(), PlanModel.class);

        return model;
    }

    /**
     * Search based on sponsor name in AWS ES
     * @param sponsorName Takes Request Body
     * @return model
     */
    @RequestMapping(value = "/sponsorname", method = RequestMethod.GET)
    @ResponseBody
    public PlanModel sponsorName(@RequestParam("sponsorName") String sponsorName)  {
        if(sponsorName == null || sponsorName.isEmpty()){
            throw new IllegalArgumentException("'Sponsor Name' parameter must not be null or empty");
        }

        JestResult result = ElasticsearchDataProcessor.getData(configLoader.getFields().get("sponsorFieldName"),
                sponsorName, configLoader.getElasticConfig().get("searchIndexAlias"), configLoader.getElasticConfig().get("searchType"));

        if(result == null){
            throw new IllegalArgumentException("No results found for this sponsor " + sponsorName);
        }

        PlanModel model = new Gson().fromJson(result.getSourceAsString(), PlanModel.class);

        return model;
    }

    /**
     * Search based on sponsor state in AWS ES
     * @param sponsorState Takes Request Body
     * @return model
     */
    @RequestMapping(value = "/sponsorstate", method = RequestMethod.GET)
    @ResponseBody
    public PlanModel sponsorState(@RequestParam("sponsorState") String sponsorState)  {
        if(sponsorState == null || sponsorState.isEmpty()){
            throw new IllegalArgumentException("'Sponsor State' parameter must not be null or empty");
        }

        JestResult result = ElasticsearchDataProcessor.getData(configLoader.getFields().get("usState"),
                sponsorState, configLoader.getElasticConfig().get("searchIndexAlias"), configLoader.getElasticConfig().get("searchType"));

        if(result == null){
            throw new IllegalArgumentException("No results found for this sponsor state" + sponsorState);
        }

        PlanModel model = new Gson().fromJson(result.getSourceAsString(), PlanModel.class);

        return model;
    }

}
