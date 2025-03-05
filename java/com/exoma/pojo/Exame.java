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
@Table(name="EXAME")
public class Exame implements java.io.Serializable {
	@Id
	private Integer idexa;
	private Date data_versao;
	private String metodo=null;
	private String autor=null;
	private String nome=null;
	private String versao=null;
	private String finalidade=null;	
	private Integer prazo_conclusao;
	private Integer ambiente;

	private String message;
	private List<Exame> idlikeset;
	private List<Exame> idallset;
	private List<Exame> idallset2;
	private boolean keep_idallset=false;
	private static final long serialVersionUID = 1L;

	public Exame() {}

	public void init(Integer ambiente) {
		getAllExameAsList(ambiente);
		if(getExameAll()!=null && !isKeep_idallset() ){
			if (getExameAll().isEmpty() == false) {
				setIdexa(getExameAll().get(0).getIdexa());
				searchOneExame();
				setKeep_idallset(true);
			}
		}
	}

	public void reset() {
		setIdexa(0);
		setData_versao(null);
		setMetodo(null);
		setAutor(null);
		setNome(null);
		setVersao(null);
		setFinalidade(null);
		setPrazo_conclusao(0);
		setAmbiente(0);
		setExameSet(null);
		setMessage(null);
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getNome()) && !nogo.contains(getMetodo()) &&
			 !nogo.contains(getAutor()) &&
			 !nogo.contains(getData_versao()) && !nogo.contains(getAmbiente())
				
				) return true;
		return false;
	}

	// Method To Update Particular Exame Details In Database
	public void updateExameDetails() {
		//System.out.println("Calling updateExameDetails() Method To Update Exame Record");
			(new DatabaseOperations()).updateExameRecord(this);
	}

	public void saveExameRecord() {
		//System.out.println("Calling saveExameRecord() Method To Save Exame Record");
		(new DatabaseOperations()).addExameInDb(this);
		setKeep_idallset(false);
	}
	
	// Method To Delete A Particular Exame Record From The Database
	public void deleteExameRecord() {
		//System.out.println("Calling deleteExameRecord() Method To Delete Exame Record");
		(new DatabaseOperations()).deleteExameInDb(getIdexa());
		setKeep_idallset(false);
	}
	
	public void deleteExameRecordbyId(Integer idexa) {
		//System.out.println("Calling deleteExameRecordbyId() Method To Delete Exame Record");
		(new DatabaseOperations()).deleteExameInDb(idexa);
		setKeep_idallset(false);
	}
	
	public Exame searchOneExame() {
		return searchExamesbyId(getIdexa());
		
	}
	
	public Exame searchExamesbyId(Integer idexa) {
		Exame selectExame=null;
		if (idexa!=null) {
			if (idexa > 0 ) { 
				System.out.println("Calling searchExamesbyId() Method Details For Exame idexa?= " + idexa);
				List<Exame> Exames_searched = (new DatabaseOperations()).searchExamesbyId(idexa);
				if(Exames_searched!=null) {
					if (!Exames_searched.isEmpty()){
						setExameSet(Exames_searched);
						selectExame = getExameSet().get(0);
						setIdexa(selectExame.getIdexa());
						setData_versao(selectExame.getData_versao());
						setMetodo( selectExame.getMetodo());				
						setAutor(selectExame.getAutor());
						setNome(selectExame.getNome());
						setVersao(selectExame.getVersao());
						setFinalidade(selectExame.getFinalidade());
						setPrazo_conclusao(selectExame.getPrazo_conclusao());
						setAmbiente(selectExame.getAmbiente());
						System.out.println("Fetched Metodo? " + getIdexa() + " Details Are: Metodo=" + getMetodo() );
						setMessage(null);
					}else {reset(); setMessage("Identificador não encontrado");}
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}			
		return selectExame;
	}
	
	public List<Exame> searchAnExameSet() {
		List<Exame> Exames_searched=null;
		if (getMetodo() !=null) { 
			System.out.println("Calling searchAnExameSet() Method Details For metodo?= " + getMetodo());
			Exames_searched = (new DatabaseOperations()).searchAnExameSet(getMetodo());
			if(Exames_searched!=null) {
				if (!Exames_searched.isEmpty()){
					setExameSet(Exames_searched);
					Exame selectExame = getExameSet().get(0);
					setIdexa(selectExame.getIdexa());
					setData_versao(selectExame.getData_versao());
					setMetodo( selectExame.getMetodo());				
					setAutor(selectExame.getAutor());
					setNome(selectExame.getNome());
					setVersao(selectExame.getVersao());
					setFinalidade(selectExame.getFinalidade());
					setPrazo_conclusao(selectExame.getPrazo_conclusao());
					setAmbiente(selectExame.getAmbiente());
					System.out.println("Fetched Metodo? " + getIdexa() + " Details Are: Metodo=" + getMetodo() );
					setMessage(null);
				}else {reset(); setMessage("Identificador não encontrado");}
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return Exames_searched;
	}
	
	// Method To Fetch All From The Database
	public List<Exame> getAllExameAsList(Integer ambiente) {
		if (getExameAll()!=null && isKeep_idallset()) return returnlist(ambiente);
		//System.out.println("Calling getAllExameAsList() Method To Fetch Exames Record");
		setExameAll ((new DatabaseOperations()).retrieveAllExameAsList(1));
		setExameAll2((new DatabaseOperations()).retrieveAllExameAsList(2));
		return returnlist(ambiente);
	}
	
	private List<Exame> returnlist(Integer ambiente){
		if (ambiente.intValue() == 1) 
			return getExameAll();
		else if (ambiente.intValue() == 2)
			return getExameAll2();
		else {
			List<Exame> all12 = new ArrayList<Exame>();
			 for(int i=0; i< getExameAll().size();i++){
				 all12.add(getExameAll().get(i));
			 }
			 for(int i=0; i< getExameAll2().size();i++){
				 all12.add(getExameAll2().get(i));
			 }
			 
			return all12;
		}
	}

	public Integer getIdexa() {
		return idexa;
	}

	public void setIdexa(Integer idexa) {
		this.idexa = idexa;
	}

	public Date getData_versao() {
		return data_versao;
	}

	public void setData_versao(Date data_versao) {
		this.data_versao = data_versao;
	}

	public String getMetodo() {
		return this.metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
	
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public List<Exame> getExameSet() {
		return idlikeset;
	}
	public void setExameSet(List<Exame> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public Integer getPrazo_conclusao() {
		return prazo_conclusao;
	}

	public void setPrazo_conclusao(Integer prazo_conclusao) {
		this.prazo_conclusao = prazo_conclusao;
	}

	public List<Exame> getExameAll() {
		return idallset;
	}

	public void setExameAll(List<Exame> exameAll) {
		idallset = exameAll;
	}

	public List<Exame> getExameAll2() {
		return idallset2;
	}

	public void setExameAll2(List<Exame> idallset2) {
		this.idallset2 = idallset2;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	public Integer getAmbiente() {
		return ambiente;
	}

	public String getAmbiente(int p_tipo) {
		if (p_tipo==1 ) 
			return "Físico";
		else 
			return "Virtual";
	}
	
	public void setAmbiente(Integer ambiente) {
		this.ambiente = ambiente;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
