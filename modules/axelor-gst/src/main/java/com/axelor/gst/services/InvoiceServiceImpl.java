package com.axelor.gst.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.AddressRepository;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.ContactRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;

public class InvoiceServiceImpl implements InvoiceService {

	@Inject
	InvoiceLineService service;

	@Inject
	InvoiceService invoiceService;

	@Override
	public Invoice setInvoice(Invoice invoice, List<Long> productIds, Long companyId, Long productId) {
		try {

			List<Product> pList = Beans.get(ProductRepository.class).all().filter("id in (?1)", productIds).fetch();
			Company company = Beans.get(CompanyRepository.class).find(companyId);
			Party party = Beans.get(PartyRepository.class).find(productId);
			invoice.setCompany(company);
			invoice.setParty(party);

			if (invoice.getParty().getContactList().stream().filter(a -> a.getType() == ContactRepository.CONTACT_TYPE_SELECT_PRIMARY).findFirst().isPresent()) {
				Contact partyContact = invoice.getParty().getContactList().stream().filter(a -> a.getType() == ContactRepository.CONTACT_TYPE_SELECT_PRIMARY)
						.findFirst().get();
				invoice.setPartyContact(partyContact);

			}

			if (invoice.getParty().getAddressList().stream().filter(a -> a.getType() == AddressRepository.ADDRESS_TYPE_SELECT_INVOICE).findFirst().isPresent()) {
				Address invoiceAddress = invoice.getParty().getAddressList().stream().filter(a -> a.getType() == AddressRepository.ADDRESS_TYPE_SELECT_INVOICE)
						.findFirst().get();
				invoice.setInvoiceAddress(invoiceAddress);
				invoice.setShippingAddress(invoiceAddress);
			}

			List<InvoiceLine> lineList = new ArrayList<InvoiceLine>();

			for (int i = 0; i < pList.size(); i++) {
				Product p = pList.get(i);
				InvoiceLine invoiceLine = new InvoiceLine();
				invoiceLine.setProduct(p);
				invoiceLine.setHsbn(p.getHsbn());
				invoiceLine.setItem("[" + p.getCode() + "] " + p.getName());
				invoiceLine.setGstRate(p.getGstRate());
				invoiceLine.setPrice(p.getCostPrice());
				if (invoice.getInvoiceAddress().getState().equals(invoice.getCompany().getAddress().getState())) {
					invoiceLine = service.calculateCgstSgst(invoiceLine);
				}

				else {
					invoiceLine = service.calculateIgst(invoiceLine);
				}

				lineList.add(invoiceLine);
			}
			invoice.setInvoiceItemsList(lineList);
			invoice = invoiceService.calculateData(lineList, invoice);
			return invoice;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@Override
	public Invoice calculateData(List<InvoiceLine> list, Invoice invoice) {

		BigDecimal amount = BigDecimal.ZERO, igst = BigDecimal.ZERO, gross = BigDecimal.ZERO,
				sgst_cgst = BigDecimal.ZERO;
		for (InvoiceLine l : list) {
			amount = amount.add(l.getNetAmount());
			sgst_cgst = sgst_cgst.add(l.getCgst());
			igst = igst.add(l.getIgst());
			gross = gross.add(l.getGrossAmount());
		}

		invoice.setNetAmount(amount);
		invoice.setNetIGST(igst);
		invoice.setNetCGST(sgst_cgst);
		invoice.setNetSGST(sgst_cgst);
		invoice.setGrossAmount(gross);

		return invoice;
	}

}
