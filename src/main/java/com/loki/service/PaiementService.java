package com.loki.service;

import com.loki.domain.Paiement;
import com.loki.domain.enumeration.ErrorCodes;
import com.loki.domain.enumeration.StatusPaiement;
import com.loki.repository.PaiementRepository;
import com.loki.service.dto.PaiementDTO;
import com.loki.service.mapper.PaiementMapper;

import java.util.List;
import java.util.Optional;

import com.loki.validator.PaimentValidateur;
import com.loki.web.rest.errors.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;

    public PaiementService(PaiementRepository paiementRepository, PaiementMapper paiementMapper) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
    }

    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    /*   public PaiementDTO save(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement = paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }*/
    public PaiementDTO confirm(PaiementDTO paiementDTO) {
       List<String> errors = PaimentValidateur.validate(paiementDTO);
        if(!errors.isEmpty()){
            log.error("Paiement is not valid {}",paiementDTO);
            throw new InvalidEntityException("Veuillez renseigner le paiement", ErrorCodes.PAIEMENT_NOT_VALID, errors);
        }
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement.setStatus(StatusPaiement.ACCEPTE);
        paiement.setActive(true);
        paiement = paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }

    public PaiementDTO cancelPaiement(PaiementDTO paiementDTO) {
        List<String> errors = PaimentValidateur.validate(paiementDTO);
        if(!errors.isEmpty()){
            log.error("Paiement is not valid {}",paiementDTO);
            throw new InvalidEntityException("Veuillez renseigner le paiement", ErrorCodes.PAIEMENT_NOT_FOUND, errors);
        }
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement.setStatus(StatusPaiement.ANNULE);
        paiement.setActive(false);
        paiement = paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }

    public PaiementDTO failedPaiement(PaiementDTO paiementDTO) {
        List<String> errors = PaimentValidateur.validate(paiementDTO);
        if(!errors.isEmpty()){
            log.error("Paiement is not valid {}",paiementDTO);
            throw new InvalidEntityException("Veuillez renseigner le paiement", ErrorCodes.PAIEMENT_NOT_VALID, errors);
        }
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement.setStatus(StatusPaiement.REFUSE);
        paiement.setActive(false);
        paiement = paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }
    /**
     * Update a paiement.
     *
     * @param //paiementDTO the entity to save.
     * @return the persisted entity.
     */
   /* public PaiementDTO update(PaiementDTO paiementDTO) {
        log.debug("Request to update Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement = paiementRepository.save(paiement);
        return paiementMapper.toDto(paiement);
    }*/
    public PaiementDTO update(Long idPaiement) {
        Paiement paiement1 = new Paiement();
        try {
            paiement1 = paiementRepository.findById(idPaiement).get();

            paiement1.setStatus(StatusPaiement.EN_ATTENTE);
            paiementRepository.save(paiement1);

        }catch (Exception e) {
        }
        return paiementMapper.toDto(paiement1);
    }


    /**
     * Partially update a paiement.
     *
     * @param paiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaiementDTO> partialUpdate(PaiementDTO paiementDTO) {
        log.debug("Request to partially update Paiement : {}", paiementDTO);

        return paiementRepository
            .findById(paiementDTO.getId())
            .map(existingPaiement -> {
                paiementMapper.partialUpdate(existingPaiement, paiementDTO);

                return existingPaiement;
            })
            .map(paiementRepository::save)
            .map(paiementMapper::toDto);
    }

    /**
     * Get all the paiements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll(pageable).map(paiementMapper::toDto);
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaiementDTO> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id).map(paiementMapper::toDto);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
    }


}
