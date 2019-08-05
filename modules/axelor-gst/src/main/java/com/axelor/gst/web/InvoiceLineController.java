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
			if (invoiceLine.getProduct() != null) {
				if ((!invoice.getParty().getAddressList().isEmpty()) && invoice.getCompany().getAddress() != null) {
					invoiceLine.setHsbn(invoiceLine.getProduct().getHsbn());
					invoiceLine.setItem(
							"[" + invoiceLine.getProduct().getCode() + "] " + invoiceLine.getProduct().getName());
					invoiceLine.setGstRate(invoiceLine.getProduct().getGstRate());
					invoiceLine.setPrice(invoiceLine.getProduct().getCostPrice());
					invoiceLine = service.calculateAll(invoice, invoiceLine);
					response.setValues(invoiceLine);
				} else {
					response.setFlash("Address Field of Company or Party is Empty");
					response.setReload(true);
				}
			}
			else
			{
				response.setFlash("No Product Selected");
				response.setReload(true);
			}

		} catch (

		Exception e) {
			response.setFlash(e.toString());
		}

	}

}
