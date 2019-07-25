package com.axelor.gst.interfaces;

import java.math.BigDecimal;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

	public BigDecimal getNetAmount(InvoiceLine iLine);

	public BigDecimal getSgstCgst(InvoiceLine iLine,BigDecimal amount);

	public BigDecimal getGrossAmount1(BigDecimal amount, BigDecimal SGSTCGST);
	
	public BigDecimal getIgst(InvoiceLine iLine,BigDecimal amount);
	
	public BigDecimal getGrossAmount2(BigDecimal IGST,BigDecimal amount);

}
