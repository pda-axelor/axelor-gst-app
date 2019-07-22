package com.axelor.gst.controllers;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.interfaces.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class PartyController {

	@Inject
	SequenceService seqService;

	public void setReference(ActionRequest request, ActionResponse response) {
		Sequence seq = Beans.get(SequenceRepository.class).find((long) 1);
		response.setValue("reference", seq.getNextNumber());
		seqService.generateNextSequence(seq);
	}

}
