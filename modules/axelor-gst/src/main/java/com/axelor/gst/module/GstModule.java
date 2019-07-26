package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.repo.InvoiceSequenceRepository;
import com.axelor.gst.repo.PartySequenceRepository;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.InvoiceLineServiceImpl;
import com.axelor.gst.services.SequenceService;
import com.axelor.gst.services.SequenceServiceImpl;

public class GstModule extends AxelorModule {

	@Override
	protected void configure() {
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(PartyRepository.class).to(PartySequenceRepository.class);
		bind(InvoiceRepository.class).to(InvoiceSequenceRepository.class);
	}

}
