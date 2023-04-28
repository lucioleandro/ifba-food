package br.com.ifbafood.pagamentos.controller;

import br.com.ifbafood.pagamentos.exceptions.MessagingException;
import br.com.ifbafood.pagamentos.model.Pagamento;
import br.com.ifbafood.pagamentos.service.PagamentoService;
import br.com.ifbafood.pagamentos.dto.PagamentoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService service;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PagamentoController(PagamentoService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Creates a new Pagamento and returns its details.
     *
     * @param dto The PagamentoDTO object containing the payment details.
     * @return ResponseEntity containing the created PagamentoDTO object.
     */
    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastrar(@RequestBody @Valid PagamentoDTO dto, UriComponentsBuilder uriBuilder) {
        PagamentoDTO pagamento = service.create(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    /**
     * Updates an existing Pagamento and returns its details.
     *
     * @param id  The ID of the Pagamento to update.
     * @param dto The PagamentoDTO object containing the updated payment details.
     * @return ResponseEntity containing the updated PagamentoDTO object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDTO dto) {
        PagamentoDTO atualizado = service.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Retrieves a list of Pagamentos in a pageable format.
     *
     * @param paginacao The Pageable object containing pagination information.
     * @return A Page of PagamentoDTO objects.
     */
    @GetMapping
    public Page<PagamentoDTO> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return service.findAll(paginacao);
    }

    /**
     * Finds a Pagamento by its ID and returns its details.
     *
     * @param id The ID of the Pagamento to find.
     * @return ResponseEntity containing the found PagamentoDTO object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> detalhar(@PathVariable @NotNull Long id) {
        PagamentoDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    /**
     * Deletes a Pagamento by its ID.
     *
     * @param id The ID of the Pagamento to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable @NotNull Long id) {
        service.deleteById(id);
    }

    /**
     * Confirms a payment by updating its status and sending a message through RabbitMQ.
     *
     * @param id The payment ID to be confirmed.
     */
    @PatchMapping("/{id}/confirmar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id) {
        Pagamento pagamento = service.confirmaPagamento(id);
        rabbitTemplate.convertAndSend("pagamentos.ex", "", pagamento);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
