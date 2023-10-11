package com.loki.validator;

import com.loki.service.dto.PaiementDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PaimentValidateur {
    public static List<String> validate(PaiementDTO paiementDTO) {
        List<String> errors = new ArrayList<>();

        if (paiementDTO == null) {
            //errors.add("Veuillez renseigner le paiement");
            errors.add("Veuillez renseigner l'identifiant du fournisseur");
            errors.add("Veuillez renseigner l'identifiant de la commande");
            errors.add("Veuillez renseigner le total du paiement");
            return errors;
        }
        if (!StringUtils.hasLength(paiementDTO.getFournisseur().getId().toString())) {
            errors.add("Veuillez renseigner l'identifiant du fournisseur");
        }
        if (!StringUtils.hasLength(paiementDTO.getCommand().getId().toString())) {
            errors.add("Veuillez renseigner l'identifiant de la commande");
        }
        if (!StringUtils.hasLength(paiementDTO.getTotal().toString())) {
            errors.add("Veuillez renseigner le total du paiement");
        }

        return errors;
    }
}
