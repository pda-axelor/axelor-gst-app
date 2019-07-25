package com.axelor.gst.services;

import com.axelor.db.Model;
import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public Sequence getSequenceModel(Class<Model> dfg);

	public void generateNextSequence(Sequence seq);
}
