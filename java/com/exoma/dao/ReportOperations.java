package com.exoma.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.exoma.pojo.E_amostra;
import com.exoma.util.HibernateUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ReportOperations {

	private static Transaction transObj;
	private static Session sessionObj = HibernateUtil.getSessionFactory().openSession();
	private FacesContext facesContext = FacesContext.getCurrentInstance();

	public void feedback(String message, boolean touser) {
		if (message != null) {
			if (touser) {
				System.out.println(message);
				FacesMessage facesMessage = new FacesMessage(message);
				facesContext.addMessage("selectRecord:selectRecordmessage", facesMessage);
			}
		}
	}

	public List<String> getdistinctvalues(String table, String tablecolumn) {
		if (tablecolumn == null)
			return null;
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createSQLQuery(
					"select distinct " + tablecolumn + " from " + table + " order by " + tablecolumn + " asc");

			final List<String> result = new LinkedList<>();
			for (final Object o : queryObj.list()) {
				result.add((String) o);
			}
			return (result);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<E_amostra> searchAnE_amostraSet(String conds) {
		List<E_amostra> E_amostraList = new ArrayList<E_amostra>();
		E_amostra record;
		String message = null;
		if (conds == null)
			return null;
		if (!conds.isBlank())
			conds = " where " + conds;
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createSQLQuery(
					"select distinct amop.digby, amop.datad, amop.ideam, amop.file, amop.tecnica, amop.idamo  from" +
							"( (select e.digitalizado_por as digby, e.data as datad, e.ideam, e.file, e.tecnica, e.idamo, a.idpro,"
							+
							" i.cidade, i.estado, i.nascimento, i.sexo, a.lote from participante as i " +
							"natural inner join (amostra as a natural inner join e_amostra as e )" +
							") as amo natural inner join projeto ) as amop natural inner join diagnostico" + conds +
							" order by ideam");
			List results = queryObj.list();
			for (ListIterator iter = results.listIterator(); iter.hasNext();) {
				Object[] row = (Object[]) iter.next();
				// System.out.println(row[0]+" "+row[1]+" "+row[2]+" "+row[4]);
				record = new E_amostra();
				record.setDigitalizado_por((String) row[0]);
				record.setData((Date) row[1]);
				record.setIdeam((Integer) row[2]);
				// record.setFile((String)row[3]);
				record.setTecnica((String) row[4]);
				record.setIdamo((Integer) row[5]);
				E_amostraList.add(record);
				record = null;
			}
			if (E_amostraList != null) {
				if (!E_amostraList.isEmpty()) {
					message = "Records was found.";
					// Integer E_amostraId = E_amostraList.get(0).getIdeam();
					// facesContext.getExternalContext().getSessionMap().put("findE_amostraById",
					// E_amostraId);
				} else
					message = "Records was not found in this database.";
				feedback(message, false);
			}
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return E_amostraList;
	}

}
