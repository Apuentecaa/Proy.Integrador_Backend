package com.policlinica.pagos.domain.port.out;

import com.policlinica.pagos.domain.model.Pago;

public interface PdfGeneratorPort {
    byte[] generarBoleta(Pago pago, String detalleCita, String nombrePaciente, String dniPaciente);
    String guardarPDF(byte[] pdf, String idTransaccion);
}