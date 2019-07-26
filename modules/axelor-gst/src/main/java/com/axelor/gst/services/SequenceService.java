package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public Sequence getSequenceModel(String name);

	public void generateNextSequence(Sequence seq);
}
