package com.policlinica.pagos.domain.port.out;

public interface EmailPort {
    void enviarEmailConBoleta(String destinatario, String nombre, byte[] pdf, String idTransaccion, String detalleCita);
}