package com.axelor.gst.controllers;

import com.axelor.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {

	
	public void setGST(ActionRequest request, ActionResponse response)
	{
		Product product=request.getContext().asType(Product.class);
		response.setValue("gstRate", product.getProductCategory().getGstRate());
	}
}
