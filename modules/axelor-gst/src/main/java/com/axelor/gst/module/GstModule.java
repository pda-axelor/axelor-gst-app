package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.repo.InvoiceGstRepository;
import com.axelor.gst.repo.PartyGstRepository;
import com.axelor.gst.repo.ProductGstRepository;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.InvoiceLineServiceImpl;
import com.axelor.gst.services.InvoiceService;
import com.axelor.gst.services.InvoiceServiceImpl;
import com.axelor.gst.services.SequenceService;
import com.axelor.gst.services.SequenceServiceImpl;

public class GstModule extends AxelorModule {

	@Override
	protected void configure() {
		
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(PartyRepository.class).to(PartyGstRepository.class);
		bind(InvoiceRepository.class).to(InvoiceGstRepository.class);
		bind(ProductRepository.class).to(ProductGstRepository.class);

	}

}
