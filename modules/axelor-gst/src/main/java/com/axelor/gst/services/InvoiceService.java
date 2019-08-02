package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;

public interface InvoiceService {

	public Invoice setInvoice(Invoice invoice, List<Long> productIds, Long ccompanyId, Long productId);

	public Invoice calculateData(List<InvoiceLine> list, Invoice invoice);	
	
	public Invoice getPartyContactAddress(Invoice invoice);
	
	public List<InvoiceLine> getInvoiceLineList(Invoice invoice,List<Product> productList,List<InvoiceLine> invoiceLineList);

}
