package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.axelor.app.AppSettings;
import com.axelor.db.Model;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.services.InvoiceLineService;
import com.axelor.gst.services.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceController {

	@Inject
	SequenceService seqService;
	
	@Inject
	InvoiceLineService iLineservice;

	public void changeStatus(ActionRequest request, ActionResponse response) {
		try {
			Invoice invoice = request.getContext().asType(Invoice.class);

			switch (invoice.getStatus()) {

			case 1:
				response.setValue("status", 2);
				@SuppressWarnings("unchecked")
				Class<Model> model = (Class<Model>) request.getContext().getContextClass();
				Sequence seq = seqService.getSequenceModel(model);
				response.setValue("reference", seq.getNextNumber());
				seqService.generateNextSequence(seq);
				break;

			case 2:
				response.setValue("status", 3);
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
		response.setValue("status", 4);
		response.setAttr("cancelBtn", "readonly", "true");
		response.setAttr("statusBtn", "readonly", "true");
	}

	public void calculateNetGST(ActionRequest request, ActionResponse response) {
		try {
			Invoice invoice = request.getContext().asType(Invoice.class);
			List<InvoiceLine> list = invoice.getInvoiceItemsList();
			response.setValue("netAmount",
					list.stream().map(l -> l.getNetAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
			response.setValue("netIGST", list.stream().map(l -> l.getIgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
			response.setValue("netCGST", list.stream().map(l -> l.getCgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
			response.setValue("netSGST", list.stream().map(l -> l.getSgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
			response.setValue("grossAmount",
					list.stream().map(l -> l.getGrossAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
		} catch (Exception e) {
			response.setFlash(e.toString());
		}
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
		try {
			Invoice invoice = request.getContext().asType(Invoice.class);

			if (request.getContext().get("ids") != null) {

				List<Long> productIds = (List<Long>) request.getContext().get("ids");
				Long cId = new Long(request.getContext().get("companyId").toString());
				Long pId = new Long(request.getContext().get("partyId").toString());
				List<Product> pList = Beans.get(ProductRepository.class).all().filter("id in (?1)", productIds).fetch();
				Company company = Beans.get(CompanyRepository.class).find(cId);
				Party party = Beans.get(PartyRepository.class).find(pId);
				List<InvoiceLine> lineList = new ArrayList<InvoiceLine>();

				for (int i = 0; i < pList.size(); i++) {
					Product p = pList.get(i);
					InvoiceLine il = new InvoiceLine();
					il.setProduct(p);
					il.setItem("["+p.getCode()+"] "+p.getName());
					il.setPrice(p.getCostPrice());
					lineList.add(il);
				}
				invoice.setInvoiceItemsList(lineList);
				invoice.setCompany(company);
				invoice.setParty(party);
				Address invoiceAddress = invoice.getParty().getAddressList().stream().filter(a -> a.getType() == 2)
						.findFirst().get();
				invoice.setInvoiceAddress(invoiceAddress);
				invoice.setShippingAddress(invoiceAddress);
			}

			System.out.println(request.getContext().entrySet());

			response.setValues(invoice);

		} catch (Exception e) {
			response.setFlash(e.toString());
		}

	}

	public void setParty(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		if (invoice.getParty() != null) {
			Optional<Address> invoiceAddress = invoice.getParty().getAddressList().stream()
					.filter(a -> a.getType() == 2).findFirst();
			response.setValue("partyContact",
					invoice.getParty().getContactList().stream().filter(c -> c.getType() == 1).findFirst());
			response.setValue("invoiceAddress", invoiceAddress);
			response.setValue("useInvoiceAddress", true);
			response.setValue("shippingAddress", invoiceAddress);
		}
	}

}
