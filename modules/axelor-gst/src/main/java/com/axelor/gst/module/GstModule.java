package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.InvoiceLineServiceImpl;
import com.axelor.gst.services.SequenceService;
import com.axelor.gst.services.SequenceServiceImpl;

public class GstModule extends AxelorModule {

	@Override
	protected void configure() {
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
	}

}
