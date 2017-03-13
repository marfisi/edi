package it.cascino.edi.dbas.model.pkey;

import java.io.Serializable;
import javax.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;

@Embeddable
public class AsMovma0fPKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer vdatr;
	private String vcaus;
	private Integer vnura;
	private Integer vnumd;
	private Integer vprog;
	
	public AsMovma0fPKey(){
	}
	
	public Integer getVdatr(){
		return vdatr;
	}
	
	public void setVdatr(Integer vdatr){
		this.vdatr = vdatr;
	}
	
	public String getVcaus(){
		return vcaus;
	}
	
	public void setVcaus(String vcaus){
		this.vcaus = vcaus;
	}
	
	public Integer getVnura(){
		return vnura;
	}
	
	public void setVnura(Integer vnura){
		this.vnura = vnura;
	}
	
	public Integer getVnumd(){
		return vnumd;
	}
	
	public void setVnumd(Integer vnumd){
		this.vnumd = vnumd;
	}
	
	public Integer getVprog(){
		return vprog;
	}
	
	public void setVprog(Integer vprog){
		this.vprog = vprog;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vcaus == null) ? 0 : vcaus.hashCode());
		result = prime * result + ((vdatr == null) ? 0 : vdatr.hashCode());
		result = prime * result + ((vnumd == null) ? 0 : vnumd.hashCode());
		result = prime * result + ((vnura == null) ? 0 : vnura.hashCode());
		result = prime * result + ((vprog == null) ? 0 : vprog.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsMovma0fPKey) {
			if((this.vdatr == ((AsMovma0fPKey)obj).vdatr) && (this.vcaus == ((AsMovma0fPKey)obj).vcaus) && (this.vnura == ((AsMovma0fPKey)obj).vnura) && (this.vnumd == ((AsMovma0fPKey)obj).vnumd) && (this.vprog == ((AsMovma0fPKey)obj).vprog)) {
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
		stringBuilder.append("AsMovma0fPKey");
		stringBuilder.append("[");
		stringBuilder.append("vdatr=" + vdatr).append(", ");
		stringBuilder.append("vcaus=" + StringUtils.trim(vcaus)).append(", ");
		stringBuilder.append("vnura=" + vnura).append(", ");
		stringBuilder.append("vnumd=" + vnumd).append(", ");
		stringBuilder.append("vprog=" + vprog);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}