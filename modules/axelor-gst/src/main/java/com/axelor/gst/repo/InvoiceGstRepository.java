package com.axelor.gst.repo;

import javax.persistence.PersistenceException;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.services.SequenceService;
import com.google.inject.Inject;

public class InvoiceGstRepository extends InvoiceRepository {

	@Inject
	SequenceService seqService;

	@Override
	public Invoice save(Invoice invoice) {
		try {
			Sequence seq = seqService.getSequenceModel(Invoice.class.getSimpleName());
			if (seq == null)
				throw new PersistenceException();

			if (invoice.getStatus() == INVOICE_STATUS_SELECT_VALIDATED) {
				invoice.setReference(seq.getNextNumber());
				seqService.generateNextSequence(seq);
				return super.save(invoice);
			}
			return super.save(invoice);
		} catch (Exception e) {
			throw new PersistenceException("Please set Sequence for this Model");
		}
	}
}
