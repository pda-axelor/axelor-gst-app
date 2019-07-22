package com.axelor.gst.controllers;

import java.math.BigDecimal;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.interfaces.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

	@Inject
	InvoiceLineService service;

	public void setProduct(ActionRequest request, ActionResponse response) {

		InvoiceLine iLine = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);

		BigDecimal amount = service.multiply(iLine.getQty(), iLine.getPrice());

		response.setValue("hsbn", iLine.getProduct().getHsbn());
		response.setValue("item", "[" + iLine.getProduct().getCode() + "] " + iLine.getProduct().getName());
		response.setValue("gstRate", iLine.getProduct().getGstRate());
		response.setValue("price", iLine.getProduct().getCostPrice());

		response.setValue("netAmount", amount);
		try {
			if (invoice.getInvoiceAddress().getState().getId() == invoice.getCompany().getAddress().getState()
					.getId()) {

				response.setValue("sgst", service.divide(service.multiply(amount, iLine.getGstRate()), 2));
				response.setValue("cgst", service.divide(service.multiply(amount, iLine.getGstRate()), 2));
				response.setValue("grossAmount", service.add(amount, service.add(iLine.getSgst(), iLine.getCgst())));

			}

			else {
				response.setValue("igst", service.multiply(amount, iLine.getGstRate()));
				response.setValue("grossAmount", service.add(amount, iLine.getIgst()));
			}

		} catch (Exception e) {
			response.setFlash("Address of Company or Party Field is Empty, Please add those fields");
		}

	}

}
