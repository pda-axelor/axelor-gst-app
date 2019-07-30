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
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;

public class InvoiceServiceImpl implements InvoiceService {

	@Override
	public Invoice setInvoice(Invoice invoice, List<Long> productIds, Long cId, Long pId) {
		try {

			List<Product> pList = Beans.get(ProductRepository.class).all().filter("id in (?1)", productIds).fetch();
			Company company = Beans.get(CompanyRepository.class).find(cId);
			Party party = Beans.get(PartyRepository.class).find(pId);
			List<InvoiceLine> lineList = new ArrayList<InvoiceLine>();

			for (int i = 0; i < pList.size(); i++) {
				Product p = pList.get(i);
				InvoiceLine il = new InvoiceLine();
				il.setProduct(p);
				il.setItem("[" + p.getCode() + "] " + p.getName());
				il.setPrice(p.getCostPrice());
				
				lineList.add(il);
			}
			invoice.setInvoiceItemsList(lineList);
			invoice.setCompany(company);
			invoice.setParty(party);

			if (invoice.getParty().getContactList().stream().filter(a -> a.getType() == 1).findFirst().isPresent()) {
				Contact partyContact = invoice.getParty().getContactList().stream().filter(a -> a.getType() == 1)
						.findFirst().get();
				invoice.setPartyContact(partyContact);

			}

			if (invoice.getParty().getAddressList().stream().filter(a -> a.getType() == 2).findFirst().isPresent()) {
				Address invoiceAddress = invoice.getParty().getAddressList().stream().filter(a -> a.getType() == 2)
						.findFirst().get();
				invoice.setInvoiceAddress(invoiceAddress);
				invoice.setShippingAddress(invoiceAddress);
			}

			return invoice;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@Override
	public Invoice calculateData(List<InvoiceLine> list, Invoice invoice) {

		invoice.setNetAmount(list.stream().map(l -> l.getNetAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
		invoice.setNetIGST(list.stream().map(l -> l.getIgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		invoice.setNetCGST(list.stream().map(l -> l.getCgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		invoice.setNetSGST(list.stream().map(l -> l.getSgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
		invoice.setGrossAmount(list.stream().map(l -> l.getGrossAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
		return invoice;
	}

}
