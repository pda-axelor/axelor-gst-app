package com.axelor.gst.web;

import java.math.BigDecimal;

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
			InvoiceLine iLine = request.getContext().asType(InvoiceLine.class);
			Invoice invoice = request.getContext().getParent().asType(Invoice.class);

			if (invoice.getInvoiceAddress() != null && invoice.getCompany().getAddress() != null) {
				BigDecimal amount = service.getNetAmount(iLine);

				response.setValue("hsbn", iLine.getProduct().getHsbn());
				response.setValue("item", "[" + iLine.getProduct().getCode() + "] " + iLine.getProduct().getName());
				response.setValue("gstRate", iLine.getProduct().getGstRate());
				response.setValue("price", iLine.getProduct().getCostPrice());

				response.setValue("netAmount", amount);

				if (invoice.getInvoiceAddress().getState().equals(invoice.getCompany().getAddress().getState())) {

					BigDecimal SGST_CGST = service.getSgstCgst(iLine, amount);
					response.setValue("sgst", SGST_CGST);
					response.setValue("cgst", SGST_CGST);
					response.setValue("grossAmount", service.getGrossAmount1(amount, SGST_CGST));

				}

				else {

					BigDecimal IGST = service.getIgst(iLine, amount);
					response.setValue("igst", IGST);
					response.setValue("grossAmount", service.getGrossAmount2(IGST, amount));

				}

			} else {
				response.setFlash("Address of Company or Party Field is Empty, Please add those fields");
				response.setReload(true);
			}

		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
