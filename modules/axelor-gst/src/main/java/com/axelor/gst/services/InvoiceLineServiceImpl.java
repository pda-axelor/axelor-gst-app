package com.axelor.gst.services;

import java.math.BigDecimal;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.interfaces.InvoiceLineService;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public BigDecimal getNetAmount(InvoiceLine iLine) {
		return BigDecimal.valueOf(iLine.getQty()).multiply(iLine.getPrice());
	}

	@Override
	public BigDecimal getSgstCgst(InvoiceLine iLine, BigDecimal amount) {
		return amount.multiply(iLine.getGstRate().divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(2)));
	}

	@Override
	public BigDecimal getGrossAmount1(BigDecimal amount, BigDecimal SGSTCGST) {
		return amount.add(SGSTCGST.multiply(BigDecimal.valueOf(2)));
	}

	@Override
	public BigDecimal getGrossAmount2(BigDecimal IGST, BigDecimal amount) {
		return amount.add(IGST);
	}

	@Override
	public BigDecimal getIgst(InvoiceLine iLine, BigDecimal amount) {
		return amount.multiply(iLine.getGstRate().divide(BigDecimal.valueOf(100)));
	}

}
