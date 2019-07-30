package com.axelor.gst.services;

import java.math.BigDecimal;

import com.axelor.gst.db.InvoiceLine;

public class InvoiceLineServiceImpl implements InvoiceLineService {

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

}
