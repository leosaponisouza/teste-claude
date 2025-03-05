package com.exoma.dao;

import com.exoma.model.RespostaDc;
import com.exoma.util.HibernateUtil;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RespostaDcDAO {

    public void saveOrUpdate(Set<RespostaDc> respostas) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            for (RespostaDc resposta : respostas) {
                session.saveOrUpdate(resposta);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar respostas.", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<RespostaDc> findByParticipanteCpf(long cpf) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM RespostaDc r JOIN FETCH r.pergunta WHERE r.participante.cpf = :cpf";
            Query query = session.createQuery(hql);
            query.setParameter("cpf", cpf);
            List<RespostaDc> results = query.list();
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar respostas.", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return Collections.emptyList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public RespostaDc findByParticipanteCpfAndPerguntaId(long cpf, int perguntaId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM RespostaDc r WHERE r.participante.cpf = :cpf AND r.pergunta.id = :perguntaId";
            Query query = session.createQuery(hql);
            query.setParameter("cpf", cpf);
            query.setParameter("perguntaId", perguntaId);
            RespostaDc respostaDc = (RespostaDc) query.uniqueResult();
            return respostaDc;
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao buscar resposta.", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
