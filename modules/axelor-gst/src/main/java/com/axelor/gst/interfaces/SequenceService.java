package com.axelor.gst.interfaces;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

	public void generateNextSequence(Sequence seq);
}
