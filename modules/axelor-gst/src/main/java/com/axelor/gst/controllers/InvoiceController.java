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
		response.setValue("partyContact", invoice.getParty().getContactList().get(0));
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

	public void onConfirm(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (invoice.getIsConfirmed()) {
			response.setValue("status", "2");
		} else {
			response.setValue("status", "1");
		}
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
