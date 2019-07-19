package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.interfaces.InvoiceLineService;
import com.axelor.gst.interfaces.InvoiceService;
import com.axelor.gst.services.InvoiceLineServiceImpl;
import com.axelor.gst.services.InvoiceServiceImpl;

public class ServiceModule extends AxelorModule{

	@Override
	protected void configure() {
		bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
	}

}
