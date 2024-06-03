package org.licenta2024JNoSQL;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

//ToDo Apps For multiple insertions;
public class MongoDBExample {
    public static void main(String[] args) {
        /*
        String uri = "mongodb://localhost:27017/";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("Licenta2024JNoSQL");

            MongoCollection<Document> collection = database.getCollection("test");
            Document document = new Document("test", "test 1");
            collection.insertOne(document);

            System.out.println("Document inserted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}
