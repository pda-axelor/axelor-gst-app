package com.axelor.gst.web;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {

		request.getContext().put("AttachmentPath", AppSettings.get().get("file.upload.dir"));
		Product product = request.getContext().asType(Product.class);
		if (product.getImage() != null) {
			request.getContext().put("FilePath", product.getImage().getFilePath());
		} else {
			request.getContext().put("FilePath", "1.jpg");
		}

	}

}
