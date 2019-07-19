package com.axelor.gst.services;

import java.math.BigDecimal;

import com.axelor.gst.interfaces.InvoiceLineService;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public BigDecimal add(BigDecimal n1, BigDecimal n2) {
		return n1.add(n2);
	}

	@Override
	public BigDecimal multiply(BigDecimal n1, BigDecimal n2) {
		return n1.multiply(n2);
	}

	@Override
	public BigDecimal multiply(Integer n1, BigDecimal n2) {
		return BigDecimal.valueOf(n1).multiply(n2);
	}

	@Override
	public BigDecimal divide(BigDecimal n1, Integer n2) {
		return n1.divide(BigDecimal.valueOf(n2));
	}

}
