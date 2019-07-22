package com.axelor.gst.services;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.interfaces.SequenceService;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

	@Override
	@Transactional
	public void generateNextSequence(Sequence seq) {
		String sequence = seq.getNextNumber();
		int pref = seq.getPrefix().length();
		int pad = seq.getPadding();
		long number = Long.parseLong(sequence.substring(pref, pad + pref));
		sequence = String.valueOf(number + 1);
		String padNumber = "";
		try {
			if (sequence.length() > pad)
				throw new Exception();

			for (int i = 0; i < (pad - sequence.length()); i++) {
				padNumber = padNumber + "0";
			}

			if (seq.getSuffix() == null) {
				sequence = seq.getPrefix() + padNumber + sequence;
			} else {
				sequence = seq.getPrefix() + padNumber + sequence + seq.getSuffix();
			}

			seq.setNextNumber(sequence);
			Beans.get(SequenceRepository.class).save(seq);
		} catch (Exception e) {
			System.out.println("End of Padding");
		}
	}
}
