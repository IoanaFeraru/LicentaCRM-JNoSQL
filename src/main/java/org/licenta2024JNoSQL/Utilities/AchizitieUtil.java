package org.licenta2024JNoSQL.Utilities;

import org.licenta2024JNoSQL.Entities.Achizitie.*;
import org.licenta2024JNoSQL.Entities.Client.*;
import org.licenta2024JNoSQL.Entities.Client.Embeded.IstoricPuncte;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.TipReducere;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;
import org.licenta2024JNoSQL.Entities.Oferta.Embeded.ProdusCost;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.OfertaRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import jakarta.nosql.mapping.document.DocumentTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class AchizitieUtil {

    public static void calculateValoareAchizitie(Achizitie achizitie, ProdusRepository produsRepository, OfertaRepository ofertaRepository) {
        double totalValue = 0.0;

        Optional<Oferta> ofertaOpt = Optional.empty();
        if (achizitie.getCodOferta() != null && !achizitie.getCodOferta().isEmpty()) {
            ofertaOpt = ofertaRepository.findByCodOferta(achizitie.getCodOferta()).stream().findFirst();
        }

        for (LinieAchizitie linie : achizitie.getLinieAchizitie()) {
            Optional<Produs> produsOpt = produsRepository.findByCodProdus(linie.getCodProdus()).stream().findFirst();
            if (produsOpt.isPresent()) {
                Produs produs = produsOpt.get();
                double lineValue = produs.getPret() * linie.getCantitate();

                if (ofertaOpt.isPresent()) {
                    Oferta oferta = ofertaOpt.get();
                    if (oferta.getTipReducere() == TipReducere.PRODUS) {
                        for (ProdusCost pc : oferta.getProduseCost()) {
                            if (pc.getProdus().getCodProdus().equals(produs.getCodProdus())) {
                                lineValue = 0;
                                break;
                            }
                        }
                    }
                }

                linie.setPrice(lineValue);
                totalValue += lineValue;
            }
        }

        if (ofertaOpt.isPresent()) {
            Oferta oferta = ofertaOpt.get();
            if (oferta.getTipReducere() == TipReducere.VOUCHER) {
                totalValue -= oferta.getValoareReducere();
            } else if (oferta.getTipReducere() == TipReducere.PROCENT) {
                totalValue -= (totalValue * oferta.getValoareReducere() / 100);
            }
        }

        achizitie.setValoareAchizitie(String.valueOf(totalValue));
    }

    public static void calculateValoarePuncte(Achizitie achizitie, OfertaRepository ofertaRepository) {
        double valoareAchizitie = Double.parseDouble(achizitie.getValoareAchizitie());
        int valoarePuncte = (int) Math.floor(valoareAchizitie);

        if (achizitie.getCodOferta() != null && !achizitie.getCodOferta().isEmpty()) {
            Optional<Oferta> ofertaOpt = ofertaRepository.findByCodOferta(achizitie.getCodOferta()).stream().findFirst();
            if (ofertaOpt.isPresent()) {
                Oferta oferta = ofertaOpt.get();
                if (oferta.getTipReducere() == TipReducere.PRODUS) {
                    for (ProdusCost pc : oferta.getProduseCost()) {
                        valoarePuncte -= pc.getCostPuncte();
                    }
                }
            }
        }

        achizitie.setValoarePuncte(String.valueOf(valoarePuncte));
    }

    public static void updatePuncteClient(Achizitie achizitie, ClientRepository clientRepository, DocumentTemplate template) {
        String codClient = achizitie.getCodClient();
        Optional<Client> optionalClient = clientRepository.findByCodClient(codClient).stream().findFirst();

        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            int newPuncteLoialitate = client.getPuncteLoialitate() + Integer.parseInt(achizitie.getValoarePuncte());
            client.setPuncteLoialitate(newPuncteLoialitate);

            IstoricPuncte istoricPuncte = new IstoricPuncte();
            istoricPuncte.setCodAchizitie(achizitie.getCodAchizitie());
            istoricPuncte.setValoarePuncte(Integer.parseInt(achizitie.getValoarePuncte()));
            istoricPuncte.setDataProcesare(new Date());

            if (client.getIstoricPuncte() == null) {
                client.setIstoricPuncte(new ArrayList<>());
            }
            client.getIstoricPuncte().add(istoricPuncte);

            template.update(client);
        } else {
            System.out.println("Client not found with ID: " + codClient);
        }
    }
}
