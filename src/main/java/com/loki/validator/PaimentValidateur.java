package com.loki.validator;

import com.loki.service.dto.PaiementDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PaimentValidateur {
    public static List<String> validate(PaiementDTO paiementDTO) {
        List<String> errors = new ArrayList<>();

        if (paiementDTO == null) {
            errors.add("Veuillez renseigner l'adresse 1'");
            errors.add("Veuillez renseigner la ville'");
            errors.add("Veuillez renseigner le pays'");
            errors.add("Veuillez renseigner le code postal'");
            return errors;
        }
        if (!StringUtils.hasLength(paiementDTO.getFournisseur().getAddress())) {
            errors.add("Veuillez renseigner l'adresse du '");
        }
        if (!StringUtils.hasLength(paiementDTO.getCommand().getCommandNumber())) {
            errors.add("Veuillez renseigner la ville'");
        }
        if (!StringUtils.hasLength(paiementDTO.getTotal().toString())) {
            errors.add("Veuillez renseigner le pays'");
        }
        if (!StringUtils.hasLength(paiementDTO.getStatus().toString())){

            errors.add("Veuillez renseigner le code postal'");
        }
        return errors;
    }
}
