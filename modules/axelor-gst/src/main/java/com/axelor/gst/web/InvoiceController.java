package com.axelor.gst.web;

import java.util.List;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.InvoiceService;
import com.axelor.gst.services.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	SequenceService seqService;

	@Inject
	InvoiceService invoiceService;

	@Inject
	InvoiceLineService invoiceLineService;

	public void changeStatus(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);

		switch (invoice.getStatus()) {

		case 1:
			response.setValue("status", InvoiceRepository.INVOICE_STATUS_SELECT_VALIDATED);
			break;

		case 2:
			response.setValue("status", InvoiceRepository.INVOICE_STATUS_SELECT_PAID);
			response.setAttr("statusBtn", "readonly", "true");
			break;
		}

	}

	public void onCancel(ActionRequest request, ActionResponse response) {
		response.setValue("status", InvoiceRepository.INVOICE_STATUS_SELECT_CANCELLED);
		response.setAttr("cancelBtn", "readonly", "true");
		response.setAttr("statusBtn", "readonly", "true");
	}

	public void calculateNetGST(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		List<InvoiceLine> list = invoice.getInvoiceItemsList();
		response.setValues(invoiceService.calculateData(list, invoice));

	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {

		request.getContext().put("LogoAttachmentPath", AppSettings.get().get("file.upload.dir"));
		request.getContext().put("LogoPath",
				request.getContext().asType(Invoice.class).getCompany().getLogo().getFilePath());

	}

	public void setSelected(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		invoice.setCompany(Beans.get(CompanyRepository.class).all().fetchOne());
		@SuppressWarnings("unchecked")
		List<Long> productIds = (List<Long>) request.getContext().get("ids");
		if (productIds != null) {
			Long productId = new Long(request.getContext().get("partyId").toString());
			Long companyId = new Long(request.getContext().get("companyId").toString());
			invoice = invoiceService.setInvoice(invoice, productIds, companyId, productId);
		}
		response.setValues(invoice);
	}

	public void setInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		response.setValue("partyContact", "");
		response.setValue("invoiceAddress", null);
		response.setValue("useInvoiceAddress", true);
		response.setValue("shippingAddress", "");
        System.out.println(invoice.getParty());
		if (invoice.getParty() != null) {

			invoice = invoiceService.getPartyContactAddress(invoice);
			response.setValue("partyContact", invoice.getPartyContact());
			if (invoice.getInvoiceAddress() != null && invoice.getCompany().getAddress()!=null) {
				response.setValue("invoiceAddress", invoice.getInvoiceAddress());
				response.setValue("useInvoiceAddress", true);
				response.setValue("shippingAddress", invoice.getShippingAddress());

				List<InvoiceLine> list = invoiceLineService.reCalculateInvoiceLine(invoice);
				response.setValue("invoiceItemsList", list);
				invoice = invoiceService.calculateData(list, invoice);
				response.setValue("netAmount", invoice.getNetAmount());
				response.setValue("netIGST", invoice.getNetIGST());
				response.setValue("netSGST", invoice.getNetSGST());
				response.setValue("netCGST", invoice.getNetCGST());
				response.setValue("grossAmount", invoice.getGrossAmount());
			} else {
				response.setReload(true);
				response.setFlash("No Address Found in Selected Party or Company");
			}
		}

	}

	public void invoiceView(ActionRequest request, ActionResponse response) {
		Party party = (Party) request.getContext().get("party");
		Company company = (Company) request.getContext().get("company");
		if (company.getAddress() != null && (!party.getAddressList().isEmpty())) {
			response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form")
					.context("partyId", party.getId()).context("companyId", company.getId())
					.context("ids", request.getContext().get("ids")).map());
			response.setCanClose(true);
		} else {
			response.setFlash("Selected Company or Party has No Address!");
			response.setReload(true);

		}

	}

}
