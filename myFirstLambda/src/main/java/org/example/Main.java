package org.example;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;


public class Main implements RequestHandler<Map<String, String>, String> {

    private AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.AP_SOUTH_1)
            .build();
    private DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private String errorMsg = "Exception occurred while saving data into db";
    private String successMsg = "Data saved successfully to DynamoDB";


    public String handleRequest(Map<String, String> request, Context context) {
        try {
            String tableName = "FirstTable";
            Table table = dynamoDB.getTable(tableName);
            Item item = new Item();
            for (Map.Entry<String, String> entry : request.entrySet()) {
                item.withString(entry.getKey(), entry.getValue());
            }
            table.putItem(item);
            context.getLogger().log("Data saved successfully to DynamoDB");
        } catch (AmazonDynamoDBException e) {
            context.getLogger().log(errorMsg + e);
            return errorMsg;
        }
        return successMsg;
    }
}
