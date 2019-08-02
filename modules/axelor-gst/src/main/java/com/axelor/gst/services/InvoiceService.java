package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {

	public Invoice setInvoice(Invoice invoice, List<Long> productIds, Long ccompanyId, Long productId);

	public Invoice calculateData(List<InvoiceLine> list, Invoice invoice);	

}
