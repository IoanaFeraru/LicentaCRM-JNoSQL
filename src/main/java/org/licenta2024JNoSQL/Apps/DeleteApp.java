package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentDeleteQuery;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
import java.util.List;

public class DeleteApp {

    @Inject
    private DocumentCollectionManager manager;

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            DeleteApp app = container.select(DeleteApp.class).get();
            app.deleteAllDocuments(List.of("Client", "Produs", "Achizitie", "Oferta", "Campanie"));
        }
        System.exit(0);
    }

    public void deleteAllDocuments(List<String> collections) {
        for (String collection : collections) {
            DocumentDeleteQuery deleteQuery = DocumentDeleteQuery.builder()
                    .from(collection)
                    .build();
            manager.delete(deleteQuery);

            System.out.println("Deleted all documents from collection: " + collection);
        }
    }
}
