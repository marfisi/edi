package it.cascino.edi;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.milyn.SmooksException;
import org.milyn.edi.unedifact.d96a.D96AInterchangeFactory;
import org.milyn.edi.unedifact.d96a.DESADV.Desadv;
import org.milyn.edi.unedifact.d96a.INVOIC.Invoic;
import org.milyn.edi.unedifact.d96a.INVOIC.SegmentGroup2;
import org.milyn.edi.unedifact.d96a.INVOIC.SegmentGroup25;
import org.milyn.edisax.model.internal.SegmentGroup;
import org.milyn.smooks.edi.unedifact.model.UNEdifactInterchange;
import org.milyn.smooks.edi.unedifact.model.r41.UNEdifactInterchange41;
import org.milyn.smooks.edi.unedifact.model.r41.UNEdifactMessage41;
import org.xml.sax.SAXException;
import it.cascino.edi.dbas.dao.AsBofor0fDao;
import it.cascino.edi.dbas.dao.AsMovma0fDao;
import it.cascino.edi.dbas.managmentbean.AsBofor0fDaoMng;
import it.cascino.edi.dbas.managmentbean.AsMovma0fDaoMng;
import it.cascino.edi.dbas.model.AsBofor0f;
import it.cascino.edi.dbas.model.AsMovma0f;
import it.cascino.edi.dbas.dao.AsAncab0fDao;
import it.cascino.edi.dbas.managmentbean.AsAncab0fDaoMng;
import it.cascino.edi.dbas.model.AsAncab0f;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EDI{
	
	private Logger log = Logger.getLogger(EDI.class);
	
	private StringBuilder stringBuilder = new StringBuilder();
	
	private AsBofor0fDao asBofor0fDao = new AsBofor0fDaoMng();
	private List<AsBofor0f> asBofor0fLs;
	
	private AsMovma0fDao asMovma0fDao = new AsMovma0fDaoMng();
	private List<AsMovma0f> asMovma0fLs;
	
	private AsAncab0fDao asAncab0fDao = new AsAncab0fDaoMng();
	private List<AsAncab0f> asAncab0fLs;
	
	private File fileEdi = null;
	private String tipoDoc = null;
	private String fornitore = null;
	private Integer codiceFornitore = null;
	
	public EDI(String args[]){
		log.info("[" + "EDI");
		
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-file") == 0) {
				numArg++;
				fileEdi = new File(args[numArg]);
			}else if(args[numArg].compareTo("-tipoDoc") == 0) {
				numArg++;
				tipoDoc = args[numArg];	// fattura o bolla
				if(StringUtils.equals(tipoDoc, "fattura")){
					tipoDoc = "I";	// INVOIC - fattura
				}else if(StringUtils.equals(tipoDoc, "bolla")){
					tipoDoc = "D";	// DESADV - bolla
				}else{
					tipoDoc = "E";	// errore
					log.error("Tipo documento errato");
				}
			}else if(args[numArg].compareTo("-fornitore") == 0) {
				numArg++;
				fornitore = args[numArg];	// marazzi, giochipreziosi
				if(StringUtils.equals(fornitore, "marazzi")){
					codiceFornitore = 4015;
				}else if(StringUtils.equals(fornitore, "giochipreziosi")){
					codiceFornitore = 5478;
				}else{
					codiceFornitore = 0;
					log.error("Fornitore errato");
				}
			}else{ // se c'e' almeno un parametro e non e' tra quelli previsti stampo il messaggio d'aiuto
			}
		}
		
		D96AInterchangeFactory factory = null;
		try{
			factory = D96AInterchangeFactory.getInstance();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
		
		Boolean fileInteramenteElaborato = true;
		
		log.info("File: " + fileEdi.getAbsolutePath());
		
		// Deserialize the UN/EDIFACT interchange stream to Java...
		InputStream stream = null;
		try{
			stream = new FileInputStream(fileEdi);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		UNEdifactInterchange interchange = null;
		try{
			interchange = factory.fromUNEdifact(stream);
			
			if(interchange instanceof UNEdifactInterchange41) {
				UNEdifactInterchange41 interchange41 = (UNEdifactInterchange41)interchange;
				
				for(UNEdifactMessage41 messageWithControlSegments : interchange41.getMessages()){
					System.out.println("\tMessage Name: " + messageWithControlSegments.getMessageHeader().getMessageIdentifier().getId());
					
					Object messageObj = messageWithControlSegments.getMessage();
					if(StringUtils.equals(tipoDoc, "I")){
						if(messageObj instanceof Invoic){
							Invoic invoic = (Invoic)messageObj;
							
							log.info("Num Fattura: " + invoic.getBeginningOfMessage().getDocumentMessageNumber());
							log.info("Data Fattura: " + invoic.getDateTimePeriod().get(0).getDateTimePeriod().getDateTimePeriod());
							//log.info("Art in Fattura: " + invoic.getControlTotal().get(0).getControl().getControlValue());
							
							SegmentGroup2 sg2 = invoic.getSegmentGroup2().get(0);
							System.out.println("Qualifica: " + sg2.getNameAndAddress().getPartyQualifier());
							System.out.println("Nome: " + sg2.getNameAndAddress().getPartyName().getPartyName1());
							System.out.println("Indicod/GLN: " + sg2.getNameAndAddress().getPartyIdentificationDetails().getPartyIdIdentification());
							System.out.println(sg2.getNameAndAddress().getStreet().getStreetAndNumberPOBox1() + "  " + sg2.getNameAndAddress().getCityName() + "  " + sg2.getNameAndAddress().getPostcodeIdentification() + "  " + sg2.getNameAndAddress().getCountryCoded());
							System.out.println("P.Iva: " + sg2.getSegmentGroup3().get(1).getReference().getReference().getReferenceNumber());
							System.out.println("");
							sg2 = invoic.getSegmentGroup2().get(1);
							System.out.println("Qualifica: " + sg2.getNameAndAddress().getPartyQualifier());
							System.out.println("Nome: " + sg2.getNameAndAddress().getPartyName().getPartyName1());
							System.out.println("Indicod/GLN: " + sg2.getNameAndAddress().getPartyIdentificationDetails().getPartyIdIdentification());
							System.out.println(sg2.getNameAndAddress().getStreet().getStreetAndNumberPOBox1() + "  " + sg2.getNameAndAddress().getCityName() + "  " + sg2.getNameAndAddress().getPostcodeIdentification() + "  " + sg2.getNameAndAddress().getCountryCoded());
							System.out.println("P.Iva: " + sg2.getSegmentGroup3().get(0).getReference().getReference().getReferenceNumber());
							
							if(codiceFornitore == 4015){	// marazzi
								for(SegmentGroup25 sg25 : invoic.getSegmentGroup25()){
									log.info("Item: " + sg25.getLineItem().getLineItemNumber());
									String idDDT = sg25.getSegmentGroup29().get(1).getReference().getReference().getReferenceNumber();
									log.info("DDT: " + idDDT);
									
									Integer dataDDT = Integer.parseInt(sg25.getSegmentGroup29().get(0).getDateTimePeriod().get(0).getDateTimePeriod().getDateTimePeriod());
									
									asBofor0fLs = asBofor0fDao.getDaFornitoreEIdBolla(codiceFornitore, "B1", dataDDT , idDDT);
									if(asBofor0fLs.isEmpty()){
										// provo a usare un id documento meno preciso
										log.warn("DDT "+ idDDT + " non trovato" );
										idDDT = StringUtils.substring(idDDT, StringUtils.lastIndexOf(idDDT, "00") + 2);
										log.info("provo con "+ idDDT);
										asBofor0fLs = asBofor0fDao.getDaFornitoreEIdBolla(codiceFornitore, "B1", dataDDT , idDDT);
										if(asBofor0fLs.isEmpty()){
											log.warn("DDT "+ idDDT + " nemmeno cosi' e' stato trovato" );
											fileInteramenteElaborato = false;
											// continuo con articolo successivo
											continue;
										}
									}						
									
									// se sono qui' ho trovato la bolla
									AsBofor0f asBofor0f = asBofor0fLs.get(0);
									log.info("trovato DDT: "+ asBofor0f);
									asMovma0fLs = asMovma0fDao.getMov(asBofor0f.getId().getBfdat(), asBofor0f.getId().getBfcau(), asBofor0f.getId().getBfnuz(), asBofor0f.getId().getBfnum());
								
									String codiceArticoloFornitore = sg25.getAdditionalProductId().get(0).getItemNumberIdentification1().getItemNumber();
									String descrizioneArticoloFornitore = sg25.getItemDescription().get(0).getItemDescription().getItemDescription1();
									BigDecimal qtyArticoloFatturata = sg25.getQuantity().get(0).getQuantityDetails().getQuantity();
									BigDecimal prezzoArticoloNetto = sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getPrice();
									log.info("Codice Fornitore: " + codiceArticoloFornitore);
									log.info("Desc: " + descrizioneArticoloFornitore);
									log.info("Qty Fatturata: " + qtyArticoloFatturata + " " + sg25.getQuantity().get(0).getQuantityDetails().getMeasureUnitQualifier());
									log.info("Prezzo netto: " + prezzoArticoloNetto + "/" + sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getMeasureUnitQualifier());
																
									List<AsMovma0f> asMovma0fPossibiliLs = new ArrayList<AsMovma0f>();
									
									AsMovma0f asMovma0f = null;
									Iterator<AsMovma0f> iter_asMovma0f = asMovma0fLs.iterator();
									while(iter_asMovma0f.hasNext()){
										asMovma0f = iter_asMovma0f.next();
										//System.out.println(asMovma0f.toString());
										
										Float qtyFatt = qtyArticoloFatturata.floatValue();
										if(Float.compare((Math.abs(qtyFatt - asMovma0f.getVquan())) / qtyFatt, 0.02f) < 0){	// sotto il 2%
											asMovma0fPossibiliLs.add(asMovma0f);
										}
									}
									
									if(asMovma0fPossibiliLs.size() == 0){
										fileInteramenteElaborato = false;
										// continuo con articolo successivo
										log.warn(codiceArticoloFornitore + " con quantita' " + qtyArticoloFatturata + " non trovato");
										continue;
									}else if(asMovma0fPossibiliLs.size() > 1){
										// devo cercare di portarlo ad un solo elemento
										 iter_asMovma0f = asMovma0fPossibiliLs.iterator();
										 while(iter_asMovma0f.hasNext()){
												asMovma0f = iter_asMovma0f.next();
												
												String codiceArticoloFornitoreCorto = StringUtils.substring(codiceArticoloFornitore, 2, 6);
												
												if(StringUtils.contains(asMovma0f.getVdesc(), codiceArticoloFornitoreCorto)){
													asMovma0fPossibiliLs.clear();
													asMovma0fPossibiliLs.add(asMovma0f);
													break;
												}
										 }
									}
									
									// nella lista dei possibili se ho solo un elemento e' lui
									if(asMovma0fPossibiliLs.size() == 1){
										asMovma0f = asMovma0fPossibiliLs.get(0);
										log.info("elaboro " + asMovma0f );
										log.info("provo ad aggiornare il prezzo con " + prezzoArticoloNetto);
										String risultatoUpdate = asMovma0fDao.updPrezzo(asMovma0f.getId().getVdatr(), asMovma0f.getId().getVcaus(), asMovma0f.getId().getVnura(), asMovma0f.getId().getVnumd(), asMovma0f.getId().getVprog(), prezzoArticoloNetto.floatValue());
										if(StringUtils.equals(risultatoUpdate, "1")){
											log.info("Aggiornato,  scritto il prezzo di " + prezzoArticoloNetto + " - " + codiceArticoloFornitore + " con quantita' " + qtyArticoloFatturata);
										}else{
											log.warn("il prezzo in db e' diverso da 0 ed e' " + asMovma0f.getVprez());
										}
									}else{
										log.warn("ATTENZIONE: asMovma0fPossibiliLs.size() = " + asMovma0fPossibiliLs.size());
										fileInteramenteElaborato = false;
									}
								}
							}else if(codiceFornitore == 5478){	// giochipreziosi
								for(SegmentGroup25 sg25 : invoic.getSegmentGroup25()){
									log.info("Item: " + sg25.getLineItem().getLineItemNumber());
									
									String codicebarre = null;
									try{
										codicebarre = sg25.getLineItem().getItemNumberIdentification().getItemNumber();
									}catch(Exception e){
										codicebarre = "9898989898989";
									}

									AsAncab0f asAncab0f = asAncab0fDao.getDaCcodb(codicebarre);
									if(asAncab0f == null){
										log.warn(codicebarre + " non agganciato a nessun articolo");
										fileInteramenteElaborato = false;
										continue;
									}
									String codicearticolo = asAncab0f.getCcoda();
									log.info(codicebarre + " articolo: " + codicearticolo);
									
									String idDDT = sg25.getSegmentGroup29().get(0).getReference().getReference().getReferenceNumber();
									log.info("DDT: " + idDDT);
									
									Integer dataDDT = Integer.parseInt(sg25.getSegmentGroup29().get(0).getDateTimePeriod().get(0).getDateTimePeriod().getDateTimePeriod());
									
									asBofor0fLs = asBofor0fDao.getDaFornitoreEIdBolla(codiceFornitore, "B2", dataDDT , idDDT);
									if(asBofor0fLs.isEmpty()){
										// provo a usare un id documento meno preciso
										log.warn("DDT "+ idDDT + " non trovato" );
										idDDT = StringUtils.stripStart(idDDT, "0");
										idDDT = StringUtils.substring(idDDT, 0, StringUtils.lastIndexOf(idDDT, "/"));
										log.info("provo con "+ idDDT);
										asBofor0fLs = asBofor0fDao.getDaFornitoreEIdBolla(codiceFornitore, "B2", dataDDT , idDDT);
										if(asBofor0fLs.isEmpty()){
											log.warn("DDT "+ idDDT + " nemmeno cosi' e' stato trovato" );
											fileInteramenteElaborato = false;
											// continuo con articolo successivo
											continue;
										}
									}						
									
									// se sono qui' ho trovato la bolla
									AsBofor0f asBofor0f = asBofor0fLs.get(0);
									log.info("trovato DDT: "+ asBofor0f);
									
									String codiceArticoloFornitore = sg25.getAdditionalProductId().get(0).getItemNumberIdentification1().getItemNumber();
									String descrizioneArticoloFornitore = sg25.getItemDescription().get(0).getItemDescription().getItemDescription1();
									BigDecimal qtyArticoloFatturata = sg25.getQuantity().get(0).getQuantityDetails().getQuantity();
									BigDecimal prezzoArticolo = sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getPrice();
									log.info("Codice Fornitore: " + codiceArticoloFornitore);
									log.info("Desc: " + descrizioneArticoloFornitore);
									log.info("Qty Fatturata: " + qtyArticoloFatturata + " " + sg25.getQuantity().get(0).getQuantityDetails().getMeasureUnitQualifier());
									log.info("Prezzo: " + prezzoArticolo + "/" + sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getMeasureUnitQualifier());
												
									BigDecimal sco1 = null;
									try{
										sco1 = sg25.getSegmentGroup38().get(0).getSegmentGroup40().getPercentageDetails().getPercentageDetails().getPercentage();
									}catch(IndexOutOfBoundsException e){
										sco1 = new BigDecimal(0.0f);
									}
									BigDecimal sco2 = null;
									try{
										sco2 = sg25.getSegmentGroup38().get(1).getSegmentGroup40().getPercentageDetails().getPercentageDetails().getPercentage();
									}catch(IndexOutOfBoundsException e){
										sco2= new BigDecimal(0.0f);
									}
									BigDecimal sco3 = null;
									try{
										sco3 = sg25.getSegmentGroup38().get(2).getSegmentGroup40().getPercentageDetails().getPercentageDetails().getPercentage();
									}catch(IndexOutOfBoundsException e){
										sco3 = new BigDecimal(0.0f);
									}
									
									AsMovma0f asMovma0f = asMovma0fDao.getMovConCodart(asBofor0f.getId().getBfdat(), asBofor0f.getId().getBfcau(), asBofor0f.getId().getBfnuz(), asBofor0f.getId().getBfnum(), codicearticolo);
									if(asMovma0f == null){
										log.warn(codicearticolo + " non trovato in movma " + asBofor0f);
										fileInteramenteElaborato = false;
										continue;
									}
									
									log.info("elaboro " + asMovma0f );
									log.info("provo ad aggiornare il prezzo con " + prezzoArticolo);
									String risultatoUpdate = asMovma0fDao.updPrezzoESco(asMovma0f.getId().getVdatr(), asMovma0f.getId().getVcaus(), asMovma0f.getId().getVnura(), asMovma0f.getId().getVnumd(), asMovma0f.getId().getVprog(), prezzoArticolo.floatValue(), sco1.floatValue(), sco2.floatValue(), sco3.floatValue());
									if(StringUtils.equals(risultatoUpdate, "1")){
										log.info("Aggiornato,  scritto il prezzo di " + prezzoArticolo + " - " + codiceArticoloFornitore + " con quantita' " + qtyArticoloFatturata);
									}else{
										log.warn("il prezzo in db e' diverso da 0 ed e' " + asMovma0f.getVprez());
									}
								}
							}else{
								log.error("Fornitore errato");
							}
						}
					}else if(StringUtils.equals(tipoDoc, "D")){
						if(messageObj instanceof Desadv){
							Desadv desadv = (Desadv)messageObj;
						}
						log.warn("non eleboriamo DDT");
					}else{
						log.error("Tipo documento errato");
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
		
		}
		
		try{
			stream.close();
		}catch(IOException e1){
			e1.printStackTrace();
		}
		
		StringWriter ediOutStream = new StringWriter();
		
		try{
			factory.toUNEdifact(interchange, ediOutStream);
		}catch(IOException e){
			e.printStackTrace();
		}

		if(fileInteramenteElaborato) {
			Path source = Paths.get(fileEdi.getAbsolutePath());
			try{
				Files.move(source, source.resolveSibling("elaborato_" + fileEdi.getName()), StandardCopyOption.REPLACE_EXISTING);
			}catch(IOException e){
				e.printStackTrace();
			}
			log.info("file rinominato");
		}else{
			log.info("file non lo rinomino dato che ancora c'e' qualcosa di utile");
		}
		
		asBofor0fDao.close();
		asAncab0fDao.close();
		asMovma0fDao.close();
		
		log.info("]" + "EDI");		
	}
}