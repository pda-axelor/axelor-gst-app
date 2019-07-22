package com.axelor.gst.controllers;

import java.math.BigDecimal;
import java.util.List;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.interfaces.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	SequenceService seqService;

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
	}

	public void calculateNetGST(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		Sequence seq = Beans.get(SequenceRepository.class).find((long) 2);
		response.setValue("reference", seq.getNextNumber());
		seqService.generateNextSequence(seq);
		List<InvoiceLine> list = invoice.getInvoiceItemsList();
		response.setValue("netAmount",
				list.stream().map(l -> l.getNetAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
		response.setValue("netIGST", list.stream().map(l -> l.getIgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		response.setValue("netCGST", list.stream().map(l -> l.getCgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		response.setValue("netSGST", list.stream().map(l -> l.getSgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		response.setValue("grossAmount",
				list.stream().map(l -> l.getGrossAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
	}

}
