package com.axelor.gst.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

	@Inject
	InvoiceLineService service;

	public void setProduct(ActionRequest request, ActionResponse response) {
		try {
			InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
			Invoice invoice = request.getContext().getParent().asType(Invoice.class);

			invoiceLine.setHsbn(invoiceLine.getProduct().getHsbn());
			invoiceLine.setItem("[" + invoiceLine.getProduct().getCode() + "] " + invoiceLine.getProduct().getName());
			invoiceLine.setGstRate(invoiceLine.getProduct().getGstRate());
			invoiceLine.setPrice(invoiceLine.getProduct().getCostPrice());
			response.setValues(service.calculateAll(invoice, invoiceLine));
			

		} catch (

		Exception e) {
			response.setFlash(e.toString());
		}

	}

}
