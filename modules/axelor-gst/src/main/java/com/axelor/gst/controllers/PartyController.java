package com.axelor.gst.controllers;

import com.axelor.db.Model;
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
		Class<Model> dfg = (Class<Model>) request.getContext().getContextClass();
		Sequence seq = Beans.get(SequenceRepository.class).all().filter("self.model.name=?1", dfg.getSimpleName())
				.fetchOne();
		response.setValue("reference", seq.getNextNumber());
		seqService.generateNextSequence(seq);
	}

}
