package com.loki.service;

import com.loki.domain.Command;
import com.loki.repository.CommandRepository;
import com.loki.service.dto.CommandDTO;
import com.loki.service.mapper.CommandMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Command}.
 */
@Service
@Transactional
public class CommandService {

    private final Logger log = LoggerFactory.getLogger(CommandService.class);

    private final CommandRepository commandRepository;

    private final CommandMapper commandMapper;

    public CommandService(CommandRepository commandRepository, CommandMapper commandMapper) {
        this.commandRepository = commandRepository;
        this.commandMapper = commandMapper;
    }

    /**
     * Save a command.
     *
     * @param commandDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandDTO save(CommandDTO commandDTO) {
        log.debug("Request to save Command : {}", commandDTO);
        Command command = commandMapper.toEntity(commandDTO);
        command = commandRepository.save(command);
        return commandMapper.toDto(command);
    }

    /**
     * Update a command.
     *
     * @param commandDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandDTO update(CommandDTO commandDTO) {
        log.debug("Request to update Command : {}", commandDTO);
        Command command = commandMapper.toEntity(commandDTO);
        command = commandRepository.save(command);
        return commandMapper.toDto(command);
    }

    /**
     * Partially update a command.
     *
     * @param commandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandDTO> partialUpdate(CommandDTO commandDTO) {
        log.debug("Request to partially update Command : {}", commandDTO);

        return commandRepository
            .findById(commandDTO.getId())
            .map(existingCommand -> {
                commandMapper.partialUpdate(existingCommand, commandDTO);

                return existingCommand;
            })
            .map(commandRepository::save)
            .map(commandMapper::toDto);
    }

    /**
     * Get all the commands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commands");
        return commandRepository.findAll(pageable).map(commandMapper::toDto);
    }

    /**
     *  Get all the commands where Paiement is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommandDTO> findAllWherePaiementIsNull() {
        log.debug("Request to get all commands where Paiement is null");
        return StreamSupport
            .stream(commandRepository.findAll().spliterator(), false)
            .filter(command -> command.getPaiement() == null)
            .map(commandMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one command by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandDTO> findOne(Long id) {
        log.debug("Request to get Command : {}", id);
        return commandRepository.findById(id).map(commandMapper::toDto);
    }

    /**
     * Delete the command by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Command : {}", id);
        commandRepository.deleteById(id);
    }
}
