package com.exoma.dao;

import com.exoma.model.PerguntaDc;
import com.exoma.util.HibernateUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PerguntaDcDAO {

    public List<PerguntaDc> findAtivas() {
        Session session = null;
        Transaction transaction = null;
        List<PerguntaDc> perguntasAtivas = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from PerguntaDc p where p.flagAtivo = true order by p.id");
//            query.setParameter("ativo", true);
            perguntasAtivas = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar perguntas ativas.", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return perguntasAtivas;
    }




}
