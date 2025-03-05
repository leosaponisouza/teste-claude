package com.exoma.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import com.exoma.pojo.participante;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import org.postgresql.util.PSQLException;
import com.exoma.util.HibernateUtil;
import com.exoma.pojo.*;

public class DatabaseOperations {

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

	public void addparticipanteInDb(participante participanteObj) {
		String message;

		if (participanteObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from participante where cpf= :participante_id")
						.setLong("participante_id", participanteObj.getCpf());
				participante particularParticipante = (participante) queryObj.uniqueResult();

				if (particularParticipante == null) {
					if (participanteObj.getTermo() != null) {
						sessionObj.save(participanteObj);
						message = "Participante With Número de Prontuário: " + participanteObj.getCpf() + " Created In Database";
						participanteObj.setKeep_idallset(false);
					} else {
						message = "Não é possível adicionar o participante sem um termo de consentimento.";
					}
				} else {
					message = "Participante With Número de Prontuário: " + participanteObj.getCpf() + " already exists.";
				}
				feedback(message, true);
				participanteObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, fulfill the data.";
		}
	}

	// Method To Delete A Particular participante Record From The Database
	public void deleteparticipanteInDb(long delparticipanteId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante where cpf= :participante_id")
					.setParameter("participante_id", delparticipanteId);
			participante particularparticipante = (participante) queryObj.uniqueResult();
			if (particularparticipante != null) {
				sessionObj.delete(particularparticipante);
				message = "Participante Record With Número de Prontuário: " + delparticipanteId
						+ " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findparticipanteById", delparticipanteId);
			} else
				message = "Participante With Número de Prontuário: " + delparticipanteId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular participante Details From The Database
	@SuppressWarnings("unchecked")
	public List<participante> searchAnparticipanteSet(long participanteId) {
		List<participante> participanteList = new ArrayList<participante>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante where str(cpf) like  :participante_id")
					.setParameter("participante_id", "%" + String.valueOf(participanteId) + "%");
			participanteList = (List<participante>) queryObj.list();
			if (participanteList != null) {
				if (!participanteList.isEmpty()) {
					message = "Número de Prontuário: *" + participanteId + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findparticipanteById", participanteId);
				} else
					message = "Número de Prontuário: *" + participanteId + "* was not found in this database.";
				feedback(message, true);
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
		return participanteList;
	}

	public participante searchOneparticipante(long participanteId) {
		participante particularparticipante = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante where cpf= :participante_id")
					.setParameter("participante_id", participanteId);
			particularparticipante = (participante) queryObj.uniqueResult();
			if (particularparticipante != null) {
				message = "Número de Prontuário: " + participanteId + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findparticipanteById", participanteId);
			} else
				message = "Número de Prontuário: " + participanteId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularparticipante;
	}

	// Method To Update Particular participante Details In The Database
	public void updateparticipanteRecord(participante updateparticipanteObj) {
		String message;

		if (updateparticipanteObj.notnull()) {
			long key = updateparticipanteObj.getCpf();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from participante where cpf= :participante_id")
						.setParameter("participante_id", updateparticipanteObj.getCpf()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateparticipanteObj);
					message = "Participante Record With Número de Prontuário: " + updateparticipanteObj.getCpf()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findparticipanteById",
							updateparticipanteObj);

				} else
					message = "Participante With Número de Prontuário: " + key + " doesn't exists.";
				feedback(message, true);
				updateparticipanteObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateparticipanteObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<participante> retrieveAllparticipanteAsList() {
		List<participante> allparticipantes = new ArrayList<participante>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante table order by table.cpf ASC");
			allparticipantes.addAll(queryObj.list());
			// System.out.println("All The participantes Records Are Fetched Successfully
			// From Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allparticipantes;
	}

	// Method To Add New Termo Details In Database
	public void addTermoInDb(Termo termoObj) {
		String message;

		if (termoObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Termo where cpf= :termo_id").setParameter("termo_id",
						termoObj.getCpf());
				Termo particularTermo = (Termo) queryObj.uniqueResult();
				if (particularTermo == null) {
					sessionObj.save(termoObj);
					message = "addTermoRecord:Cpf: " + termoObj.getCpf() +
							" Created In Database";
				} else
					message = "Termo With Cpf: " + termoObj.getCpf() + " already exists.";
				feedback(message, false);
				termoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			termoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Termo Record From The Database
	public void deleteTermoInDb(long delTermoId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo where cpf= :termo_id").setParameter("termo_id", delTermoId);
			com.exoma.pojo.Termo particularTermo = (com.exoma.pojo.Termo) queryObj.uniqueResult();
			// Termo particularTermo = (Termo)sessionObj.load(Termo.class, new
			// Long(delTermoId));
			if (particularTermo != null) {
				sessionObj.delete(particularTermo);
				message = "Termo Record With Cpf: " + delTermoId + " Is Successfully Deleted From Database";
			} else
				message = "Termo With Cpf: " + delTermoId + " doesn't exists.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Termo Details From The Database
	@SuppressWarnings("unchecked")
	public List<Termo> searchAnTermoSet(long termoId) {
		List<Termo> TermoList = new ArrayList<Termo>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo where str(cpf) like  :termo_id").setParameter("termo_id",
					"%" + String.valueOf(termoId) + "%");
			TermoList = (List<Termo>) queryObj.list();
			if (TermoList != null) {
				if (!TermoList.isEmpty())
					message = "Cpf: *" + termoId + "* was found.";
				else
					message = "Cpf: *" + termoId + "* was not found in this database.";
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
		return TermoList;
	}

	public Termo searchOneTermo(long termoId) {
		Termo particularTermo = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo where cpf= :termo_id").setParameter("termo_id", termoId);
			particularTermo = (Termo) queryObj.uniqueResult();
			if (particularTermo != null)
				message = "Termo for Cpf: " + termoId + " was found.";
			else
				message = "Termo for Cpf: " + termoId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularTermo;
	}

	// Method To Update Particular Termo Details In The Database
	public void updateTermoRecord(Termo updateTermoObj) {
		String message;
		if (updateTermoObj.notnull()) {
			long key = updateTermoObj.getCpf();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Termo where cpf= :termo_id")
						.setParameter("termo_id", updateTermoObj.getCpf()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateTermoObj);
					message = "updateTermoRecord: Termo Record With Cpf: " + updateTermoObj.getCpf()
							+ " Is Successfully Updated In Database";
				} else
					message = "updateTermoRecord: Termo With Cpf: " + key + " doesn't exists.";
				feedback(message, false);
				updateTermoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateTermoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<Termo> retrieveAllTermoAsList() {
		List<Termo> allTermos = new ArrayList<Termo>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo table order by table.cpf ASC");
			allTermos.addAll(queryObj.list());
			// System.out.println("All The Termos Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allTermos;
	}

	// Method To Add New Termo_assentimento Details In Database
	public void addTermoAssentimentoInDb(Termo_assentimento termoAssentimentoObj) {
		String message;

		if (termoAssentimentoObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Termo_assentimento where cpf= :termoAssentimento_id")
						.setParameter("termoAssentimento_id", termoAssentimentoObj.getCpf());
				Termo_assentimento particularTermoAssentimento = (Termo_assentimento) queryObj.uniqueResult();
				if (particularTermoAssentimento == null) {
					sessionObj.save(termoAssentimentoObj);
					message = "addTermoAssentimentoRecord: Cpf: " + termoAssentimentoObj.getCpf()
							+ " Created In Database";
				} else
					message = "Termo_assentimento With Cpf: " + termoAssentimentoObj.getCpf() + " already exists.";
				feedback(message, false);
				termoAssentimentoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();

			}
		} else {
			message = "Termo de assentimento opcional";
			feedback(message, true);
			termoAssentimentoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Termo_assentimento Record From The Database
	public void deleteTermoAssentimentoInDb(long delTermoAssentimentoId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo_assentimento where cpf= :termoAssentimento_id")
					.setParameter("termoAssentimento_id", delTermoAssentimentoId);
			Termo_assentimento particularTermoAssentimento = (Termo_assentimento) queryObj.uniqueResult();
			// Termo_assentimento particularTermoAssentimento =
			// (Termo_assentimento)sessionObj.load(Termo_assentimento.class, new
			// Long(delTermoAssentimentoId));
			if (particularTermoAssentimento != null) {
				sessionObj.delete(particularTermoAssentimento);
				message = "Termo_assentimento Record With Cpf: " + delTermoAssentimentoId
						+ " Is Successfully Deleted From Database";
			} else
				message = "Termo_assentimento With Cpf: " + delTermoAssentimentoId + " doesn't exists.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Termo_assentimento Details From The Database
	@SuppressWarnings("unchecked")
	public List<Termo_assentimento> searchAnTermoAssentimentoSet(long termoAssentimentoId) {
		List<Termo_assentimento> termoAssentimentoList = new ArrayList<Termo_assentimento>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo_assentimento where str(cpf) like :termoAssentimento_id")
					.setParameter("termoAssentimento_id", "%" + String.valueOf(termoAssentimentoId) + "%");
			termoAssentimentoList = (List<Termo_assentimento>) queryObj.list();
			if (termoAssentimentoList != null) {
				if (!termoAssentimentoList.isEmpty())
					message = "Cpf: *" + termoAssentimentoId + "* was found.";
				else
					message = "Cpf: *" + termoAssentimentoId + "* was not found in this database.";
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
		return termoAssentimentoList;
	}

	public Termo_assentimento searchOneTermoAssentimento(long termoAssentimentoId) {
		Termo_assentimento particularTermoAssentimento = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo_assentimento where cpf= :termoAssentimento_id")
					.setParameter("termoAssentimento_id", termoAssentimentoId);
			particularTermoAssentimento = (Termo_assentimento) queryObj.uniqueResult();
			if (particularTermoAssentimento != null)
				message = "Termo_assentimento for Cpf: " + termoAssentimentoId + " was found.";
			else
				message = "Termo_assentimento for Cpf: " + termoAssentimentoId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularTermoAssentimento;
	}

	// Method To Update Particular Termo_assentimento Details In The Database
	public void updateTermoAssentimentoRecord(Termo_assentimento updateTermoAssentimentoObj) {
		String message;
		if (updateTermoAssentimentoObj.notnull()) {
			long key = updateTermoAssentimentoObj.getCpf();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Termo_assentimento where cpf= :termoAssentimento_id")
						.setParameter("termoAssentimento_id", updateTermoAssentimentoObj.getCpf()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateTermoAssentimentoObj);
					message = "updateTermoAssentimentoRecord: Termo_assentimento Record With Cpf: "
							+ updateTermoAssentimentoObj.getCpf() + " Is Successfully Updated In Database";
				} else
					message = "updateTermoAssentimentoRecord: Termo_assentimento With Cpf: " + key + " doesn't exist.";
				feedback(message, false);
				updateTermoAssentimentoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, fill in the data.";
			feedback(message, true);
			updateTermoAssentimentoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<Termo_assentimento> retrieveAllTermoAssentimentoAsList() {
		List<Termo_assentimento> allTermoAssentimentos = new ArrayList<Termo_assentimento>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Termo_assentimento table order by table.cpf ASC");
			allTermoAssentimentos.addAll(queryObj.list());
			// System.out.println("All The Termo_assentimentos Records Are Fetched
			// Successfully From Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allTermoAssentimentos;
	}

	// Method To Add New participante_contato Details In Database
	public void addparticipante_contatoInDb(participante_contato indconObj) {
		String message;

		if (indconObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj
						.createQuery("from participante_contato where contato= '" + indconObj.getContato() + "'");
				participante_contato particularparticipante_contato = (participante_contato) queryObj.uniqueResult();
				if (particularparticipante_contato == null) {
					sessionObj.save(indconObj);
					message = "Contato: " + indconObj.getContato() + " Created In Database";
				} else
					message = "Participante with contato: " + indconObj.getContato() + " already exists.";
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular participante_contato Record From The Database
	public void deleteparticipante_contatoInDb(long cpf) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante_contato where cpf = " + cpf);
			@SuppressWarnings("unchecked")
			List<participante_contato> participante_contatos = (List<participante_contato>) queryObj.list();
			if (!participante_contatos.isEmpty()) {
				for (int i = 0; i < participante_contatos.size(); i++) {
					message = "Participante Record With Contato: " + participante_contatos.get(i).getContato()
							+ " Is Successfully Deleted From Database";
					sessionObj.delete(participante_contatos.get(i));
				}
			} else {
				message = "Participante With cpf: " + cpf + " doesn't exists.";
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
	}

	@SuppressWarnings("unchecked")
	public List<participante_contato> searchAnparticipante_contatoSet(long cpf) {
		List<participante_contato> participante_contatoList = new ArrayList<participante_contato>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante_contato where cpf = :indcon_id")
					.setParameter("indcon_id", cpf);
			participante_contatoList = (List<participante_contato>) queryObj.list();
			if (participante_contatoList != null) {
				if (!participante_contatoList.isEmpty())
					message = "Contato: " + cpf + " was found.";
				else
					message = "Contato: " + cpf + " was not found in this database.";
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
		return participante_contatoList;
	}

	// Method To Fetch Particular participante_contato Details From The Database
	@SuppressWarnings("unchecked")
	public List<participante_contato> searchAnparticipante_contatoSet(String indconId) {
		List<participante_contato> participante_contatoList = new ArrayList<participante_contato>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante_contato where str(contato) like  :indcon_id")
					.setParameter("indcon_id", "%" + String.valueOf(indconId) + "%");
			participante_contatoList = (List<participante_contato>) queryObj.list();
			if (participante_contatoList != null) {
				if (!participante_contatoList.isEmpty())
					message = "Contato: *" + indconId + "* was found.";
				else
					message = "Contato: *" + indconId + "* was not found in this database.";
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
		return participante_contatoList;
	}

	public participante_contato searchOneparticipante_contato(String indconId) {
		participante_contato particularparticipante_contato = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from participante_contato where contato= '" + indconId + "'");
			particularparticipante_contato = (participante_contato) queryObj.uniqueResult();
			if (particularparticipante_contato != null)
				message = "Contato: " + indconId + " was found.";
			else
				message = "Contato: " + indconId + " was not found in this database.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularparticipante_contato;
	}

	// Method To Add New Amostra Details In Database
	public void addAmostraInDb(Amostra amostraObj) {
		String message;

		if (amostraObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createQuery("from participante where cpf= :participante_id")
						.setParameter("participante_id", amostraObj.getCpf());
				participante particularparticipante = (participante) queryObj.uniqueResult();
				if (particularparticipante != null) {
					queryObj = sessionObj.createSQLQuery("select nextval('public.amostra_idamo_seq');");
					amostraObj.setIdamo(Integer.valueOf(queryObj.uniqueResult().toString()));
					if (amostraObj.getIdamo() > 0) {
						sessionObj.save(amostraObj);
						message = "addAmostraRecord:IdAmostra: " + amostraObj.getIdamo() + " Created In Database";
						amostraObj.setKeep_idallset(false);
					} else
						message = "Amostra With IdAmostra refused.";
				} else
					message = "CPF not registered!";

				amostraObj.setMessage(message);
				feedback(message, true);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			amostraObj.setMessage(message);
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Amostra Record From The Database
	public void deleteAmostraInDb(Integer delAmostraId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra where idamo= :amostra_id").setParameter("amostra_id",
					delAmostraId);
			Amostra particularAmostra = (Amostra) queryObj.uniqueResult();
			if (particularAmostra != null) {
				sessionObj.delete(particularAmostra);
				message = "Amostra Record With IdAmostra: " + delAmostraId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findAmostraById", delAmostraId);
			} else
				message = "Amostra With IdAmostra: " + delAmostraId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Amostra Details From The Database
	@SuppressWarnings("unchecked")
	public List<Amostra> searchAnAmostraSet(long cpf) {
		List<Amostra> AmostraList = new ArrayList<Amostra>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra where cpf = " + String.valueOf(cpf));
			AmostraList = (List<Amostra>) queryObj.list();
			if (AmostraList != null) {
				if (!AmostraList.isEmpty()) {
					message = "Amostra: with " + cpf + " was found.";
					facesContext.getExternalContext().getSessionMap().put("findAmostraById", cpf);
				} else
					message = "Amostra: with " + cpf + " was not found in this database.";
				feedback(message, true);
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
		return AmostraList;
	}

	public Amostra searchOneAmostra(Integer amostraId) {
		Amostra particularAmostra = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra where idamo= :amostra_id").setParameter("amostra_id",
					amostraId);
			particularAmostra = (Amostra) queryObj.uniqueResult();
			if (particularAmostra != null) {
				message = "IdAmostra: " + amostraId + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findAmostraById", amostraId);
			} else
				message = "IdAmostra: " + amostraId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularAmostra;
	}

	public Acompanhamento_amostra searchOneAcompAmostra(Integer acompanhamentoId) {
		Acompanhamento_amostra particularAcompAmostra = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Acompanhamento_amostra where idacompanhamento= :acompanhamento_id").setParameter("acompanhamento_id",
					acompanhamentoId);
			particularAcompAmostra = (Acompanhamento_amostra) queryObj.uniqueResult();
			if (particularAcompAmostra != null) {
				message = "IdAcompanhamento: " + acompanhamentoId + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findAcompAmostraById", acompanhamentoId);
			} else
				message = "IdAcompanhamento: " + acompanhamentoId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularAcompAmostra;
	}


	// Method To Add New Acompanhamento_amostra Details In Database
	public void addAcompAmostraInDb(Acompanhamento_amostra acompObj) {
		String message;

		if (acompObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createQuery("from Acompanhamento_amostra where idamo= :amostra_id")
						.setParameter("amostra_id", acompObj.getIdamo());
				Acompanhamento_amostra particularamostra = (Acompanhamento_amostra) queryObj.uniqueResult();
				if (particularamostra == null) {
					queryObj = sessionObj.createSQLQuery("select nextval('public.acompanhamento_amostra_idacompanhamento_seq');");
					acompObj.setIdacompanhamento(Integer.valueOf(queryObj.uniqueResult().toString()));
					if (acompObj.getIdacompanhamento() > 0) {
						sessionObj.save(acompObj);
						message = "addAcompAmostraRecord:IdAcompanhamento: " + acompObj.getIdacompanhamento() + " Created In Database";
						acompObj.setKeep_idallset(false);
					} else
						message = "Amostra With " + acompObj.getIdacompanhamento() + "refused.";
				} else
					message = "Acompanhamento para amostra "+ acompObj.getIdamo()+" já existe.";

				acompObj.setMessage(message);
				feedback(message, true);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			acompObj.setMessage(message);
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Update Particular Amostra Details In The Database
	public void updateAcompAmostraRecord(Acompanhamento_amostra updateAcompObj) {
		String message;

		if (updateAcompObj.notnull()) {
			Integer key = updateAcompObj.getIdacompanhamento();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Acompanhamento_amostra where idacompanhamento= :acompanhamento_id")
						.setParameter("acompanhamento_id", updateAcompObj.getIdacompanhamento()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					//updateAcompObj = (Acompanhamento_amostra) sessionObj.merge(updateAcompObj); // Mescla e obtém a versão atualizada
					sessionObj.update(updateAcompObj);
					message = "updateAcompAmostraRecord: Acompanhamento Record With IdAcompanhamento: " + updateAcompObj.getIdacompanhamento()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findAcompAmostraById", updateAcompObj);

				} else
					message = "updateAcompAmostraRecord: Acompanhamento With IdAcompanhamento: " + key + " doesn't exists.";
				feedback(message, true);
				updateAcompObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateAcompObj.setMessage(message);
		}
		sessionObj.clear();
	}


	// Method To Delete A Particular Amostra Record From The Database
	public void deleteAcompAmostraInDb(Integer delAcompAmostraId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Acompanhamento_amostra where idacompanhamento= :acompanhamento_id").setParameter("acompanhamento_id",
					delAcompAmostraId);
			Acompanhamento_amostra particularAcompAmostra = (Acompanhamento_amostra) queryObj.uniqueResult();
			if (particularAcompAmostra != null) {
				sessionObj.delete(particularAcompAmostra);
				message = "Acompanhamento Record With IdAcompanhamento: " + delAcompAmostraId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findAcompAmostraById", delAcompAmostraId);
			} else
				message = "Acompanhamento With IdAcompanhamento: " + delAcompAmostraId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Acompanhamento_amostra Details From The Database
	@SuppressWarnings("unchecked")
	public List<Acompanhamento_amostra> searchAnAcompAmostraSet(int idamo) {
		List<Acompanhamento_amostra> AcompanhamentoList = new ArrayList<Acompanhamento_amostra>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Acompanhamento_amostra where idamo = "+ String.valueOf(idamo));
			AcompanhamentoList = (List<Acompanhamento_amostra>) queryObj.list();
			if (AcompanhamentoList != null) {
				if (!AcompanhamentoList.isEmpty()) {
					message = "Acompanhamento: with " + idamo + " was found.";
					facesContext.getExternalContext().getSessionMap().put("findAcompAmostraById", idamo);
				} else
					message = "Acompanhamento: with " + idamo + " was not found in this database.";
				feedback(message, true);
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
		return AcompanhamentoList;
	}


	// Method To Update Particular Amostra Details In The Database
	public void updateAmostraRecord(Amostra updateAmostraObj) {
		String message;

		if (updateAmostraObj.notnull()) {
			Integer key = updateAmostraObj.getIdamo();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Amostra where idamo= :amostra_id")
						.setParameter("amostra_id", updateAmostraObj.getIdamo()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateAmostraObj);
					message = "updateAmostraRecord: Amostra Record With IdAmostra: " + updateAmostraObj.getIdamo()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findAmostraById", updateAmostraObj);

				} else
					message = "updateAmostraRecord: Amostra With IdAmostra: " + key + " doesn't exists.";
				feedback(message, true);
				updateAmostraObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateAmostraObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<Amostra> retrieveAllAmostraAsList() {
		List<Amostra> allAmostras = new ArrayList<Amostra>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra table order by table.idamo ASC");
			allAmostras.addAll(queryObj.list());
			// System.out.println("All The Amostras Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allAmostras;
	}

	@SuppressWarnings("unchecked")
	public List<Acompanhamento_amostra> retrieveAllAcompAmostraAsList() {
		List<Acompanhamento_amostra> allAcompAmostras = new ArrayList<Acompanhamento_amostra>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Acompanhamento_amostra table order by table.idacompanhamento ASC");
			allAcompAmostras.addAll(queryObj.list());
			// System.out.println("All The Amostras Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allAcompAmostras;
	}

	// Method To Add New Amostra_sintoma Details In Database
	public void addAmostra_sintomaInDb(Amostra_sintoma amosinObj) {
		String message;

		if (amosinObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Amostra_sintoma where idamo= " + amosinObj.getIdamo()
						+ " and sintoma ='" + amosinObj.getSintoma() + "'");
				Amostra_sintoma particularAmostra_sintoma = (Amostra_sintoma) queryObj.uniqueResult();
				if (particularAmostra_sintoma == null) {
					sessionObj.save(amosinObj);
					message = "addAmostra_sintomaRecord:Idamo: " + amosinObj.getIdamo() + " Created In Database";
				} else
					message = "Amostra_sintoma With sintoma=" + amosinObj.getSintoma() + " and Idamo="
							+ amosinObj.getIdamo() + " already exists.";
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Amostra_sintoma Record From The Database
	public void deleteAmostra_sintomaInDb(Integer idamo) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_sintoma where idamo = " + idamo);
			@SuppressWarnings("unchecked")
			List<Amostra_sintoma> Amostra_sintomas = (List<Amostra_sintoma>) queryObj.list();
			if (!Amostra_sintomas.isEmpty()) {
				for (int i = 0; i < Amostra_sintomas.size(); i++) {
					message = "Amostra_sintoma Record With Idamo: " + Amostra_sintomas.get(i).getIdamo()
							+ " Is Successfully Deleted From Database";
					sessionObj.delete(Amostra_sintomas.get(i));
				}
			} else {
				message = "Amostra_sintoma With idamo: " + idamo + " doesn't exists.";
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
	}

	// Method To Fetch Particular Amostra_sintoma Details From The Database
	@SuppressWarnings("unchecked")
	public List<Amostra_sintoma> searchAmostra_sintomasbyId(Integer idamo) {
		List<Amostra_sintoma> Amostra_sintomaList = new ArrayList<Amostra_sintoma>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_sintoma where idamo = " + idamo);
			Amostra_sintomaList = (List<Amostra_sintoma>) queryObj.list();
			if (Amostra_sintomaList != null) {
				if (!Amostra_sintomaList.isEmpty())
					message = "Amostra_sintomas: " + idamo + " was found.";
				else
					message = "Amostra_sintomas: " + idamo + " was not found in this database.";
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
		return Amostra_sintomaList;
	}

	// Method To Add New E_amostra Details In Database
	public void addE_amostraInDb(E_amostra E_amostraObj) {
		String message;

		if (E_amostraObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Amostra where idamo=" + E_amostraObj.getIdamo());
				Amostra particularAmostra = (Amostra) queryObj.uniqueResult();
				if (particularAmostra != null) {
					queryObj = sessionObj.createSQLQuery("select nextval('public.e_amostra_ideam_seq');");
					E_amostraObj.setIdeam(Integer.valueOf(queryObj.uniqueResult().toString()));
					if (E_amostraObj.getIdeam() > 0) {
						sessionObj.save(E_amostraObj);
						message = "addE_amostraRecord:Ideam: " + E_amostraObj.getIdeam() + " Created In Database";
						E_amostraObj.setMessage(message);
					} else {
						message = "E_amostra With Ideam: " + E_amostraObj.getIdeam() + " refused.";
						E_amostraObj.setMessage(message);
					}
				} else
					message = "Amostra " + E_amostraObj.getIdamo() + " not registered.";

				E_amostraObj.setMessage(message);
				feedback(message, true);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			E_amostraObj.setMessage(message);
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular E_amostra Record From The Database
	public void deleteE_amostraInDb(Integer delE_amostraId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_amostra where ideam= :E_amostra_id").setParameter("E_amostra_id",
					delE_amostraId);
			E_amostra particularE_amostra = (E_amostra) queryObj.uniqueResult();
			if (particularE_amostra != null) {
				sessionObj.delete(particularE_amostra);
				message = "E_amostra Record With Ideam: " + delE_amostraId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findE_amostraById", delE_amostraId);
			} else
				message = "E_amostra With Ideam: " + delE_amostraId + " doesn't exists.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular E_amostra Details From The Database
	@SuppressWarnings("unchecked")
	public List<E_amostra> searchAnE_amostraSet(Integer AmostraId) {
		List<E_amostra> E_amostraList = new ArrayList<E_amostra>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_amostra where idamo = " + String.valueOf(AmostraId));
			E_amostraList = (List<E_amostra>) queryObj.list();
			if (E_amostraList != null) {
				if (!E_amostraList.isEmpty()) {
					message = "E_amostras with idamo:" + AmostraId + " was found.";
					facesContext.getExternalContext().getSessionMap().put("findE_amostraById", AmostraId);
				} else
					message = "E_amostras with idamo:" + AmostraId + " was not found in this database.";
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

	@SuppressWarnings("unchecked")
	public List<E_amostra> searchE_amostrasbyIdAmostra(Integer idamo) {
		List<E_amostra> E_amostraList = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_amostra where idamo = " + idamo);
			E_amostraList = (List<E_amostra>) queryObj.list();
			if (E_amostraList != null) {
				if (!E_amostraList.isEmpty()) {
					message = "Idamo: *" + idamo + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findE_amostrasByIdAmostra", idamo);
				} else {
					message = "Idamo: *" + idamo + "* was not found in this database.";
				}
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

	public E_amostra searchOneE_amostra(Integer ideam) {
		E_amostra particularE_amostra = null;
		String message;
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_amostra where ideam= :E_amostra_id").setParameter("E_amostra_id",
					ideam);
			particularE_amostra = (E_amostra) queryObj.uniqueResult();
			if (particularE_amostra != null) {
				message = "E_amostra for Ideam: " + ideam + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findE_amostraById", ideam);
			} else
				message = "E_amostra for Ideam: " + ideam + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularE_amostra;
	}

	// Method To Update Particular E_amostra Details In The Database
	public void updateE_amostraRecord(E_amostra updateE_amostraObj) {
		String message;
		if (updateE_amostraObj.notnull()) {
			Integer key = updateE_amostraObj.getIdeam();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from E_amostra where ideam= :E_amostra_id")
						.setParameter("E_amostra_id", updateE_amostraObj.getIdeam()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateE_amostraObj);
					message = "updateE_amostraRecord: E_amostra Record With Ideam: " + updateE_amostraObj.getIdeam()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findE_amostraById", updateE_amostraObj);
				} else
					message = "updateE_amostraRecord: E_amostra With Ideam: " + key + " doesn't exists.";
				feedback(message, false);
				updateE_amostraObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateE_amostraObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<E_amostra> retrieveAllE_amostraAsList() {
		List<E_amostra> allE_amostras = new ArrayList<E_amostra>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_amostra table order by table.ideam ASC");
			allE_amostras.addAll(queryObj.list());
			// System.out.println("All The E_amostras Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allE_amostras;
	}

	// Method To Add New Amostra_medicamento Details In Database
	public void addAmostra_medicamentoInDb(Amostra_medicamento amosinObj) {
		String message;

		if (amosinObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Amostra_medicamento where idamo= " + amosinObj.getIdamo()
						+ " and medicamento ='" + amosinObj.getMedicamento() + "'");
				Amostra_medicamento particularAmostra_medicamento = (Amostra_medicamento) queryObj.uniqueResult();
				if (particularAmostra_medicamento == null) {
					sessionObj.save(amosinObj);
					message = "addAmostra_medicamentoRecord:Idamo: " + amosinObj.getIdamo() + " Created In Database";
				} else
					message = "Amostra_medicamento With medicamento=" + amosinObj.getMedicamento() + " and Idamo="
							+ amosinObj.getIdamo() + " already exists.";
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Amostra_medicamento Record From The Database
	public void deleteAmostra_medicamentoInDb(Integer idamo) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_medicamento where idamo = " + idamo);
			@SuppressWarnings("unchecked")
			List<Amostra_medicamento> Amostra_medicamentos = (List<Amostra_medicamento>) queryObj.list();
			if (!Amostra_medicamentos.isEmpty()) {
				for (int i = 0; i < Amostra_medicamentos.size(); i++) {
					message = "Amostra_medicamento Record With Idamo: " + Amostra_medicamentos.get(i).getIdamo()
							+ " Is Successfully Deleted From Database";
					sessionObj.delete(Amostra_medicamentos.get(i));
				}
			} else {
				message = "Amostra_medicamento With idamo: " + idamo + " doesn't exists.";
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
	}

	// Method To Fetch Particular Amostra_medicamento Details From The Database
	@SuppressWarnings("unchecked")
	public List<Amostra_medicamento> searchAmostra_medicamentosbyId(Integer idamo) {
		List<Amostra_medicamento> Amostra_medicamentoList = new ArrayList<Amostra_medicamento>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_medicamento where idamo = " + idamo);
			Amostra_medicamentoList = (List<Amostra_medicamento>) queryObj.list();
			if (Amostra_medicamentoList != null) {
				if (!Amostra_medicamentoList.isEmpty())
					message = "Amostra_medicamentos: " + idamo + " was found.";
				else
					message = "Amostra_medicamentos: " + idamo + " was not found in this database.";
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
		return Amostra_medicamentoList;
	}

	// Method To Add New Amostra_armazenagem Details In Database
	public void addAmostra_armazenagemInDb(Amostra_armazenagem amosinObj) {
		String message;

		if (amosinObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();

				Query queryObj = sessionObj.createQuery("from Amostra_armazenagem where idamo= " + amosinObj.getIdamo()
						+ " and armazenagem ='" + amosinObj.getArmazenagem() + "'");
				Amostra_armazenagem particularAmostra_armazenagem = (Amostra_armazenagem) queryObj.uniqueResult();
				if (particularAmostra_armazenagem == null) {
					sessionObj.save(amosinObj);
					message = "addAmostra_armazenagemRecord:Idamo: " + amosinObj.getIdamo() + " Created In Database";
				} else
					message = "Amostra_armazenagem With armazenagem=" + amosinObj.getArmazenagem() + " and Idamo="
							+ amosinObj.getIdamo() + " already exists.";
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Amostra_armazenagem Record From The Database
	public void deleteAmostra_armazenagemInDb(Integer idamo) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_armazenagem where idamo = " + idamo);
			@SuppressWarnings("unchecked")
			List<Amostra_armazenagem> Amostra_armazenagems = (List<Amostra_armazenagem>) queryObj.list();
			if (!Amostra_armazenagems.isEmpty()) {
				for (int i = 0; i < Amostra_armazenagems.size(); i++) {
					message = "Amostra_armazenagem Record With Idamo: " + Amostra_armazenagems.get(i).getIdamo()
							+ " Is Successfully Deleted From Database";
					sessionObj.delete(Amostra_armazenagems.get(i));
				}
			} else {
				message = "Amostra_armazenagem With idamo: " + idamo + " doesn't exists.";
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
	}

	// Method To Fetch Particular Amostra_armazenagem Details From The Database
	@SuppressWarnings("unchecked")
	public List<Amostra_armazenagem> searchAmostra_armazenagemsbyId(Integer idamo) {
		List<Amostra_armazenagem> Amostra_armazenagemList = new ArrayList<Amostra_armazenagem>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Amostra_armazenagem where idamo = " + idamo);
			Amostra_armazenagemList = (List<Amostra_armazenagem>) queryObj.list();
			if (Amostra_armazenagemList != null) {
				if (!Amostra_armazenagemList.isEmpty())
					message = "Amostra_armazenagems: " + idamo + " was found.";
				else
					message = "Amostra_armazenagems: " + idamo + " was not found in this database.";
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
		return Amostra_armazenagemList;
	}

	// Method To Add New Projeto Details In Database
	public void addProjetoInDb(Projeto projetoObj) {
		String message;

		if (projetoObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createSQLQuery("select nextval('public.projeto_idpro_seq');");
				projetoObj.setIdpro(Integer.valueOf(queryObj.uniqueResult().toString()));
				if (projetoObj.getIdpro() > 0) {
					sessionObj.save(projetoObj);
					message = "addProjetoRecord:IdProjeto: " + projetoObj.getIdpro() + " Created In Database";
					projetoObj.setKeep_idallset(false);
				} else
					message = "Projeto With IdProjeto refused.";
				feedback(message, true);
				projetoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			projetoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Projeto Record From The Database
	public void deleteProjetoInDb(Integer delProjetoId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Projeto where idpro= :projeto_id").setParameter("projeto_id",
					delProjetoId);
			Projeto particularProjeto = (Projeto) queryObj.uniqueResult();
			if (particularProjeto != null) {
				sessionObj.delete(particularProjeto);
				message = "Projeto Record With IdProjeto: " + delProjetoId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findProjetoById", delProjetoId);
			} else
				message = "Projeto With IdProjeto: " + delProjetoId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Projeto Details From The Database
	@SuppressWarnings("unchecked")
	public List<Projeto> searchAnProjetoSet(String responsavel) {
		List<Projeto> ProjetoList = new ArrayList<Projeto>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Projeto where responsavel like  :projeto_id")
					.setParameter("projeto_id", "%" + String.valueOf(responsavel) + "%");
			ProjetoList = (List<Projeto>) queryObj.list();
			if (ProjetoList != null) {
				if (!ProjetoList.isEmpty()) {
					message = "IdProjeto: from *" + responsavel + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findProjetoById", responsavel);
				} else
					message = "IdProjeto: from *" + responsavel + "* was not found in this database.";
				feedback(message, true);
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
		return ProjetoList;
	}

	public Projeto searchOneProjeto(Integer projetoId) {
		Projeto particularProjeto = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Projeto where idpro= :projeto_id").setParameter("projeto_id",
					projetoId);
			particularProjeto = (Projeto) queryObj.uniqueResult();
			if (particularProjeto != null) {
				message = "IdProjeto: " + projetoId + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findProjetoById", projetoId);
			} else
				message = "IdProjeto: " + projetoId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularProjeto;
	}

	// Method To Update Particular Projeto Details In The Database
	public void updateProjetoRecord(Projeto updateProjetoObj) {
		String message;

		if (updateProjetoObj.notnull()) {
			Integer key = updateProjetoObj.getIdpro();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Projeto where idpro= :projeto_id")
						.setParameter("projeto_id", updateProjetoObj.getIdpro()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateProjetoObj);
					message = "updateProjetoRecord: Projeto Record With IdProjeto: " + updateProjetoObj.getIdpro()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findProjetoById", updateProjetoObj);

				} else
					message = "updateProjetoRecord: Projeto With IdProjeto: " + key + " doesn't exists.";
				feedback(message, true);
				updateProjetoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateProjetoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<Projeto> retrieveAllProjetoAsList() {
		List<Projeto> allProjetos = new ArrayList<Projeto>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Projeto table order by table.idpro ASC");
			allProjetos.addAll(queryObj.list());
			// System.out.println("All The Projetos Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allProjetos;
	}

	// Method To Update Particular Diagnostico Details In The Database
	public void updateDiagnosticoRecord(Diagnostico updateDiagnosticoObj) {
		String message;

		if (updateDiagnosticoObj.notnull()) {
			Integer key = updateDiagnosticoObj.getIddia();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Diagnostico where iddia= :diagnostico_id")
						.setParameter("diagnostico_id", updateDiagnosticoObj.getIddia()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateDiagnosticoObj);
					message = "updateDiagnosticoRecord: Diagnostico Record With IdDiagnostico: "
							+ updateDiagnosticoObj.getIddia() + " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findDiagnosticoById", updateDiagnosticoObj);

				} else
					message = "updateDiagnosticoRecord: Diagnostico With IdDiagnostico: " + key + " doesn't exists.";
				feedback(message, true);
				updateDiagnosticoObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateDiagnosticoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Add New Diagnostico Details In Database
	public void addDiagnosticoInDb(Diagnostico DiagnosticoObj) {
		String message;

		if (DiagnosticoObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createQuery("from Amostra where idamo= " + DiagnosticoObj.getIdamo());
				Amostra particularAmostra = (Amostra) queryObj.uniqueResult();
				if (particularAmostra != null) {
					queryObj = sessionObj.createQuery("from Diagnostico where idamo= " + DiagnosticoObj.getIdamo()
							+ " and idexa=" + DiagnosticoObj.getIdexa());
					Diagnostico particularDiagnostico = (Diagnostico) queryObj.uniqueResult();
					if (particularDiagnostico == null) {
						queryObj = sessionObj.createSQLQuery("select nextval('public.diagnostico_iddia_seq');");
						DiagnosticoObj.setIddia(Integer.valueOf(queryObj.uniqueResult().toString()));
						if (DiagnosticoObj.getIddia() > 0) {
							sessionObj.save(DiagnosticoObj);
							message = "addDiagnosticoRecord:Iddia: " + DiagnosticoObj.getIddia()
									+ " Created In Database";
						} else {
							message = "Diagnostico With Iddia: " + DiagnosticoObj.getIddia() + " refused.";
						}
					} else
						message = "Amostra " + DiagnosticoObj.getIdamo() + " and Exame " + DiagnosticoObj.getIdexa()
								+ " already registered!";
				} else
					message = "Amostra " + DiagnosticoObj.getIdamo() + " not registered.";

				DiagnosticoObj.setMessage(message);
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			DiagnosticoObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Diagnostico Record From The Database
	public void deleteDiagnosticoInDb(Integer delDiagnosticoId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Diagnostico where iddia= :Diagnostico_id")
					.setParameter("Diagnostico_id", delDiagnosticoId);
			Diagnostico particularDiagnostico = (Diagnostico) queryObj.uniqueResult();
			if (particularDiagnostico != null) {
				sessionObj.delete(particularDiagnostico);
				message = "Diagnostico Record With Iddia: " + delDiagnosticoId
						+ " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findDiagnosticoById", delDiagnosticoId);
			} else
				message = "Diagnostico With Iddia: " + delDiagnosticoId + " doesn't exists.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Diagnostico Details From The Database
	@SuppressWarnings("unchecked")
	public List<Diagnostico> searchAnDiagnosticoSet(Integer DiagnosticoId) {
		List<Diagnostico> DiagnosticoList = new ArrayList<Diagnostico>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Diagnostico where str(iddia) like  :Diagnostico_id")
					.setParameter("Diagnostico_id", "%" + String.valueOf(DiagnosticoId) + "%");
			DiagnosticoList = (List<Diagnostico>) queryObj.list();
			if (DiagnosticoList != null) {
				if (!DiagnosticoList.isEmpty()) {
					message = "Iddia: *" + DiagnosticoId + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findDiagnosticoById", DiagnosticoId);
				} else
					message = "Iddia: *" + DiagnosticoId + "* was not found in this database.";
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
		return DiagnosticoList;
	}

	@SuppressWarnings("unchecked")
	public List<Diagnostico> searchDiagnosticosbyIdAmostra(Integer idamo, String map) {
		List<Diagnostico> DiagnosticoList = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery(
					"from Diagnostico table where idamo = " + idamo + " order by table.data_conclusao DESC");
			DiagnosticoList = (List<Diagnostico>) queryObj.list();
			if (DiagnosticoList != null) {
				if (!DiagnosticoList.isEmpty()) {
					message = "Idamo: *" + idamo + "* was found.";
					facesContext.getExternalContext().getSessionMap().put(map, idamo);
				} else {
					message = "Idamo: *" + idamo + "* was not found in this database.";
				}
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
		return DiagnosticoList;
	}

	public Diagnostico searchOneDiagnosticobyId(Integer iddia) {
		Diagnostico OneDiagnostico = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Diagnostico where iddia = " + iddia);
			OneDiagnostico = (Diagnostico) queryObj.uniqueResult();
			if (OneDiagnostico != null) {
				message = "Iddia: " + iddia + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findDiagnosticoById", iddia);
			} else {
				message = "Iddia: " + iddia + " was not found in this database.";
			}
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return OneDiagnostico;
	}

	@SuppressWarnings("unchecked")
	public List<Diagnostico> retrieveAllDiagnosticoAsList() {
		List<Diagnostico> allDiagnosticos = new ArrayList<Diagnostico>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Diagnostico table order by table.iddia ASC");
			allDiagnosticos.addAll(queryObj.list());
			// System.out.println("All The Diagnosticos Records Are Fetched Successfully
			// From Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allDiagnosticos;
	}

	// Method To Update Particular Exame Details In The Database
	public void updateExameRecord(Exame updateExameObj) {
		String message;

		if (updateExameObj.notnull()) {
			Integer key = updateExameObj.getIdexa();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Exame where idexa= :exame_id")
						.setParameter("exame_id", updateExameObj.getIdexa()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateExameObj);
					message = "updateExameRecord: Exame Record With IdExame: " + updateExameObj.getIdexa()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findExameById", updateExameObj);

				} else
					message = "updateExameRecord: Exame With IdExame: " + key + " doesn't exists.";
				feedback(message, true);
				updateExameObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateExameObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Add New Exame Details In Database
	public void addExameInDb(Exame ExameObj) {
		String message;

		if (ExameObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createSQLQuery("select nextval('public.exame_idexa_seq');");
				ExameObj.setIdexa(Integer.valueOf(queryObj.uniqueResult().toString()));
				if (ExameObj.getIdexa() > 0) {
					sessionObj.save(ExameObj);
					message = "addExameRecord:Idexa: " + ExameObj.getIdexa() + " Created In Database";
				} else {
					message = "Exame With Idexa: " + ExameObj.getIdexa() + " refused.";
				}
				feedback(message, true);
				ExameObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			ExameObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Exame Record From The Database
	public void deleteExameInDb(Integer delExameId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Exame where idexa= :Exame_id").setParameter("Exame_id",
					delExameId);
			Exame particularExame = (Exame) queryObj.uniqueResult();
			if (particularExame != null) {
				sessionObj.delete(particularExame);
				message = "Exame Record With Idexa: " + delExameId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findExameById", delExameId);
			} else
				message = "Exame With Idexa: " + delExameId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Exame Details From The Database
	@SuppressWarnings("unchecked")
	public List<Exame> searchAnExameSet(String ExameId) {
		List<Exame> ExameList = new ArrayList<Exame>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Exame where metodo like  :Exame_id").setParameter("Exame_id",
					"%" + ExameId + "%");
			ExameList = (List<Exame>) queryObj.list();
			if (ExameList != null) {
				if (!ExameList.isEmpty()) {
					message = "Metodo: *" + ExameId + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findExameById", ExameId);
				} else
					message = "Metodo: *" + ExameId + "* was not found in this database.";
				feedback(message, true);
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
		return ExameList;
	}

	// Method To Fetch Particular Exame Details From The Database
	@SuppressWarnings("unchecked")
	public List<Exame> retrieveAllExameAsList(Integer ambiente) {
		List<Exame> ExameList = new ArrayList<Exame>();
		String message = null;
		String cond = "";
		if (ambiente.intValue() > 0)
			cond = cond + " where ambiente = " + ambiente;
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Exame " + cond + " order by idexa ASC");
			ExameList = (List<Exame>) queryObj.list();
			if (ExameList != null) {
				if (!ExameList.isEmpty()) {
					message = "Exames was found.";
				} else
					message = "Exames was not found in this database.";
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
		return ExameList;
	}

	@SuppressWarnings("unchecked")
	public List<Exame> searchExamesbyId(Integer idexa) {
		List<Exame> ExameList = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Exame table where idexa = " + idexa);
			ExameList = (List<Exame>) queryObj.list();
			if (ExameList != null) {
				if (!ExameList.isEmpty()) {
					message = "Idexa: *" + idexa + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findExameById", idexa);
				} else {
					message = "Idexa: *" + idexa + "* was not found in this database.";
				}
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
		return ExameList;
	}

	// Method To Add New Usuario Details In Database
	public void addUsuarioInDb(Usuario usuarioObj) {
		String message;

		if (usuarioObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createSQLQuery("select nextval('public.usuario_idusu_seq')");
				usuarioObj.setIdusu(Integer.valueOf(queryObj.uniqueResult().toString()));
				if (usuarioObj.getIdusu() > 0) {
					sessionObj.save(usuarioObj);
					message = "addUsuarioRecord:IdUsuario: " + usuarioObj.getIdusu() + " Created In Database";
					usuarioObj.setKeep_idallset(false);
				} else
					message = "Usuario With IdUsuario refused.";
				feedback(message, true);
				usuarioObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			usuarioObj.setMessage(message);
		}
		sessionObj.clear();
	}

	// Method To Delete A Particular Usuario Record From The Database
	public void deleteUsuarioInDb(Integer delUsuarioId) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Usuario where idusu= :usuario_id").setParameter("usuario_id",
					delUsuarioId);
			Usuario particularUsuario = (Usuario) queryObj.uniqueResult();
			if (particularUsuario != null) {
				sessionObj.delete(particularUsuario);
				message = "Usuario Record With IdUsuario: " + delUsuarioId + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findUsuarioById", delUsuarioId);
			} else
				message = "Usuario With IdUsuario: " + delUsuarioId + " doesn't exists.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular Usuario Details From The Database
	@SuppressWarnings("unchecked")
	public List<Usuario> searchAnUsuarioSet(Integer ativo) {
		List<Usuario> UsuarioList = new ArrayList<Usuario>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Usuario where ativo = " + String.valueOf(ativo));
			UsuarioList = (List<Usuario>) queryObj.list();
			if (UsuarioList != null) {
				if (!UsuarioList.isEmpty()) {
					message = "Usuario: with ativo = " + ativo + " was found.";
					facesContext.getExternalContext().getSessionMap().put("findUsuarioById", ativo);
				} else
					message = "Usuario: with ativo = " + ativo + " was not found in this database.";
				feedback(message, true);
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
		return UsuarioList;
	}

	public Usuario searchOneUsuario(Integer usuarioId) {
		Usuario particularUsuario = null;
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Usuario where idusu= :usuario_id").setParameter("usuario_id",
					usuarioId);
			particularUsuario = (Usuario) queryObj.uniqueResult();
			if (particularUsuario != null) {
				message = "IdUsuario: " + usuarioId + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findUsuarioById", usuarioId);
			} else
				message = "IdUsuario: " + usuarioId + " was not found in this database.";
			feedback(message, true);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return particularUsuario;
	}

	public Usuario searchOneUsuario(String email) {
		Usuario particularUsuario = null;
		String message;
		if (email != null) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createQuery("from Usuario where email = '" + email + "'");
				particularUsuario = (Usuario) queryObj.uniqueResult();
				if (particularUsuario != null) {
					message = "Usuario: " + email + " was found.";
					facesContext.getExternalContext().getSessionMap().put("findUsuarioById", email);
				} else
					message = "Usuario: " + email + " was not found in this database.";
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
			sessionObj.clear();
		}
		return particularUsuario;
	}

	public Integer searchAccessUsuario(Integer user, Integer access) {
		Integer perm = null;
		if (user != null && access != null) {
			String message;
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj.createSQLQuery("select idper from funcao where idusu=" + user.toString()
						+ " and idper= " + access.toString());
				if (queryObj.uniqueResult() != null) {
					perm = Integer.valueOf(queryObj.uniqueResult().toString());
					if (perm != null) {
						message = "Access:" + access + " was found for user:" + user.toString();
					} else
						message = "Access:" + access + " was not found for user:" + user.toString();
				} else
					message = "Access:" + access + " was not found for user:" + user.toString();
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
			sessionObj.clear();
		}
		return perm;
	}

	// Method To Update Particular Usuario Details In The Database
	public void updateUsuarioRecord(Usuario updateUsuarioObj) {
		String message;

		if (updateUsuarioObj.notnull()) {
			Integer key = updateUsuarioObj.getIdusu();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj.createQuery("from Usuario where idusu= :usuario_id")
						.setParameter("usuario_id", updateUsuarioObj.getIdusu()).uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateUsuarioObj);
					message = "updateUsuarioRecord: Usuario Record With IdUsuario: " + updateUsuarioObj.getIdusu()
							+ " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findUsuarioById", updateUsuarioObj);

				} else
					message = "updateUsuarioRecord: Usuario With IdUsuario: " + key + " doesn't exists.";
				feedback(message, true);
				updateUsuarioObj.setMessage(message);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
			updateUsuarioObj.setMessage(message);
		}
		sessionObj.clear();
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> retrieveAllUsuarioAsList() {
		List<Usuario> allUsuarios = new ArrayList<Usuario>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Usuario table order by table.idusu ASC");
			allUsuarios.addAll(queryObj.list());
			// System.out.println("All The Usuarios Records Are Fetched Successfully From
			// Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allUsuarios;
	}

	public List<Permissao> searchPermissaobyIdUser(Integer userid) {
		List<Permissao> allPermissao = new ArrayList<Permissao>();
		if (userid != null) {
			if (userid.intValue() > 0) {
				try {
					transObj = sessionObj.beginTransaction();
					Query queryObj = sessionObj.createSQLQuery("select p.idper, p.descricao from permissao as p" +
							" natural inner join funcao as f where f.idusu = " + userid.toString());

					List results = queryObj.list();
					for (ListIterator iter = results.listIterator(); iter.hasNext();) {
						Object[] row = (Object[]) iter.next();
						// System.out.println(row[0]+" "+row[1]);
						Permissao record = new Permissao();
						record.setIdper((Integer) row[0]);
						record.setDescricao((String) row[1]);
						allPermissao.add(record);
						record = null;
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
			}
		}
		return allPermissao;
	}

	public void addPermissaoInDb(List<Integer> permissoes, Integer userid) {
		if (permissoes != null && userid != null) {
			if (userid.intValue() > 0) {
				try {
					transObj = sessionObj.beginTransaction();
					Query queryObj = null;
					queryObj = sessionObj.createSQLQuery("delete from funcao where idusu = " + userid);
					queryObj.executeUpdate();
					for (int i = 0; i < permissoes.size(); i++) {
						queryObj = sessionObj.createSQLQuery(
								"insert into funcao(idusu,idper) values(" + userid + "," + permissoes.get(i) + ")");
						queryObj.executeUpdate();
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
			}
		}
	}

	public List<Permissao> retrieveAllPermissaoAsList() {
		List<Permissao> allPermissao = new ArrayList<Permissao>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from Permissao table order by table.idper ASC");
			allPermissao.addAll(queryObj.list());
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allPermissao;
	}

	// Method To Update Particular E_diagnostico Details In The Database
	public String updateE_diagnosticoRecord(E_diagnostico updateE_diagnosticoObj) {
		String message = null;

		if (updateE_diagnosticoObj.notnull()) {
			Integer key = updateE_diagnosticoObj.getIdeam();
			try {
				transObj = sessionObj.beginTransaction();
				Object search = sessionObj
						.createQuery("from E_diagnostico where ideam = " + updateE_diagnosticoObj.getIdeam() +
								" and idexa = " + updateE_diagnosticoObj.getIdexa())
						.uniqueResult();
				if (search != null) {
					sessionObj.evict(search);
					sessionObj.update(updateE_diagnosticoObj);
					message = "updateE_diagnosticoRecord: E_diagnostico Record With IdE_diagnostico: "
							+ updateE_diagnosticoObj.getIdeam() + " Is Successfully Updated In Database";
					facesContext.getExternalContext().getSessionMap().put("findE_diagnosticoById",
							updateE_diagnosticoObj);

				} else
					message = "updateE_diagnosticoRecord: E_diagnostico With IdE_diagnostico: " + key
							+ " doesn't exists.";
				feedback(message, true);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
		return message;
	}

	// Method To Add New E_diagnostico Details In Database
	public String addE_diagnosticoInDb(E_diagnostico E_diagnosticoObj) throws PSQLException {
		String message = null;

		if (E_diagnosticoObj.notnull()) {
			try {
				transObj = sessionObj.beginTransaction();
				Query queryObj = sessionObj
						.createQuery("from E_diagnostico where ideam = " + E_diagnosticoObj.getIdeam() +
								" and idexa = " + E_diagnosticoObj.getIdexa());
				E_diagnostico edia = (E_diagnostico) queryObj.uniqueResult();
				if (edia == null) {
					sessionObj.save(E_diagnosticoObj);
					message = "addE_diagnosticoRecord:Ideam: " + E_diagnosticoObj.getIdeam() + " Created In Database";
				} else {
					message = "E_diagnostico With Ideam: " + E_diagnosticoObj.getIdeam() + " already exists.";
				}
				feedback(message, false);
			} catch (Exception exceptionObj) {
				exceptionObj.printStackTrace();
				if (transObj.isActive())
					transObj.rollback();
			} finally {
				if (transObj.isActive())
					transObj.commit();
			}
		} else {
			message = "Essential data cannot be empty! Please, full fill the data.";
			feedback(message, true);
		}
		sessionObj.clear();
		return message;
	}

	// Method To Delete A Particular E_diagnostico Record From The Database
	public void deleteE_diagnosticoInDb(Integer ideam, Integer idexa) {
		String message;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj
					.createQuery("from E_diagnostico  where ideam = " + ideam + " and idexa = " + idexa);
			E_diagnostico particularE_diagnostico = (E_diagnostico) queryObj.uniqueResult();
			if (particularE_diagnostico != null) {
				sessionObj.delete(particularE_diagnostico);
				message = "E_diagnostico Record With Ideam: " + ideam + " Is Successfully Deleted From Database";
				facesContext.getExternalContext().getSessionMap().put("findE_diagnosticoById", ideam);
			} else
				message = "E_diagnostico With Ideam: " + ideam + " doesn't exists.";
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
	}

	// Method To Fetch Particular E_diagnostico Details From The Database
	@SuppressWarnings("unchecked")
	public List<E_diagnostico> searchAnE_diagnosticoSet(Integer ideam) {
		List<E_diagnostico> E_diagnosticoList = new ArrayList<E_diagnostico>();
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_diagnostico where ideam = " + ideam);
			E_diagnosticoList = (List<E_diagnostico>) queryObj.list();
			if (E_diagnosticoList != null) {
				if (!E_diagnosticoList.isEmpty()) {
					message = "Ideam: *" + ideam + "* was found.";
					facesContext.getExternalContext().getSessionMap().put("findE_diagnosticoById", ideam);
				} else
					message = "Ideam: *" + ideam + "* was not found in this database.";
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
		return E_diagnosticoList;
	}

	@SuppressWarnings("unchecked")
	public List<E_diagnostico> searchE_diagnosticosbyIdE_amostra(Integer ideam, String map) {
		List<E_diagnostico> E_diagnosticoList = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj
					.createQuery("from E_diagnostico where ideam = " + ideam + " order by data DESC");
			E_diagnosticoList = (List<E_diagnostico>) queryObj.list();
			if (E_diagnosticoList != null) {
				if (!E_diagnosticoList.isEmpty()) {
					message = "Idamo: " + ideam + " was found.";
					facesContext.getExternalContext().getSessionMap().put(map, ideam);
				} else {
					message = "Idamo: " + ideam + " was not found in this database.";
				}
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
		return E_diagnosticoList;
	}

	public E_diagnostico searchOneE_diagnosticobyId(Integer ideam, Integer idexa) {
		E_diagnostico OneE_diagnostico = null;
		String message = null;

		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj
					.createQuery("from E_diagnostico where ideam = " + ideam + " and idexa =" + idexa);
			OneE_diagnostico = (E_diagnostico) queryObj.uniqueResult();
			if (OneE_diagnostico != null) {
				message = "Ideam: " + ideam + " was found.";
				facesContext.getExternalContext().getSessionMap().put("findE_diagnosticoById", ideam);
			} else {
				message = "Ideam: " + ideam + " was not found in this database.";
			}
			feedback(message, false);
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return OneE_diagnostico;
	}

	@SuppressWarnings("unchecked")
	public List<E_diagnostico> retrieveAllE_diagnosticoAsList() {
		List<E_diagnostico> allE_diagnosticos = new ArrayList<E_diagnostico>();
		try {
			transObj = sessionObj.beginTransaction();
			Query queryObj = sessionObj.createQuery("from E_diagnostico table order by table.ideam ASC");
			allE_diagnosticos.addAll(queryObj.list());
			// System.out.println("All The E_diagnosticos Records Are Fetched Successfully
			// From Database");
		} catch (Exception exceptionObj) {
			exceptionObj.printStackTrace();
			if (transObj.isActive())
				transObj.rollback();
		} finally {
			if (transObj.isActive())
				transObj.commit();
		}
		sessionObj.clear();
		return allE_diagnosticos;
	}

}
