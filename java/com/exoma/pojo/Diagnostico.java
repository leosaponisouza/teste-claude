package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="DIAGNOSTICO")
public class Diagnostico implements java.io.Serializable {
	@Id
	private Integer iddia;
	private Date data_conclusao;
	private String resultado=null;	
	private Date data_prevista;
    @ManyToOne
    @JoinColumn(name="idexa", nullable=false)
	private Integer idexa;	
    @ManyToOne
    @JoinColumn(name="idamo", nullable=false)
	private Integer idamo;

    private String message;
    private Exame exame;
	private List<Diagnostico> idlikeset;
	private List<Diagnostico> idallset;
	private boolean keep_idallset=false;	
	private static final long serialVersionUID = 1L;

	public Diagnostico() {}
	
	
	public void init() {
		setExame(new Exame());
		reset();
		getAllDiagnosticoAsList();
		if( getDiagnosticoAll()!=null && !isKeep_idallset() ){
			if (getDiagnosticoAll().isEmpty() == false) {
				searchOneDiagnosticobyId(getDiagnosticoAll().get(0).getIddia());
				setKeep_idallset(true);
			}
		}
	}

	public void reset() {
		setIddia(0);
		setData_conclusao(null);
		setResultado(null);
		setData_prevista(null);		
		setIdexa(0);
		setIdamo(0);
		setDiagnosticoSet(null);
		setMessage(null);
		if (getExame()!=null ) 
			getExame().reset(); 
		
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getIdamo()) && !nogo.contains(getResultado()) &&
			 !nogo.contains(getIdexa()) && !nogo.contains(getData_conclusao())  
			) return true;
		return false;
	}

	// Method To Update Particular Diagnostico Details In Database
	public void updateDiagnosticoDetails() {
		//System.out.println("Calling updateDiagnosticoDetails() Method To Update Diagnostico Record");
		calcData_prevista();
		(new DatabaseOperations()).updateDiagnosticoRecord(this);
	}

	public void saveDiagnosticoRecordbyIdAmostra(Integer idamo) {
		if (idamo!=null) {
			this.setIdamo(idamo);
			//System.out.println("Calling saveDiagnosticoRecordbyId("+ item.getResultado() +") Method To Save Diagnostico Record");
			calcData_prevista();
			(new DatabaseOperations()).addDiagnosticoInDb(this);
			setKeep_idallset(false);
		}
	}
	
	// Method To Delete A Particular Diagnostico Record From The Database
	public void deleteDiagnosticoRecord() {
		//System.out.println("Calling deleteDiagnosticoRecord() Method To Delete Diagnostico Record");
		(new DatabaseOperations()).deleteDiagnosticoInDb(getIddia());
		setKeep_idallset(false);
	}
	
	// Method To Delete A Particular Diagnostico Record From The Database
	public void deleteDiagnosticoRecordbyId(Integer iddia) {
		//System.out.println("Calling deleteDiagnosticoRecordbyId() Method To Delete Diagnostico Record");
		(new DatabaseOperations()).deleteDiagnosticoInDb(iddia);
		setKeep_idallset(false);
	}
	
	public Diagnostico searchDiagnosticosbyIdAmostra(Integer idamo) {
		Diagnostico selectDiagnostico=null;
		if (idamo != null) {
			if(idamo.intValue()>0) {
				System.out.println("Calling searchDiagnosticosbyIdAmostra() Method Details For Diagnostico Idamo?= " + idamo);
				List<Diagnostico> Diagnosticos_searched = (new DatabaseOperations()).searchDiagnosticosbyIdAmostra(idamo, "findDiagnosticosByIdAmostra");
				if (Diagnosticos_searched != null) {
					if (!Diagnosticos_searched.isEmpty()){
						setDiagnosticoSet(Diagnosticos_searched);
						selectDiagnostico = getDiagnosticoSet().get(0);
						setIddia(selectDiagnostico.getIddia());
						setData_conclusao(selectDiagnostico.getData_conclusao());
						setResultado( selectDiagnostico.getResultado());				
						setData_prevista(selectDiagnostico.getData_prevista());
						setIdexa(selectDiagnostico.getIdexa());
						setIdamo( selectDiagnostico.getIdamo());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Resultado=" + getResultado() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
						setMessage(null);
					}else {reset(); setMessage("Identificador não encontrado");}					
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return selectDiagnostico;
	}
	
	public Diagnostico searchOneDiagnosticobyId(Integer iddia) {
		Diagnostico OneDiagnostico=null;
		if (iddia != null) {
			if (iddia.intValue() > 0 ) { 
				System.out.println("Calling searchOneDiagnosticobyId() Method Details For Diagnostico Iddia?= " + iddia);
				OneDiagnostico = (new DatabaseOperations()).searchOneDiagnosticobyId(iddia);
				if (OneDiagnostico != null){
					setIddia(OneDiagnostico.getIddia());
					setData_conclusao(OneDiagnostico.getData_conclusao());
					setResultado( OneDiagnostico.getResultado());				
					setData_prevista(OneDiagnostico.getData_prevista());
					setIdexa(OneDiagnostico.getIdexa());
					setIdamo( OneDiagnostico.getIdamo());
					System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Data_conclusao=" + getData_conclusao() );
					setExame(new Exame());
					getExame().searchExamesbyId(getIdexa());
					setMessage(null);
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return OneDiagnostico;
	}
	
	public List<Diagnostico> searchAnDiagnosticoSetbyAmostra(Integer idamo) {
		//Aqui faz mais sentido buscar o conjunto de diagnósticos de uma amostra do que um conjunto de identificadores com algum padrão
		if (idamo != null) {
			if(idamo.intValue()>0) {
				setIdamo(idamo);
				searchAnDiagnosticoSet();
			}
		}
		return null;
		
	}

	public List<Diagnostico> searchAnDiagnosticoSet() {
		//Aqui faz mais sentido buscar o conjunto de diagnósticos de uma amostra do que um conjunto de identificadores com algum padrão
		List<Diagnostico> Diagnosticos_searched=null;		
		Diagnostico selectDiagnostico=null;
		if (getIdamo()!=null) {
			if (getIdamo().intValue() > 0 ) { 
				System.out.println("Calling searchAnDiagnosticoSet() Method Details For Diagnostico Idamo?= " + getIdamo());
				Diagnosticos_searched = (new DatabaseOperations()).searchDiagnosticosbyIdAmostra(getIdamo(), "findDiagnosticoById");
				if (Diagnosticos_searched != null) {
					if (!Diagnosticos_searched.isEmpty()){
						setDiagnosticoSet(Diagnosticos_searched);
						selectDiagnostico = getDiagnosticoSet().get(0);
						setIddia(selectDiagnostico.getIddia());
						setData_conclusao(selectDiagnostico.getData_conclusao());
						setResultado( selectDiagnostico.getResultado());				
						setData_prevista(selectDiagnostico.getData_prevista());
						setIdexa(selectDiagnostico.getIdexa());
						setIdamo( selectDiagnostico.getIdamo());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Atualizado em=" + getData_conclusao() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
						setMessage(null);
					}else {reset(); setMessage("Identificador não encontrado");}
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return Diagnosticos_searched;
	}
	
	public Diagnostico searchAnDiagnosticoSet(Integer iddia) {
		Diagnostico selectDiagnostico=null;
		if (iddia != null) {
			if (iddia.intValue() > 0 ) { 
				System.out.println("Calling searchAnDiagnosticoSet() Method Details For Diagnostico Iddia?= " + iddia);
				List<Diagnostico> Diagnosticos_searched = (new DatabaseOperations()).searchAnDiagnosticoSet(iddia);
				if (Diagnosticos_searched != null) {
					if (!Diagnosticos_searched.isEmpty()){
						setDiagnosticoSet(Diagnosticos_searched);
						selectDiagnostico = getDiagnosticoSet().get(0);
						setIddia(selectDiagnostico.getIddia());
						setData_conclusao(selectDiagnostico.getData_conclusao());
						setResultado( selectDiagnostico.getResultado());				
						setData_prevista(selectDiagnostico.getData_prevista());
						setIdexa(selectDiagnostico.getIdexa());
						setIdamo( selectDiagnostico.getIdamo());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Resultado=" + getResultado() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
						setMessage(null);
					}else {reset(); setMessage("Identificador não encontrado");}
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return selectDiagnostico;
	}

	public Integer getIddia() {
		return iddia;
	}

	public void setIddia(Integer iddia) {
		this.iddia = iddia;
	}

	public Date getData_conclusao() {
		return data_conclusao;
	}

	public void setData_conclusao(Date data_conclusao) {
		this.data_conclusao = data_conclusao;
	}

	public Date getData_prevista() {
		return data_prevista;
	}

	public void setData_prevista(Date data_prevista) {
		this.data_prevista = data_prevista;
	}
	
	public void calcData_prevista() {
		Amostra amo = new Amostra();
		amo.searchOneAmostrabyId(getIdamo());
		calcData_prevista(amo.getData_coleta());
	}
	
	public void calcData_prevista(Date data_coleta ) {
		
		if (data_coleta != null && getExame()!=null && getIdexa()!=null) {
			if(data_coleta instanceof Date) {
				Integer prazo_conclusao= getExame().searchExamesbyId(getIdexa()).getPrazo_conclusao();
				if (prazo_conclusao != null)
					if (prazo_conclusao.intValue() > 0) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(data_coleta);
						cal.add(Calendar.DAY_OF_MONTH, prazo_conclusao+1);
						setData_prevista(new java.sql.Date(cal.getTimeInMillis()));
					}else System.out.println("Erro em Diagnostico: prazo_conclusao <= 0:"+prazo_conclusao);
				else System.out.println("Erro em Diagnostico: prazo_conclusao nulo:"+ prazo_conclusao);
			}else System.out.println("Erro em Diagnostico: data_coleta não é instancia de Date:"+data_coleta);
		}else System.out.println("Erro em Diagnostico: dados nulos ");
		
	}
	
	// Method To Fetch All From The Database
	public List<Diagnostico> getAllDiagnosticoAsList() {
		if (getDiagnosticoAll()!=null  && isKeep_idallset() ) return getDiagnosticoAll();
		//System.out.println("Calling getAllDiagnosticoAsList() Method To Fetch Diagnosticos Record");
		DatabaseOperations dobj = new DatabaseOperations(); 
		setDiagnosticoAll(dobj.retrieveAllDiagnosticoAsList());
		return getDiagnosticoAll();
	}

	public Integer getIdexa() {
		return idexa;
	}

	public void setIdexa(Integer idexa) {
		this.idexa = idexa;
	}
	
	public String getResultado() {
		return this.resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	public Integer getIdamo() {
		return this.idamo;
	}
	public void setIdamo(Integer idamo) {
		this.idamo = idamo;
	}
	
	public List<Diagnostico> getDiagnosticoSet() {
		return idlikeset;
	}
	public void setDiagnosticoSet(List<Diagnostico> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public List<Diagnostico> getDiagnosticoAll() {
		return idallset;
	}

	public void setDiagnosticoAll(List<Diagnostico> idallset) {
		this.idallset = idallset;
	}

	public Exame getExame() {
		return exame;
	}

	public void setExame(Exame exame) {
		this.exame = exame;
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

}
