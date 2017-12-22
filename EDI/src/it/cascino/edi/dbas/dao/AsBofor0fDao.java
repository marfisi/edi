package it.cascino.edi.dbas.dao;

import java.util.List;
import it.cascino.edi.dbas.model.AsBofor0f;

public interface AsBofor0fDao{
	List<AsBofor0f> getAll();

	List<AsBofor0f> getDaFornitoreEIdBolla(Integer bfcof, String bfcau, Integer bfdbf, String bfnbf);
	
	void close();
}
