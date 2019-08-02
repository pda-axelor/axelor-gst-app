package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

	public InvoiceLine calculateCgstSgst(InvoiceLine invoiceLine);

	public InvoiceLine calculateIgst(InvoiceLine invoiceLine);
	
	public InvoiceLine calculateAll(Invoice invoice,InvoiceLine invoiceLine);
	
	public List<InvoiceLine> reCalculateInvoiceLine(Invoice invoice);

}
