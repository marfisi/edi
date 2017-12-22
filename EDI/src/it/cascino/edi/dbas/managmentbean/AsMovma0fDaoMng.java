package it.cascino.edi.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.edi.dbas.model.AsMovma0f;
import it.cascino.edi.utils.Resources;
import it.cascino.edi.dbas.dao.AsMovma0fDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class AsMovma0fDaoMng implements AsMovma0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsMovma0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsMovma0f> getAll(){
		List<AsMovma0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsMovma0f.findAll");
				o = (List<AsMovma0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
//	public void salva(AsMovma0f a){
//		try{
//			try{
//				utx.begin();
//				// preoice.setId(null);
//				log.info("salva: " + a.toString());
//				em.persist(a);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
//	
//	public void aggiorna(AsMovma0f a){
//		try{
//			try{
//				utx.begin();
//				log.info("aggiorna: " + a.toString());
//				em.merge(a);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
//	
//	public void elimina(AsMovma0f aElimina){
//		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
//		try{
//			try{
//				utx.begin();
//				AsMovma0f a = em.find(AsMovma0f.class, aElimina.getMoa());
//				log.info("elimina: " + a.toString());
//				em.remove(a);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
	
	@SuppressWarnings("unchecked")
	public List<AsMovma0f> getMov(Integer vdatr, String vcaus, Integer vnura, Integer vnumd){
		List<AsMovma0f> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsMovma0f.findMov");
				query.setParameter("vdatr", vdatr);
				query.setParameter("vcaus", vcaus);
				query.setParameter("vnura", vnura);
				query.setParameter("vnumd", vnumd);
				o = (List<AsMovma0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public AsMovma0f getMovConRigo(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog){
		AsMovma0f o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsMovma0f.findMovConRigo");
				query.setParameter("vdatr", vdatr);
				query.setParameter("vcaus", vcaus);
				query.setParameter("vnura", vnura);
				query.setParameter("vnumd", vnumd);
				query.setParameter("vprog", vprog);
				o = (AsMovma0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public AsMovma0f getMovConCodart(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, String vcoda){
		AsMovma0f o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsMovma0f.findMovConCodart");
				query.setParameter("vdatr", vdatr);
				query.setParameter("vcaus", vcaus);
				query.setParameter("vnura", vnura);
				query.setParameter("vnumd", vnumd);
				query.setParameter("vcoda", vcoda);
				o = (AsMovma0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public String updPrezzo(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog, Float vprez){
		return updPrezzoESco(vdatr, vcaus, vnura, vnumd, vprog, vprez, 0.0f, 0.0f, 0.0f);
	}
	
	public String updPrezzoESco(Integer vdatr, String vcaus, Integer vnura, Integer vnumd, Integer vprog, Float vprez, Float vsco1, Float vsco2, Float vsco3){
		Integer o = -1;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsMovma0f.updPrezzoESco");
				query.setParameter("vdatr", vdatr);
				query.setParameter("vcaus", vcaus);
				query.setParameter("vnura", vnura);
				query.setParameter("vnumd", vnumd);
				query.setParameter("vprog", vprog);
				query.setParameter("vprez", vprez);
				query.setParameter("vsco1", vsco1);
				query.setParameter("vsco2", vsco2);
				query.setParameter("vsco3", vsco3);
				o = query.executeUpdate();
			}catch(NoResultException e){
				o = -1;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o.toString();
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
