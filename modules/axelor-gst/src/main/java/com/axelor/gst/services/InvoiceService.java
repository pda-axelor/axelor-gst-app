package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {

	public Invoice setInvoice(Invoice invoice, List<Long> productIds, Long cId, Long pId);

	public List<BigDecimal> getCalculations(List<InvoiceLine> list);

}
