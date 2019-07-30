package com.axelor.gst.services;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

	public InvoiceLine calculateCgstSgst(InvoiceLine invoiceLine);

	public InvoiceLine calculateIgst(InvoiceLine invoiceLine);

}
