package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name = "AMOSTRA")
public class Amostra implements java.io.Serializable {

	@Id
	private Integer idamo;
	private Date data_coleta;
	private String etiqueta;
	private String tipo_amostra;
	private String local_coleta;
	private String lote;
	private int validade_coleta;
	private long cpf;
	private int idpro;
	private List<Amostra> idlikeset;
	private List<Amostra> idallset;
	private boolean keep_idallset = false;
	private List<String> Alltipo_amostras;
	private String message;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "refAmostra")
	private List<com.exoma.pojo.Amostra_sintoma> sintomas;
	private List<com.exoma.pojo.E_amostra> e_amostras;

	private com.exoma.pojo.E_amostra e_amostra;
	private com.exoma.pojo.Amostra_sintoma amostra_sintoma;
	private Amostra_medicamento amostra_medicamento;
	private Amostra_armazenagem amostra_armazenagem;
	private Projeto projeto;
	private Diagnostico diagnostico;

	public void init() {
		getAllAmostraAsList();
		if (getAmostra_armazenagem() == null && this.getAmostra_medicamento() == null
				&& this.getAmostra_sintoma() == null) {
			System.out.println(">>>>>>>>>>>>>> Iniciou dependencias");
			setAmostra_sintoma(new Amostra_sintoma());
			setAmostra_medicamento(new Amostra_medicamento());
			setAmostra_armazenagem(new Amostra_armazenagem());
		}
		if (getAmostraAll() != null && !isKeep_idallset()) {
			if (getAmostraAll().isEmpty() == false) {
				setIdamo(getAmostraAll().get(0).getIdamo());
				searchOneAmostra();
				setKeep_idallset(true);
			}
		}

	}

	public Amostra() {
	}

	public void reset() {
		setData_coleta(null);
		setIdamo(0);
		setEtiqueta(null);
		setTipo_amostra(null);
		setLocal_coleta(null);
		setLote(null);
		setValidade_coleta(0);
		setIdpro(0);
		setCpf(0);
		setMessage(null);
		if (getE_amostra() != null)
			getE_amostra().reset();
		if (getAmostra_sintoma() != null)
			getAmostra_sintoma().reset();
		if (getAmostra_medicamento() != null)
			getAmostra_medicamento().reset();
		if (getAmostra_armazenagem() != null)
			getAmostra_armazenagem().reset();
		if (getDiagnostico() != null)
			getDiagnostico().reset();
	}

	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0, "");
		nogo.add(1, null);
		nogo.add(2, 0);

		// System.out.println("getData_coleta()="+ getData_coleta());
		// System.out.println("getTipo_amostra()="+ getTipo_amostra());
		// System.out.println("getCpf()="+ getCpf());
		// System.out.println("getLocal_coleta()="+ getLocal_coleta());
		// System.out.println("getValidade_coleta()="+ getValidade_coleta());
		// System.out.println("getIdpro()="+ getIdpro());

		if (!nogo.contains(getData_coleta()) && !nogo.contains(getTipo_amostra())
				&& !nogo.contains(getCpf()) && !nogo.contains(getLocal_coleta())
				&& !nogo.contains(getValidade_coleta()) && !nogo.contains(getIdpro()))
			return true;
		return false;
	}

	// Method To Add New Amostra Details In Database
	public void saveAmostraRecord() {
		// System.out.println("Calling saveAmostraRecord() Method To Save Amostra
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.addAmostraInDb(this);
		setKeep_idallset(false);
	}

	public void saveAllAmostraRecord(E_amostra eamo, Amostra_sintoma amosin,
			Amostra_medicamento amomed, Amostra_armazenagem amoarm, Diagnostico diag) {
		// System.out.println("Calling saveAmostraRecord() Method To Save Amostra
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.addAmostraInDb(this);
		eamo.addE_amostraRecordbyId(getIdamo(), eamo.getTecnica(), eamo.getDigitalizado_por());
		amosin.uploadSintomas(getIdamo());
		amomed.uploadMedicamentos(getIdamo());
		amoarm.uploadArmazenagens(getIdamo());
		diag.calcData_prevista(getData_coleta());
		diag.saveDiagnosticoRecordbyIdAmostra(getIdamo());
		setKeep_idallset(false);
	}

	// Method To Delete A Particular Amostra Record From The Database
	public void deleteAmostraRecord() {
		// System.out.println("Calling deleteAmostraRecord() Method To Delete Amostra
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteAmostraInDb(getIdamo());
		setKeep_idallset(false);
	}

	public void deleteAmostraRecordbyId(Integer p_idamo) {
		// System.out.println("Calling deleteAmostraRecord() Method To Delete Amostra
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteAmostraInDb(p_idamo);
		setKeep_idallset(false);
	}

	// Method To Fetch Particular Amostra Details From The Database
	public List<Amostra> searchAnAmostraSetbyparticipante(long cpf) {
		if (cpf > 0) {
			setAmostraSet((new DatabaseOperations()).searchAnAmostraSet(cpf));
			return getAmostraSet();
		}
		return null;
	}

	// Method To Fetch Particular Amostra Details From The Database
	public List<Amostra> searchAnAmostraSet() {
		dbObj = new DatabaseOperations();
		setAmostraSet(dbObj.searchAnAmostraSet(getCpf()));
		// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: Etiqueta="
		// + getEtiqueta() + ", Local_coleta=" + getLocal_coleta());
		return getAmostraSet();
	}

	public Amostra searchOneAmostra() {
		Amostra selectAmostra = null;
		if (getIdamo() == null)
			return null;
		if (getIdamo() != 0) {
			// System.out.println("Calling searchOneAmostra() Method Details For Amostra
			// Idamo?= " + getIdamo());
			dbObj = new DatabaseOperations();
			selectAmostra = dbObj.searchOneAmostra(getIdamo());
			if (selectAmostra != null) {
				setData_coleta(selectAmostra.getData_coleta());
				setIdamo(selectAmostra.getIdamo());
				setEtiqueta(selectAmostra.getEtiqueta());
				setTipo_amostra(selectAmostra.getTipo_amostra());
				setLocal_coleta(selectAmostra.getLocal_coleta());
				setLote(selectAmostra.getLote());
				setValidade_coleta(selectAmostra.getValidade_coleta());
				setIdpro(selectAmostra.getIdpro());
				setCpf(selectAmostra.getCpf());
				// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: Etiqueta="
				// + getEtiqueta() + ", Local_coleta=" + getLocal_coleta());
				setE_amostra(new E_amostra());
				getE_amostra().searchE_amostrasbyIdAmostra(getIdamo());
				setAmostra_sintoma(new Amostra_sintoma());
				getAmostra_sintoma().searchAmostra_sintomasbyId(getIdamo());
				setAmostra_medicamento(new Amostra_medicamento());
				getAmostra_medicamento().searchAmostra_medicamentosbyId(getIdamo());
				setAmostra_armazenagem(new Amostra_armazenagem());
				getAmostra_armazenagem().searchAmostra_armazenagemsbyId(getIdamo());
				setProjeto(new Projeto());
				getProjeto().searchOneProjetobyId(getIdpro());
				setDiagnostico(new Diagnostico());
				getDiagnostico().searchDiagnosticosbyIdAmostra(getIdamo());
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectAmostra;
	}

	public Amostra searchOneAmostrabyId(Integer p_idamo) {
		Amostra selectAmostra = null;
		if (p_idamo == null)
			return null;
		if (p_idamo.intValue() > 0) {
			// System.out.println("Calling searchOneAmostrabyId() Method Details For Amostra
			// Idamo?= " + p_idamo);
			dbObj = new DatabaseOperations();
			selectAmostra = dbObj.searchOneAmostra(p_idamo);
			if (selectAmostra != null) {
				setData_coleta(selectAmostra.getData_coleta());
				setIdamo(selectAmostra.getIdamo());
				setEtiqueta(selectAmostra.getEtiqueta());
				setTipo_amostra(selectAmostra.getTipo_amostra());
				setLocal_coleta(selectAmostra.getLocal_coleta());
				setLote(selectAmostra.getLote());
				setValidade_coleta(selectAmostra.getValidade_coleta());
				setIdpro(selectAmostra.getIdpro());
				setCpf(selectAmostra.getCpf());
				// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: Etiqueta="
				// + getEtiqueta() + ", Local_coleta=" + getLocal_coleta());
				setE_amostra(new E_amostra());
				getE_amostra().searchE_amostrasbyIdAmostra(getIdamo());
				setAmostra_sintoma(new Amostra_sintoma());
				getAmostra_sintoma().setIdamo(getIdamo());
				getAmostra_sintoma().searchAmostra_sintomasbyId(getIdamo());
				setAmostra_medicamento(new Amostra_medicamento());
				getAmostra_medicamento().setIdamo(getIdamo());
				getAmostra_medicamento().searchAmostra_medicamentosbyId(getIdamo());
				setAmostra_armazenagem(new Amostra_armazenagem());
				getAmostra_armazenagem().setIdamo(getIdamo());
				getAmostra_armazenagem().searchAmostra_armazenagemsbyId(getIdamo());
				setProjeto(new Projeto());
				getProjeto().searchOneProjetobyId(getIdpro());
				setDiagnostico(new Diagnostico());
				getDiagnostico().searchDiagnosticosbyIdAmostra(getIdamo());
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectAmostra;
	}

	// Method To Update Particular Amostra Details In Database
	public void updateAmostraDetails() {
		// System.out.println("Calling updateAmostraDetails() Method To Update Amostra
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.updateAmostraRecord(this);
	}

	public void updateAllAmostraDetails(E_amostra eamo, Amostra_sintoma amosin,
			Amostra_medicamento amomed, Amostra_armazenagem amoarm, Diagnostico diag) {
		// System.out.println("Calling updateAllAmostraDetails() Method To Update
		// Amostra Record");
		dbObj = new DatabaseOperations();
		dbObj.updateAmostraRecord(this);
		eamo.addE_amostraRecordbyId(getIdamo(), eamo.getTecnica(), eamo.getDigitalizado_por());
		amosin.updateSintomas(getIdamo(), amosin.getItems());
		amomed.updateMedicamentos(getIdamo(), amomed.getItems());
		amoarm.updateArmazenagens(getIdamo(), amoarm.getItems());
		diag.updateDiagnosticoDetails();
	}

	// Method To Fetch All From The Database
	public List<Amostra> getAllAmostraAsList() {
		if (getAmostraAll() != null && isKeep_idallset())
			return getAmostraAll();
		// System.out.println("Calling getAllAmostraAsList() Method To Fetch Amostras
		// Record");
		dbObj = new DatabaseOperations();
		setAmostraAll(dbObj.retrieveAllAmostraAsList());
		return getAmostraAll();
	}

	public Integer getIdamo() {
		return this.idamo;
	}

	public void setIdamo(Integer idamo) {
		this.idamo = idamo;
	}

	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getLocal_coleta() {
		return this.local_coleta;
	}

	public void setLocal_coleta(String local_coleta) {
		this.local_coleta = local_coleta;
	}

	public List<Amostra> getAmostraSet() {
		return idlikeset;
	}

	public void setAmostraSet(List<Amostra> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public Date getData_coleta() {
		return data_coleta;
	}

	public void setData_coleta(Date data_coleta) {
		this.data_coleta = data_coleta;
	}

	public String getTipo_amostra() {
		return tipo_amostra;
	}

	public void setTipo_amostra(String tipo_amostra) {
		this.tipo_amostra = tipo_amostra;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public int getValidade_coleta() {
		return validade_coleta;
	}

	public void setValidade_coleta(int validade_coleta) {
		this.validade_coleta = validade_coleta;
	}

	public int getIdpro() {
		return idpro;
	}

	public void setIdpro(int idpro) {
		this.idpro = idpro;
	}

	public List<Amostra> getAmostraAll() {
		return idallset;
	}

	public void setAmostraAll(List<Amostra> participanteAll) {
		idallset = participanteAll;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public Amostra_sintoma getAmostra_sintoma() {
		return amostra_sintoma;
	}

	public void setAmostra_sintoma(Amostra_sintoma amostra_sintoma) {
		this.amostra_sintoma = amostra_sintoma;
	}

	public E_amostra getE_amostra() {
		return e_amostra;
	}

	public void setE_amostra(E_amostra e_amostra) {
		this.e_amostra = e_amostra;
	}

	public List<E_amostra> getE_amostras() {
		return e_amostras;
	}

	public void setE_amostras(List<E_amostra> e_amostras) {
		this.e_amostras = e_amostras;
	}

	public Amostra_medicamento getAmostra_medicamento() {
		return amostra_medicamento;
	}

	public void setAmostra_medicamento(Amostra_medicamento amostra_medicamento) {
		this.amostra_medicamento = amostra_medicamento;
	}

	public Amostra_armazenagem getAmostra_armazenagem() {
		return amostra_armazenagem;
	}

	public void setAmostra_armazenagem(Amostra_armazenagem amostra_armazenagem) {
		this.amostra_armazenagem = amostra_armazenagem;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<String> getAlltipo_amostras() {
		Alltipo_amostras = new ArrayList<String>();
		Alltipo_amostras.add(0, "Saliva");
		Alltipo_amostras.add(1, "Swab");
		Alltipo_amostras.add(2, "Sangue");
		Alltipo_amostras.add(3, "Fezes");
		Alltipo_amostras.add(4, "Urina");
		Alltipo_amostras.add(5, "Biopsia");
		Alltipo_amostras.add(6, "Solo");
		Alltipo_amostras.add(7, "Água");
		Alltipo_amostras.add(8, "Ar");
		Alltipo_amostras.add(9, "Detritos");
		Alltipo_amostras.add(10, "Prova");
		Alltipo_amostras.add(11, "Vegetal");
		Alltipo_amostras.add(12, "Animal");
		Alltipo_amostras.add(13, "Orgânico");
		Alltipo_amostras.add(14, "Inorgânico");
		Alltipo_amostras.add(15, "Bactéria");
		Alltipo_amostras.add(16, "Vírus");
		Alltipo_amostras.add(17, "Fungo");
		return Alltipo_amostras;
	}

	public void setAlltipo_amostras(List<String> alltipo_amostras) {
		Alltipo_amostras = alltipo_amostras;
	}

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
