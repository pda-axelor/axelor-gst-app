package com.axelor.gst.interfaces;

import java.math.BigDecimal;

public interface InvoiceLineService {

	public BigDecimal add(BigDecimal n1, BigDecimal n2);

	public BigDecimal multiply(BigDecimal n1, BigDecimal n2);

	public BigDecimal multiply(Integer n1, BigDecimal n2);

	public BigDecimal divide(BigDecimal n1, Integer n2);

}
