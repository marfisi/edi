package it.cascino.edi.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import it.cascino.edi.dbas.model.pkey.AsBofor0fPKey;

/**
* The persistent class for the cas_dat/bofor0f database table.
* 
*/
@Entity(name = "Bofor0f")
@NamedQueries({
	@NamedQuery(name = "AsBofor0f.findAll", query = "SELECT a FROM Bofor0f a"),
	@NamedQuery(name = "AsBofor0f.findPerFornitoreEPerBolla", query = "SELECT a FROM Bofor0f a WHERE a.bfcof = :bfcof and a.bfdbf >= :bfdbf and upper(a.bfnbf) like :bfnbf")
})
public class AsBofor0f implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private AsBofor0fPKey id;
	private Integer bfcof;
	private Integer bfpfc;
	private Integer bfdbf;
	private String bfnbf;
	
	public AsBofor0f(){
		this.id = new AsBofor0fPKey();
	}
	
	public AsBofor0f(AsBofor0fPKey id, Integer bfcof, Integer bfpfc, Integer bfdbf, String bfnbf){
		super();
		this.id = id;
		this.bfcof = bfcof;
		this.bfpfc = bfpfc;
		this.bfdbf = bfdbf;
		this.bfnbf = bfnbf;
	}

	public AsBofor0fPKey getId(){
		return id;
	}

	public void setId(AsBofor0fPKey id){
		this.id = id;
	}

	public Integer getBfcof(){
		return bfcof;
	}

	public void setBfcof(Integer bfcof){
		this.bfcof = bfcof;
	}

	public Integer getBfpfc(){
		return bfpfc;
	}

	public void setBfpfc(Integer bfpfc){
		this.bfpfc = bfpfc;
	}

	public Integer getBfdbf(){
		return bfdbf;
	}

	public void setBfdbf(Integer bfdbf){
		this.bfdbf = bfdbf;
	}

	public String getBfnbf(){
		return bfnbf;
	}

	public void setBfnbf(String bfnbf){
		this.bfnbf = bfnbf;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bfcof == null) ? 0 : bfcof.hashCode());
		result = prime * result + ((bfdbf == null) ? 0 : bfdbf.hashCode());
		result = prime * result + ((bfnbf == null) ? 0 : bfnbf.hashCode());
		result = prime * result + ((bfpfc == null) ? 0 : bfpfc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsBofor0f) {
			if(this.id == ((AsBofor0f)obj).id) {
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		stringBuilder.append("id=" + StringUtils.trim(id.toString())).append(", ");
		stringBuilder.append("bfcof=" + bfcof).append(", ");
		stringBuilder.append("bfpfc=" + bfpfc).append(", ");
		stringBuilder.append("bfdbf=" + bfdbf).append(", ");
		stringBuilder.append("bfnbf=" + StringUtils.trim(bfnbf));
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}