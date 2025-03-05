package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import org.postgresql.util.PSQLException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="E_DIAGNOSTICO")
public class E_diagnostico implements java.io.Serializable {

	@Id
    @ManyToOne
    @JoinColumn(name="ideam", nullable=false)
	private Integer ideam;
	@Id
    @ManyToOne
    @JoinColumn(name="idexa", nullable=false)
	private Integer idexa;
	private Date data;
	private String resultado=null;	
	private Double probabilidade;

    private Exame exame;
    private String message;
	private List<E_diagnostico> idlikeset;
	private List<E_diagnostico> idallset;
	private boolean keep_idallset=false;	
	private static final long serialVersionUID = 1L;

	public E_diagnostico() {}
	
	
	public void init() {
		setExame(new Exame());
		reset();
		getAllE_diagnosticoAsList();
		if( getE_diagnosticoAll()!=null && !isKeep_idallset() ){
			if (getE_diagnosticoAll().isEmpty() == false) {
				searchOneE_diagnosticobyId(getE_diagnosticoAll().get(0).getIdeam(),getE_diagnosticoAll().get(0).getIdexa());
				setKeep_idallset(true);
			}
		}
	}

	public void reset() {
		setData(null);
		setResultado("Aguardado");
		setProbabilidade(null);		
		setIdexa(0);
		setIdeam(0);
		setE_diagnosticoSet(null);
		setMessage(null);
		if (getExame()!=null ) 
			getExame().reset(); 
		
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getIdeam()) && !nogo.contains(getResultado()) &&
			 !nogo.contains(getIdexa()) && !nogo.contains(getData())
			 && !nogo.contains(getProbabilidade())
			) return true;
		return false;
	}

	// Method To Update Particular E_diagnostico Details In Database
	public void updateE_diagnosticoDetails() {
		if (isFKexists(ideam, idexa)) {
			//System.out.println("Calling updateE_diagnosticoDetails() Method To Update E_diagnostico Record");
			setMessage((new DatabaseOperations()).updateE_diagnosticoRecord(this));
		}else {
			setMessage("Campos identificadores do E_diagnóstico inválidos");
		}

	}

	public void saveE_diagnosticoRecordbyIdE_amostra(Integer ideam, Integer idexa) {
		if (isFKexists(ideam, idexa)) {
			setIdeam(ideam);
			setIdexa(idexa);
			try {
				setMessage((new DatabaseOperations()).addE_diagnosticoInDb(this));
			} catch (PSQLException e) {
				e.printStackTrace();
			}
			setKeep_idallset(false);
		}else {
			setMessage("Campos identificadores do E_diagnóstico inválidos");
		}
	}
	
	// Method To Delete A Particular E_diagnostico Record From The Database
	public void deleteE_diagnosticoRecord() {
		//System.out.println("Calling deleteE_diagnosticoRecord() Method To Delete E_diagnostico Record");
		(new DatabaseOperations()).deleteE_diagnosticoInDb(getIdeam(), getIdexa());
		setKeep_idallset(false);
	}
	
	// Method To Delete A Particular E_diagnostico Record From The Database
	public void deleteE_diagnosticoRecordbyId(Integer ideam, Integer idexa) {
		//System.out.println("Calling deleteE_diagnosticoRecordbyId() Method To Delete E_diagnostico Record");
		(new DatabaseOperations()).deleteE_diagnosticoInDb(ideam, idexa);
		setKeep_idallset(false);
	}
	
	public E_diagnostico searchE_diagnosticosbyIdE_amostra(Integer ideam) {
		E_diagnostico selectE_diagnostico=null;
		if (ideam != null) {
			if(ideam.intValue()>0) {
				System.out.println("Calling searchE_diagnosticosbyIdE_amostra() Method Details For E_diagnostico Ideam?= " + ideam);
				List<E_diagnostico> E_diagnosticos_searched = (new DatabaseOperations()).searchE_diagnosticosbyIdE_amostra(ideam, "findE_diagnosticosByIdE_amostra");
				if (E_diagnosticos_searched!=null) {
					if (!E_diagnosticos_searched.isEmpty()){
						setE_diagnosticoSet(E_diagnosticos_searched);
						selectE_diagnostico = getE_diagnosticoSet().get(0);
						setData(selectE_diagnostico.getData());
						setResultado( selectE_diagnostico.getResultado());				
						setProbabilidade(selectE_diagnostico.getProbabilidade());
						setIdexa(selectE_diagnostico.getIdexa());
						setIdeam( selectE_diagnostico.getIdeam());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Resultado=" + getResultado() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
					}else {reset(); setMessage("Identificador não encontrado");}
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return selectE_diagnostico;
	}
	
	public E_diagnostico searchOneE_diagnosticobyId(Integer ideam, Integer idexa) {
		String message=null;
		E_diagnostico OneE_diagnostico=null;
		if (ideam != null && idexa != null) {
			if (ideam.intValue() > 0 && idexa.intValue() > 0) { 
				OneE_diagnostico = (new DatabaseOperations()).searchOneE_diagnosticobyId(ideam, idexa);
				if (OneE_diagnostico != null){
					setData(OneE_diagnostico.getData());
					setResultado( OneE_diagnostico.getResultado());				
					setProbabilidade(OneE_diagnostico.getProbabilidade());
					setIdexa(OneE_diagnostico.getIdexa());
					setIdeam( OneE_diagnostico.getIdeam());
					System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Data=" + getData() );
					setExame(new Exame());
					getExame().searchExamesbyId(getIdexa());
				} else {reset(); message = "Sem resultados!";}
			} else {reset();message = "Identificador inválido!";}
		}else {reset();message = "Identificador inválido!";}
		setMessage(message);
		return OneE_diagnostico;
	}
	
	public List<E_diagnostico> searchAnE_diagnosticoSetbyE_amostra(Integer ideam) {
		//Aqui faz mais sentido buscar o conjunto de diagnósticos de uma amostra do que um conjunto de identificadores com algum padrão
		if (ideam != null) {
			if(ideam.intValue()>0) {
				setIdeam(ideam);
				searchAnE_diagnosticoSet();
			}
		}
		return null;
		
	}

	public List<E_diagnostico> searchAnE_diagnosticoSet() {
		//Aqui faz mais sentido buscar o conjunto de diagnósticos de uma amostra do que um conjunto de identificadores com algum padrão
		String message=null;
		List<E_diagnostico> E_diagnosticos_searched=null;		
		E_diagnostico selectE_diagnostico=null;
		if (getIdeam()!=null) {
			if (getIdeam().intValue() > 0 ) { 
				System.out.println("Calling searchAnE_diagnosticoSet() Method Details For E_diagnostico Ideam?= " + getIdeam());
				E_diagnosticos_searched = (new DatabaseOperations()).searchE_diagnosticosbyIdE_amostra(getIdeam(), "findE_diagnosticoById");
				if (E_diagnosticos_searched!=null) {
					if (!E_diagnosticos_searched.isEmpty()){
						setE_diagnosticoSet(E_diagnosticos_searched);
						selectE_diagnostico = getE_diagnosticoSet().get(0);
						setData(selectE_diagnostico.getData());
						setResultado( selectE_diagnostico.getResultado());				
						setProbabilidade(selectE_diagnostico.getProbabilidade());
						setIdexa(selectE_diagnostico.getIdexa());
						setIdeam( selectE_diagnostico.getIdeam());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Atualizado em=" + getData() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
					}else message = "Sem resultados!";
				}else message = "Sem resultados!";
			}else message = "Identificador inválido!";
		}else message = "Identificador inválido!";
		setMessage(message);
		return E_diagnosticos_searched;
	}
	
	public E_diagnostico searchAnE_diagnosticoSet(Integer ideam) {
		E_diagnostico selectE_diagnostico=null;
		if (ideam != null && idexa != null) {
			if (ideam.intValue() > 0 && idexa.intValue() > 0) { 
				List<E_diagnostico> E_diagnosticos_searched = (new DatabaseOperations()).searchAnE_diagnosticoSet(ideam);
				if (E_diagnosticos_searched!=null) {
					if (!E_diagnosticos_searched.isEmpty()){
						setE_diagnosticoSet(E_diagnosticos_searched);
						selectE_diagnostico = getE_diagnosticoSet().get(0);
						setData(selectE_diagnostico.getData());
						setResultado( selectE_diagnostico.getResultado());				
						setProbabilidade(selectE_diagnostico.getProbabilidade());
						setIdexa(selectE_diagnostico.getIdexa());
						setIdeam( selectE_diagnostico.getIdeam());
						System.out.println("Fetched Resultado? " + getResultado() + " Details Are: Resultado=" + getResultado() );
						setExame(new Exame());
						getExame().searchExamesbyId(getIdexa());
					}
				}
			}
		}
		return selectE_diagnostico;
	}
	
	private Boolean isFKexists(Integer ideam, Integer idexa) {
		if(ideam!=null && idexa != null) {
			if(ideam.intValue()>0 && idexa.intValue()>0) {
				E_amostra eam = new E_amostra();
				Exame exa = new Exame();
				if ( eam.searchOneE_amostrabyId(ideam)!=null && exa.searchExamesbyId(idexa)!= null)
					return true;
			}
		}
		return false;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getProbabilidade() {
		return probabilidade;
	}

	public void setProbabilidade(Double probabilidade) {
		this.probabilidade = probabilidade;
	}
	
	// Method To Fetch All From The Database
	public List<E_diagnostico> getAllE_diagnosticoAsList() {
		if (getE_diagnosticoAll()!=null  && isKeep_idallset() ) return getE_diagnosticoAll();
		//System.out.println("Calling getAllE_diagnosticoAsList() Method To Fetch E_diagnosticos Record");
		DatabaseOperations dobj = new DatabaseOperations(); 
		setE_diagnosticoAll(dobj.retrieveAllE_diagnosticoAsList());
		return getE_diagnosticoAll();
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
	
	public Integer getIdeam() {
		return this.ideam;
	}
	public void setIdeam(Integer ideam) {
		this.ideam = ideam;
	}
	
	public List<E_diagnostico> getE_diagnosticoSet() {
		return idlikeset;
	}
	public void setE_diagnosticoSet(List<E_diagnostico> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public List<E_diagnostico> getE_diagnosticoAll() {
		return idallset;
	}

	public void setE_diagnosticoAll(List<E_diagnostico> idallset) {
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
