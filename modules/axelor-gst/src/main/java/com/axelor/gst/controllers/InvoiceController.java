package com.axelor.gst.controllers;

import java.util.List;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.ibm.icu.math.BigDecimal;

public class InvoiceController {

	public void setParty(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		response.setValue("partyContact",
				invoice.getParty().getContactList().stream().filter(c -> c.getType() == 1).findFirst());
		response.setValue("invoiceAddress",
				invoice.getParty().getAddressList().stream().filter(a -> a.getType() == 2).findFirst());
		if (invoice.getUseInvoiceAddress()) {
			response.setValue("shippingAddress", invoice.getInvoiceAddress());
		}
	}

	public void setShippingAddress(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (invoice.getUseInvoiceAddress()) {
			response.setValue("shippingAddress", invoice.getInvoiceAddress());
		} else {
			response.setValue("shippingAddress", "");
		}

	}

	public void changeStatus(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		switch (invoice.getStatus()) {

		case 1:
			response.setValue("status", 2);
			response.setAttr("statusBtn", "title", "Paid");
			break;

		case 2:
			response.setValue("status", 3);
			response.setAttr("statusBtn", "readonly", "true");
			break;
		}
	}

	public void onCancel(ActionRequest request, ActionResponse response) {
		response.setValue("status", 4);
		response.setAttr("cancelBtn", "readonly", "true");
		response.setAttr("statusBtn", "readonly", "true");
		response.setAttr("invoice-form", "canEdit", "false");
	}

	public void calculateNetGST(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> list = invoice.getInvoiceItemsList();
		long netAmount = list.stream().mapToLong(l -> l.getNetAmount().longValue()).sum();
		long netIGST = list.stream().mapToLong(l -> l.getIgst().longValue()).sum();
		long netCGST = list.stream().mapToLong(l -> l.getCgst().longValue()).sum();
		long netSGST = list.stream().mapToLong(l -> l.getSgst().longValue()).sum();
		long netGross = list.stream().mapToLong(l -> l.getGrossAmount().longValue()).sum();

		response.setValue("netAmount", BigDecimal.valueOf(netAmount));
		response.setValue("netIGST", BigDecimal.valueOf(netIGST));
		response.setValue("netCGST", BigDecimal.valueOf(netCGST));
		response.setValue("netSGST", BigDecimal.valueOf(netSGST));
		response.setValue("grossAmount", BigDecimal.valueOf(netGross));
	}

}
