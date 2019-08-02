package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.google.inject.Inject;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Inject
	InvoiceLineService service;

	@Override
	public InvoiceLine calculateCgstSgst(InvoiceLine invoiceLine) {
		BigDecimal amount = BigDecimal.valueOf(invoiceLine.getQty()).multiply(invoiceLine.getPrice());
		BigDecimal sgst_cgst = amount
				.multiply(invoiceLine.getGstRate().divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(2)));
		invoiceLine.setNetAmount(amount);
		invoiceLine.setCgst(sgst_cgst);
		invoiceLine.setSgst(sgst_cgst);
		invoiceLine.setGrossAmount(amount.add(sgst_cgst.multiply(BigDecimal.valueOf(2))));
		return invoiceLine;
	}

	@Override
	public InvoiceLine calculateIgst(InvoiceLine invoiceLine) {
		BigDecimal amount = BigDecimal.valueOf(invoiceLine.getQty()).multiply(invoiceLine.getPrice());
		BigDecimal igst = amount.multiply(invoiceLine.getGstRate().divide(BigDecimal.valueOf(100)));
		invoiceLine.setNetAmount(amount);
		invoiceLine.setIgst(igst);
		invoiceLine.setGrossAmount(amount.add(igst));
		return invoiceLine;
	}

	@Override
	public InvoiceLine calculateAll(Invoice invoice, InvoiceLine invoiceLine) {

		if (invoice.getInvoiceAddress().getState().equals(invoice.getCompany().getAddress().getState())) {
			invoiceLine.setIgst(BigDecimal.ZERO);
			invoiceLine = service.calculateCgstSgst(invoiceLine);
		}

		else {
			invoiceLine.setCgst(BigDecimal.ZERO);
			invoiceLine.setSgst(BigDecimal.ZERO);
			invoiceLine = service.calculateIgst(invoiceLine);
		}
		return invoiceLine;
	}

	@Override
	public List<InvoiceLine> reCalculateInvoiceLine(Invoice invoice) {

		List<InvoiceLine> list = invoice.getInvoiceItemsList();
		if (list != null) {
			for (InvoiceLine invoiceLine : list) {
				service.calculateAll(invoice, invoiceLine);
			}
		}

		return list;
	}

}
