package com.exoma.dao;

import com.exoma.pojo.participante;
import com.exoma.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParticipanteDAO {

    public participante findParticipanteByCpf(String cpf) {
        Session session = null;
        Transaction transaction = null;
        participante participante = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from Participante where cpf = :cpf");
            query.setParameter("cpf", cpf);
            participante = (com.exoma.pojo.participante) query.uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return participante;
    }
}
