package com.axelor.gst.web;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		try {
			request.getContext().put("AttachmentPath", AppSettings.get().get("file.upload.dir"));
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
