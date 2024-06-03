package org.licenta2024JNoSQL.Utilities;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FeedbackUtil {

    private final ClientRepository clientRepository;
    private final ProdusRepository produsRepository;
    private final DocumentTemplate template;

    public FeedbackUtil(ClientRepository clientRepository, ProdusRepository produsRepository, DocumentTemplate template) {
        this.clientRepository = clientRepository;
        this.produsRepository = produsRepository;
        this.template = template;
    }

    public void updateProductRating(String codProdus) {
        List<Client> clients = clientRepository.findAll();

        List<Feedback> feedbackList = clients.stream()
                .filter(client -> client.getFeedback() != null)
                .flatMap(client -> client.getFeedback().stream())
                .filter(feedback -> feedback.getCodProdus().equals(codProdus))
                .collect(Collectors.toList());

        double averageRating = feedbackList.stream()
                .collect(Collectors.averagingInt(Feedback::getRating));

        Optional<Produs> optionalProdus = produsRepository.findById(codProdus);

        if (optionalProdus.isPresent()) {
            Produs produs = optionalProdus.get();
            produs.setRating(averageRating);
            template.update(produs);
        }
    }

}
