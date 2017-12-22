package it.cascino.edi.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.edi.dbas.model.AsBofor0f;
import it.cascino.edi.utils.Resources;
import it.cascino.edi.dbas.dao.AsBofor0fDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class AsBofor0fDaoMng implements AsBofor0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsBofor0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsBofor0f> getAll(){
		List<AsBofor0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsBofor0f.findAll");
				o = (List<AsBofor0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
		
	@SuppressWarnings("unchecked")
	public List<AsBofor0f> getDaFornitoreEIdBolla(Integer bfcof, String bfcau, Integer bfdbf, String bfnbf){
		List<AsBofor0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsBofor0f.findPerFornitoreEPerBolla");
				query.setParameter("bfcof", bfcof);
				query.setParameter("bfcau", bfcau);
				query.setParameter("bfdbf", bfdbf);
				query.setParameter("bfnbf", "%" + bfnbf.toUpperCase() + "%");
				o = (List<AsBofor0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}

	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
