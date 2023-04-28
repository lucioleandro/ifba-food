package br.com.ifbafood.pagamentos.service;

import br.com.ifbafood.pagamentos.dto.PagamentoDTO;
import br.com.ifbafood.pagamentos.exceptions.MessagingException;
import br.com.ifbafood.pagamentos.model.Pagamento;
import br.com.ifbafood.pagamentos.model.Status;
import br.com.ifbafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class PagamentoService {

    private final PagamentoRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    public PagamentoService(PagamentoRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new Pagamento and saves it to the repository.
     *
     * @param dto The PagamentoDTO object containing the payment details.
     * @return The created PagamentoDTO object.
     */
    public PagamentoDTO create(PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    /**
     * Updates an existing Pagamento and saves it to the repository.
     *
     * @param id  The ID of the Pagamento to update.
     * @param dto The PagamentoDTO object containing the updated payment details.
     * @return The updated PagamentoDTO object.
     */
    public PagamentoDTO update(Long id, PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    /**
     * Retrieves all Pagamentos in a pageable format.
     *
     * @param pageable The Pageable object containing pagination information.
     * @return A Page of PagamentoDTO objects.
     */
    public Page<PagamentoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    /**
     * Finds a Pagamento by its ID.
     *
     * @param id The ID of the Pagamento to find.
     * @return The found PagamentoDTO object.
     * @throws EntityNotFoundException If the payment is not found.
     */
    public PagamentoDTO findById(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + id));

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    /**
     * Deletes a Pagamento by its ID.
     *
     * @param id The ID of the Pagamento to delete.
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Confirms a payment by updating its status and sending a message through RabbitMQ.
     *
     * @param id The payment ID to be confirmed.
     * @throws EntityNotFoundException If the payment is not found.
     * @throws MessagingException      If there is an error sending the RabbitMQ message.
     */
    public Pagamento confirmaPagamento(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + id));

        pagamento.setStatus(Status.CONFIRMADO);
        return repository.save(pagamento);
    }

}

