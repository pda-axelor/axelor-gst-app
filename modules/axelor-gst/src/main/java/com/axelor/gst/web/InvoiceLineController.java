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

			if (invoice.getParty() != null && invoice.getCompany() != null) {
				if (invoice.getInvoiceAddress() != null && invoice.getCompany().getAddress() != null) {

					invoiceLine.setHsbn(invoiceLine.getProduct().getHsbn());
					invoiceLine.setItem(
							"[" + invoiceLine.getProduct().getCode() + "] " + invoiceLine.getProduct().getName());
					invoiceLine.setGstRate(invoiceLine.getProduct().getGstRate());
					invoiceLine.setPrice(invoiceLine.getProduct().getCostPrice());

					if (invoice.getInvoiceAddress().getState().equals(invoice.getCompany().getAddress().getState())) {
						response.setValues(service.calculateCgstSgst(invoiceLine));
					}

					else {
						response.setValues(service.calculateIgst(invoiceLine));
					}

				} else {
					response.setFlash("Address of Company or Party Field is Empty, Please add those fields");
					response.setReload(true);
				}
			} else {
				response.setFlash("Please enter both Company and Party Fields");

			}
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
