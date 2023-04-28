package br.com.ifbafood.pedidos.amqp;

import br.com.ifbafood.pedidos.dto.PagamentoDTO;
import br.com.ifbafood.pedidos.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {

    @Autowired
    private PedidoService pedidoService;

    @RabbitListener(queues = "pagamentos.detalhes-pedido")
    public void recebeMensagem(@Payload PagamentoDTO pagamento) {
        String mensagem = """
                Dados do pagamento: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s
                """.formatted(pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValor(),
                pagamento.getStatus());

        System.out.println("Recebi a mensagem " + mensagem); // Use a log tool instead

        pedidoService.aprovaPagamentoPedido(pagamento.getPedidoId());
    }
}
