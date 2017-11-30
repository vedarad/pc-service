package com.personalcapital.service.client;

import com.google.gson.Gson;
import com.personalcapital.service.model.PlanModel;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.params.Parameters;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchDataProcessor {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchDataProcessor.class);

    /**
     * Returns Search Result from Elasticsearch
     * @param searchFieldName searchFieldName is name of the field we are searching
     * @param searchField searchField can be Plan_Name/Sponsor_Name/Sponsor_State
     * @param index Name of the index to get data from
     * @return Returns JestResult
     */
    public static JestResult getData(String searchFieldName, String searchField, String index, String type) {

        JestResult result = null;
        try {
            log.info("Pull data from elastic search Index: {}", index);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            qb.must(QueryBuilders.matchPhraseQuery(searchFieldName + ".keyword", searchField));
            searchSourceBuilder.query(qb).size(10000);
            log.info("Search source builder query: {}", searchSourceBuilder.toString());
            Search search = new Search.Builder(searchSourceBuilder.toString())
                    .addIndex(index)
                    .addType(type)
                    .setParameter(Parameters.SCROLL, "1m").build();
            result = ElasticSearchClient.getInstance().getJestClient().execute(search);
        } catch (Exception e) {
            log.info("Exception: {}", e.getStackTrace());
        }
        if ( result == null || !result.isSucceeded()) {
            return null;
        }
        return result;
    }

    /**
     * Upload data to Elasticseach using JestClient
     * @param csvDataList csv data list to be uploaded to ES
     * @param index Index alias where data will be uploaded
     * @param type Index type
     */
    public static void uploadData(List<Map<String, String>> csvDataList, String index, String type) {

        try {

            IndicesExists indicesExists = new IndicesExists.Builder(index).build();
            boolean indexExists = ElasticSearchClient.getInstance().getJestClient().execute(indicesExists).isSucceeded();

            if (!indexExists) {
                CreateIndex createIndex = new CreateIndex.Builder(index)
                        .build();
                ElasticSearchClient.getInstance().getJestClient().execute(createIndex);
            }
            List<Index> indexList = new ArrayList<>();
            for (Map<String, String> data : csvDataList) {
                Map<String, String> builder = new HashMap<>();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    builder.put(entry.getKey(), entry.getValue());
                }
                indexList.add(new Index.Builder(new Gson().toJson(builder)).build());
            }
            Bulk bulk = new Bulk.Builder()
                    .defaultIndex(index)
                    .defaultType(type).addAction(indexList).build();

            BulkResult result = ElasticSearchClient.getInstance().getJestClient().execute(bulk);

            if (!result.isSucceeded()) {
                log.error("{} Errors, while bulk uploading data to index [{}] with error message {}", result.getItems().size(), index, result.getErrorMessage());
            } else {
                log.info("[{}] Records loaded into index {}", result.getItems().size(), index);
            }
        }catch(Exception e){
            log.info("Exception while uploading {}", e.getStackTrace());
        }
    }
}