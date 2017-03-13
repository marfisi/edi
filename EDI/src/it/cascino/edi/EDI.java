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
import org.milyn.edi.unedifact.d96a.INVOIC.Invoic;
import org.milyn.edi.unedifact.d96a.INVOIC.SegmentGroup2;
import org.milyn.edi.unedifact.d96a.INVOIC.SegmentGroup25;
import org.milyn.edisax.model.internal.SegmentGroup;
import org.milyn.smooks.edi.unedifact.model.UNEdifactInterchange;
import org.milyn.smooks.edi.unedifact.model.r41.UNEdifactInterchange41;
import org.milyn.smooks.edi.unedifact.model.r41.UNEdifactMessage41;
import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class EDI{
	
	private Logger log = Logger.getLogger(EDI.class);
	
	StringBuilder stringBuilder = new StringBuilder();
		
	public EDI(String args[]){
		log.info("[" + "EDI");
		
		for(int numArg = 0; numArg < args.length; numArg++){
			if(args[numArg].compareTo("-soloModificati") == 0){
//				numArg++;
//			}else if(args[numArg].compareTo("-fI") == 0){
//				numArg++;
//				basePathInput = args[numArg];
			}else{ // se c'e' almeno un parametro e non e' tra quelli previsti stampo il messaggio d'aiuto
			}
		}
		
		D96AInterchangeFactory factory = null;
		try{
			factory = D96AInterchangeFactory.getInstance();
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SAXException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Boolean fileInteramenteElaborato = true;

        // Deserialize the UN/EDIFACT interchange stream to Java...
        InputStream stream = null;
		try{
			stream = new FileInputStream("INVOICE_CASCINO_MARAZZI.edi");
		}catch(FileNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /*------------------------------------------
        Read the interchange to Java Objects...
        -------------------------------------------*/
        UNEdifactInterchange interchange = null;
        try {
            interchange = factory.fromUNEdifact(stream);

            // Need to test which interchange syntax version.  Supports v4.1 at the moment...
            if (interchange instanceof UNEdifactInterchange41) {
                UNEdifactInterchange41 interchange41 = (UNEdifactInterchange41) interchange;

                System.out.println("\nJava Object Values:");
                //System.out.println("\tInterchange Sender ID: " + interchange41.getInterchangeHeader().getSender().getId());

                for (UNEdifactMessage41 messageWithControlSegments : interchange41.getMessages()) {
                    // Process the messages...

                    System.out.println("\tMessage Name: " + messageWithControlSegments.getMessageHeader().getMessageIdentifier().getId());

                    Object messageObj = messageWithControlSegments.getMessage();
                    if (messageObj instanceof Invoic) {
                        Invoic invoic = (Invoic) messageObj;
                        
                        System.out.println("Num Fattura: " + invoic.getBeginningOfMessage().getDocumentMessageNumber());
                        System.out.println("Data Fattura: " + invoic.getDateTimePeriod().get(0).getDateTimePeriod().getDateTimePeriod());
                        System.out.println("Art in Fattura: " + invoic.getControlTotal().get(0).getControl().getControlValue());
                       
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
                      	System.out.println("");
                      	sg2 = invoic.getSegmentGroup2().get(2);
                       	System.out.println("Qualifica: " + sg2.getNameAndAddress().getPartyQualifier());
                      	System.out.println("Nome: " + sg2.getNameAndAddress().getPartyName().getPartyName1());
                      	System.out.println("Indicod/GLN: " + sg2.getNameAndAddress().getPartyIdentificationDetails().getPartyIdIdentification());
                      	System.out.println(sg2.getNameAndAddress().getStreet().getStreetAndNumberPOBox1() + "  " + sg2.getNameAndAddress().getCityName() + "  " + sg2.getNameAndAddress().getPostcodeIdentification() + "  " + sg2.getNameAndAddress().getCountryCoded());
                      	System.out.println("");
                      	sg2 = invoic.getSegmentGroup2().get(3);
                       	System.out.println("Qualifica: " + sg2.getNameAndAddress().getPartyQualifier());
                      	System.out.println("Indicod/GLN: " + sg2.getNameAndAddress().getPartyIdentificationDetails().getPartyIdIdentification()); 
                        
                      	System.out.println("");
                      	for(SegmentGroup25 sg25 : invoic.getSegmentGroup25()){
                     		System.out.println("Item: " + sg25.getLineItem().getLineItemNumber());
                     		System.out.println("Barcode: " + sg25.getLineItem().getItemNumberIdentification().getItemNumber());
                     		System.out.println("Codice Fornitore: " + sg25.getAdditionalProductId().get(0).getItemNumberIdentification1().getItemNumber());
                     		System.out.println("Desc: " + sg25.getItemDescription().get(0).getItemDescription().getItemDescription1());
                     		System.out.println("Misura Lorda: " + sg25.getMeasurements().get(0).getValueRange().getMeasurementValue() + " " + sg25.getMeasurements().get(0).getValueRange().getMeasureUnitQualifier());             	
                     		System.out.println("Misura Netta: " + sg25.getMeasurements().get(1).getValueRange().getMeasurementValue() + " " + sg25.getMeasurements().get(1).getValueRange().getMeasureUnitQualifier());
                     		System.out.println("Qty colli: " + sg25.getQuantity().get(1).getQuantityDetails().getQuantity() + " " + sg25.getQuantity().get(1).getQuantityDetails().getMeasureUnitQualifier());             	
                    		System.out.println("Qty carico: " + sg25.getQuantity().get(2).getQuantityDetails().getQuantity() + " " + sg25.getQuantity().get(2).getQuantityDetails().getMeasureUnitQualifier());             	
                    		System.out.println("Qty Fatturata: " + sg25.getQuantity().get(0).getQuantityDetails().getQuantity() + " " + sg25.getQuantity().get(0).getQuantityDetails().getMeasureUnitQualifier());             	
                    		System.out.println("Prezzo netto: " + sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getPrice() +"/" + sg25.getSegmentGroup28().get(0).getPriceDetails().getPriceInformation().getMeasureUnitQualifier());             	
                       		System.out.println("Ammontare: " + sg25.getSegmentGroup26().get(0).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());             	
                       		System.out.println("iva: " + sg25.getSegmentGroup33().get(0).getDutyTaxFeeDetails().getDutyTaxFeeDetail().getDutyTaxFeeRate());             	
                    		
                    		System.out.println("");
                      	}

                      	System.out.println("Ammontare: ");
                     	System.out.println("TOT: " + invoic.getSegmentGroup48().get(0).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());
                     	System.out.println("tot articoli: " + invoic.getSegmentGroup48().get(1).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());
                     	System.out.println("iva: " + invoic.getSegmentGroup48().get(2).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());
                     	System.out.println("imponibile: " + invoic.getSegmentGroup48().get(3).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());
                     	System.out.println("cambio: " + invoic.getSegmentGroup48().get(4).getMonetaryAmount().getMonetaryAmount().getMonetaryAmount());
                     	System.out.println("");
                     	
                     	
                  		
                      	System.out.println("");
                    }
                }
            }
        }catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            
        }
               
        try{
			stream.close();
		}catch(IOException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        /*-----------------------------------
        Write interchange to Stdout...
        ------------------------------------*/
        StringWriter ediOutStream = new StringWriter();

        try{
			factory.toUNEdifact(interchange, ediOutStream);
		}catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(fileInteramenteElaborato){
        	// rinomino il file dato che non contiene più info che possono servire
        }

        System.out.println("\n\nSerialized Interchanged:");
        System.out.println("\t" + ediOutStream);

		log.info("]" + "EDI");
	}
}