package com.axelor.gst.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {

	public BigDecimal getSumofField(List<InvoiceLine> iLine, String field);

}
