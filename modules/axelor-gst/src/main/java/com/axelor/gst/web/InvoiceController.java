package com.axelor.gst.web;

import java.math.BigDecimal;
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
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.InvoiceService;
import com.axelor.gst.services.SequenceService;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	SequenceService seqService;

	@Inject
	InvoiceLineService iLineservice;

	@Inject
	InvoiceService invoiceService;

	public void changeStatus(ActionRequest request, ActionResponse response) {
		try {
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
		} catch (NullPointerException e) {
			response.setFlash("Please Set a Sequence for the Model");
		} catch (Exception e) {
			response.setFlash(e.toString());
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
		List<BigDecimal> cals = invoiceService.getCalculations(list);
		response.setValue("netAmount", cals.get(0));
		response.setValue("netIGST", cals.get(1));
		response.setValue("netCGST", cals.get(2));
		response.setValue("netSGST", cals.get(3));
		response.setValue("grossAmount", cals.get(4));

	}

	public void setAttachmentPath(ActionRequest request, ActionResponse response) {
		try {
			request.getContext().put("LogoAttachmentPath", AppSettings.get().get("file.upload.dir"));
			request.getContext().put("LogoPath",
					request.getContext().asType(Invoice.class).getCompany().getLogo().getFilePath());
		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

	public void setSelected(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		@SuppressWarnings("unchecked")
		List<Long> productIds = (List<Long>) request.getContext().get("ids");
		if (productIds != null) {
			Long pId = new Long(request.getContext().get("partyId").toString());
			Long cId = new Long(request.getContext().get("companyId").toString());
			response.setValues(invoiceService.setInvoice(invoice, productIds, cId, pId));
		}
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
