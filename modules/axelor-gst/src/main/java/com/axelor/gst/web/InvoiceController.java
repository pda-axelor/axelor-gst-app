package com.axelor.gst.web;

import java.util.List;
import java.util.Optional;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.AddressRepository;
import com.axelor.gst.db.repo.ContactRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.CompanyRepository;
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

	public void setParty(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		if (invoice.getParty() != null) {
			Optional<Address> invoiceAddress = invoice.getParty().getAddressList().stream()
					.filter(a -> a.getType() == AddressRepository.ADDRESS_TYPE_SELECT_INVOICE).findFirst();
			response.setValue("partyContact", invoice.getParty().getContactList().stream()
					.filter(c -> c.getType() == ContactRepository.CONTACT_TYPE_SELECT_PRIMARY).findFirst());
			response.setValue("invoiceAddress", invoiceAddress);
			response.setValue("useInvoiceAddress", true);
			response.setValue("shippingAddress", invoiceAddress);
		} else {
			response.setValue("partyContact", "");
			response.setValue("invoiceAddress", null);
			response.setValue("useInvoiceAddress", true);
			response.setValue("shippingAddress", "");
		}
	}

	public void invoiceView(ActionRequest request, ActionResponse response) {
		Party party = (Party) request.getContext().get("party");
		Company company = (Company) request.getContext().get("company");

		response.setView(ActionView.define("Invoice").model(Invoice.class.getName()).add("form", "invoice-form")
				.context("partyId", party.getId()).context("companyId", company.getId())
				.context("ids", request.getContext().get("ids")).map());

	}

}
