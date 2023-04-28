package br.com.ifbafood.pagamentos.exceptions;

import java.io.IOException;

public class MessagingException extends RuntimeException {
    public MessagingException(String message, IOException e) {
        super(message, e);
    }
}
