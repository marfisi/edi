package it.cascino.edi.dbas.dao;

import java.util.List;
import it.cascino.edi.dbas.model.AsAncab0f;

public interface AsAncab0fDao{
	List<AsAncab0f> getAll();
	
	List<AsAncab0f> getDaCcoda(String ccoda);

	AsAncab0f getDaCcodb(String ccodb);

	void close();
}
