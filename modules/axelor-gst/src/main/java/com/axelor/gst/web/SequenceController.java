package com.axelor.gst.web;

import com.axelor.gst.db.Sequence;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class SequenceController {

	public void calculateSequence(ActionRequest request, ActionResponse response) {
		try {
			Sequence seq = request.getContext().asType(Sequence.class);
			String padding = "", number;

			for (int i = 1; i < seq.getPadding(); i++) {
				padding = padding + "0";
			}

			if (seq.getSuffix() == null) {

				number = seq.getPrefix() + padding + "1";

			} else {
				number = seq.getPrefix() + padding + "1" + seq.getSuffix();
			}

			response.setValue("nextNumber", number);
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
