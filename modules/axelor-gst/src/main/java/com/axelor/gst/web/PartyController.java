package com.axelor.gst.web;

import com.axelor.db.Model;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.services.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController {

	@Inject
	SequenceService seqService;

	public void setReference(ActionRequest request, ActionResponse response) {
		try {
			@SuppressWarnings("unchecked")
			Class<Model> dfg = (Class<Model>) request.getContext().getContextClass();
			Sequence seq = seqService.getSequenceModel(dfg);
			response.setValue("reference", seq.getNextNumber());
			seqService.generateNextSequence(seq);
		} catch (NullPointerException e) {
			response.setFlash("Please Set a Sequence for the Model");
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

}
