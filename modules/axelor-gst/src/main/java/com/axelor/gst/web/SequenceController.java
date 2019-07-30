package com.axelor.gst.web;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.services.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class SequenceController {

	@Inject
	SequenceService service;

	public void calculateSequence(ActionRequest request, ActionResponse response) {

		Sequence seq = request.getContext().asType(Sequence.class);
		response.setValue("nextNumber", service.generateFirstSequence(seq));

	}

}
