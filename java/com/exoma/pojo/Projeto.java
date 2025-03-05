package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name = "PROJETO")
public class Projeto implements java.io.Serializable {

	@Id
	private Integer idpro;
	private Date data;
	private String responsavel;
	private String titulo;
	private String codigo_bioproject = null;

	private List<Projeto> idlikeset;
	private List<Projeto> idallset;
	private boolean keep_idallset = false;
	private String message;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public void init() {
		getAllProjetoAsList();
		if (getProjetoAll() != null && !isKeep_idallset()) {
			if (getProjetoAll().isEmpty() == false) {
				setIdpro(getProjetoAll().get(0).getIdpro());
				searchOneProjeto();
				setKeep_idallset(true);
			}
		}
	}

	public Projeto() {
	}

	public void reset() {
		setData(null);
		setIdpro(0);
		setResponsavel(null);
		setTitulo(null);
		setMessage(null);
	}

	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0, "");
		nogo.add(1, null);
		nogo.add(2, 0);

		if (!nogo.contains(getData()) && !nogo.contains(getTitulo()) && !nogo.contains(getResponsavel())
				&& !nogo.contains(getCodigo_bioproject()))
			return true;
		return false;
	}

	public Integer getIdpro() {
		return this.idpro;
	}

	public void setIdpro(Integer idpro) {
		this.idpro = idpro;
	}

	public String getResponsavel() {
		return this.responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public List<Projeto> getProjetoSet() {
		return idlikeset;
	}

	public void setProjetoSet(List<Projeto> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCodigo_bioproject() {
		return codigo_bioproject;
	}

	public void setCodigo_bioproject(String codigo_bioproject) {
		this.codigo_bioproject = codigo_bioproject;
	}

	public List<Projeto> getProjetoAll() {
		return idallset;
	}

	public void setProjetoAll(List<Projeto> ProjetoAll) {
		idallset = ProjetoAll;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// Method To Add New Projeto Details In Database
	public void saveProjetoRecord() {
		// System.out.println("Calling saveProjetoRecord() Method To Save Projeto
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.addProjetoInDb(this);
		setKeep_idallset(false);
	}

	// Method To Delete A Particular Projeto Record From The Database
	public void deleteProjetoRecord() {
		// System.out.println("Calling deleteProjetoRecord() Method To Delete Projeto
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteProjetoInDb(getIdpro());
		setKeep_idallset(false);
	}

	public void deleteProjetoRecordbyId(Integer p_idpro) {
		// System.out.println("Calling deleteProjetoRecord() Method To Delete Projeto
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteProjetoInDb(p_idpro);
		setKeep_idallset(false);
	}

	// Method To Fetch Particular Projeto Details From The Database
	public List<Projeto> searchAnProjetoSet() {
		// System.out.println("Calling searchAnProjetoSet() Method Details For Projeto
		// Idpro?= " + getIdpro());
		dbObj = new DatabaseOperations();
		setProjetoSet(dbObj.searchAnProjetoSet(getResponsavel()));
		// System.out.println("Fetched Idpro? " + getIdpro() + " Details Are:
		// Responsavel=" + getResponsavel() + ", Local_coleta=" + getLocal_coleta());
		return getProjetoSet();
	}

	public Projeto searchOneProjeto() {
		Projeto selectProjeto = null;
		if (getIdpro() != 0) {
			// System.out.println("Calling searchOneProjeto() Method Details For Projeto
			// Idpro?= " + getIdpro());
			dbObj = new DatabaseOperations();
			selectProjeto = dbObj.searchOneProjeto(getIdpro());
			if (selectProjeto != null) {
				setData(selectProjeto.getData());
				setIdpro(selectProjeto.getIdpro());
				setResponsavel(selectProjeto.getResponsavel());
				setTitulo(selectProjeto.getTitulo());
				setCodigo_bioproject(selectProjeto.getCodigo_bioproject());
				// System.out.println("Fetched Idpro? " + getIdpro() + " Details Are:
				// Responsavel=" + getResponsavel() + ", Local_coleta=" + getLocal_coleta());
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectProjeto;
	}

	public Projeto searchOneProjetobyId(Integer p_idpro) {
		Projeto selectProjeto = null;
		if (p_idpro != 0) {
			// System.out.println("Calling searchOneProjetobyId() Method Details For Projeto
			// Idpro?= " + p_idpro);
			dbObj = new DatabaseOperations();
			selectProjeto = dbObj.searchOneProjeto(p_idpro);
			if (selectProjeto != null) {
				setData(selectProjeto.getData());
				setIdpro(selectProjeto.getIdpro());
				setResponsavel(selectProjeto.getResponsavel());
				setTitulo(selectProjeto.getTitulo());
				setCodigo_bioproject(selectProjeto.getCodigo_bioproject());
				// System.out.println("Fetched Idpro? " + getIdpro() + " Details Are:
				// Responsavel=" + getResponsavel() + ", Local_coleta=" + getLocal_coleta());
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectProjeto;
	}

	// Method To Update Particular Projeto Details In Database
	public void updateProjetoDetails() {
		// System.out.println("Calling updateProjetoDetails() Method To Update Projeto
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.updateProjetoRecord(this);
		setKeep_idallset(false);
	}

	// Method To Fetch All From The Database
	public List<Projeto> getAllProjetoAsList() {
		if (getProjetoAll() != null && isKeep_idallset())
			return getProjetoAll();
		// System.out.println("Calling getAllProjetoAsList() Method To Fetch Projetos
		// Record");
		dbObj = new DatabaseOperations();
		setProjetoAll(dbObj.retrieveAllProjetoAsList());
		return getProjetoAll();
	}

}
