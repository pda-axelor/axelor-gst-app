package com.axelor.gst.web;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {

	public void setGST(ActionRequest request, ActionResponse response) {
		try {
			Product product = request.getContext().asType(Product.class);
			response.setValue("gstRate", product.getProductCategory().getGstRate());
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		try {
			request.getContext().put("AttachmentPath", AppSettings.get().get("file.upload.dir"));
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
