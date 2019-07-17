package com.axelor.gst.controllers;

import java.math.BigDecimal;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {

	public void setProduct(ActionRequest request, ActionResponse response) {
		InvoiceLine iLine = request.getContext().asType(InvoiceLine.class);
		response.setValue("hsbn", iLine.getProduct().getHsbn());
		response.setValue("item", "[" + iLine.getProduct().getCode() + "] " + iLine.getProduct().getName());
		response.setValue("gstRate", iLine.getProduct().getGstRate());
		response.setValue("price", iLine.getProduct().getCostPrice());
	}

	public void calculateGST(ActionRequest request, ActionResponse response) {
		InvoiceLine iLine = request.getContext().asType(InvoiceLine.class);
        BigDecimal amount = BigDecimal.valueOf(iLine.getQty()).multiply(iLine.getPrice());
		response.setValue("netAmount", amount);
		response.setValue("igst", amount.multiply(iLine.getGstRate()));
		response.setValue("sgst", (amount.multiply(iLine.getGstRate())).divide(BigDecimal.valueOf(2)));
		response.setValue("cgst", (amount.multiply(iLine.getGstRate())).divide(BigDecimal.valueOf(2)));
		response.setValue("grossAmount", amount.add(iLine.getIgst()));
        
	}
}
