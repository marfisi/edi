package it.cascino.edi;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunEDI{
	private static Logger log;
	
	public static void main(String[] args){
		PropertyConfigurator.configure("logdir/logEDI.properties");
		log = Logger.getLogger(RunEDI.class);
		log.info("START");
		
		@SuppressWarnings("unused")
		EDI ied  = new EDI(args);
		
		log.info("STOP");
		System.exit(0);
	}
}
