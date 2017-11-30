package com.personalcapital.service.utils;

import com.personalcapital.service.client.ElasticsearchDataProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utilities {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchDataProcessor.class);

    /**
     * Returns a List<Map<String,String>> data read from CSV file
     * @return List<Map<String,String>> of csv data
     */
    public List<Map<String, String>> readCSVDataAndUploadToES(){

        List<Map<String, String>> csvDataList = new ArrayList<>();

        FileReader fileReader = null;
        CSVParser csvFileParser = null;
        try{
            fileReader = new FileReader("test.csv");
            csvFileParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader());
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            Map<String, Integer> headerMap = csvFileParser.getHeaderMap();
            for(CSVRecord record : csvRecords){
                Map<String, String> csvData = new LinkedHashMap<>();
                for(Map.Entry<String, Integer> header : headerMap.entrySet()){
                    csvData.put(header.getKey(), record.get(header.getValue()));
                }
                csvDataList.add(csvData);
                if(csvDataList.size() == 10000){
                    ElasticsearchDataProcessor.uploadData(csvDataList, "pcindex", "pctype");
                    csvDataList = new ArrayList<>();
                }
            }
            fileReader.close();
            csvFileParser.close();
        } catch (FileNotFoundException e) {
            log.error("File not found: "+e.getStackTrace());
            return null;
        } catch (IOException e) {
            log.error("Input/Output exception while reading csv data: "+e.getStackTrace());
            return null;
        }catch (Exception e) {
            log.error("Exception while reading csv data: "+e.getStackTrace());
            return null;
        }
        return csvDataList;
    }

}
