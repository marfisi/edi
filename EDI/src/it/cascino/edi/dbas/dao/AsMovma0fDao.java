package it.cascino.edi.dbas.dao;

import java.util.List;
import it.cascino.edi.dbas.model.AsMovma0f;

public interface AsMovma0fDao{
	List<AsMovma0f> getAll();
	
//	void salva(AsMovma0f a);
//	
//	void aggiorna(AsMovma0f a);
//	
//	void elimina(AsMovma0f a);

	List<AsMovma0f> getMov(Integer vdatr, String vcaus, Integer vnura, Integer vnumd);

	AsMovma0f getMovConRigo(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog);

	AsMovma0f getMovConCodart(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, String vcoda);

	String updPrezzo(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog, Float vprez);

	String updPrezzoESco(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog, Float vprez, Float vsco1, Float vsco2, Float vsco3);

	void close();
}
