package com.axelor.gst.repo;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.services.SequenceService;
import com.google.inject.Inject;

public class PartyGstRepository extends PartyRepository {

	@Inject
	SequenceService seqService;

	@Override
	public Party save(Party party) {
		try {
			Sequence seq = seqService.getSequenceModel(Party.class.getSimpleName());

			if (seq == null) {
				throw new PersistenceException();
			}

			party.setReference(seq.getNextNumber());
			seqService.generateNextSequence(seq);
			return super.save(party);

		} catch (Exception e) {
			throw new PersistenceException("Please set Sequence for this Model");
		}
	}
}
