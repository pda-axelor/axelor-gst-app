package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.interfaces.InvoiceLineService;
import com.axelor.gst.interfaces.SequenceService;
import com.axelor.gst.services.InvoiceLineServiceImpl;
import com.axelor.gst.services.SequenceServiceImpl;

public class ServiceModule extends AxelorModule {

	@Override
	protected void configure() {
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
	}

}
