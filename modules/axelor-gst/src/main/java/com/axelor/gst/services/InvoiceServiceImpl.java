package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.interfaces.InvoiceService;

public class InvoiceServiceImpl implements InvoiceService{

	@Override
	public BigDecimal getSumofField(List<InvoiceLine> list, String field) {
		return null;
	}

}
