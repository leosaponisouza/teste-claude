package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="PERMISSAO")
public class Permissao implements java.io.Serializable {

	@Id
	private Integer idper;
	private String descricao;
	
	private List<Integer> all_idper;
	private List<String> all_descricao;
	private List<Integer> selected_idper;
	private List<Permissao> idlikeset;
	private List<Permissao> idallset;
	private boolean keep_idallset=false;	
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;
	
	public void init() {
		getAllPermissaoAsList();
		if(getPermissaoAll()!=null && !isKeep_idallset() ){
			if (getPermissaoAll().isEmpty() == false) {
				setIdper(getPermissaoAll().get(0).getIdper());
				setKeep_idallset(true);
			}
		}
	}
	
	public Permissao() { }
	
	public void reset() {
		setIdper(0);
		setDescricao(null);
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if (!nogo.contains(getDescricao()) )
			return true;
		return false;
	}	

	public Integer getIdper() {
		return this.idper;
	}

	public void setIdper(Integer idper) {
		this.idper = idper;
	}

	public List<Permissao> getPermissaoSet() {
		return idlikeset;
	}

	public void setPermissaoSet(List<Permissao> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Permissao> getPermissaoAll() {
		return idallset;
	}

	public void setPermissaoAll(List<Permissao> PermissaoAll) {
		idallset = PermissaoAll;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	public List<Integer> getAll_idper() {
		return all_idper;
	}

	public void setAll_idper(List<Integer> all_idper) {
		this.all_idper = all_idper;
	}

	public List<String> getAll_descricao() {
		return all_descricao;
	}

	public void setAll_descricao(List<String> all_descricao) {
		this.all_descricao = all_descricao;
	}

	public List<Integer> getSelected_idper() {
		return selected_idper;
	}

	public void setSelected_idper(List<Integer> selected_idper) {
		this.selected_idper = selected_idper;
	}

	// Method To Add New Permissao Details In Database
	public void savePermissaoRecord(Integer userid) {
		if (getSelected_idper()!=null && userid != null) {
			dbObj = new DatabaseOperations();
			dbObj.addPermissaoInDb(getSelected_idper(), userid);
			setKeep_idallset(false);
		}
	}

	// Method To Delete A Particular Permissao Record From The Database
	public void deletePermissaoRecord() {
		//System.out.println("Calling deletePermissaoRecord() Method To Delete Permissao Record");
		dbObj = new DatabaseOperations();
//		dbObj.deletePermissaoInDb(getIdper());
		setKeep_idallset(false);
	}
	
	public void deletePermissaoRecordbyId(Integer p_idper) {
		//System.out.println("Calling deletePermissaoRecord() Method To Delete Permissao Record");
		dbObj = new DatabaseOperations();
	//	dbObj.deletePermissaoInDb(p_idper);
		setKeep_idallset(false);
	}	


	// Method To Update Particular Permissao Details In Database
	public void updatePermissaoDetails() {
		//System.out.println("Calling updatePermissaoDetails() Method To Update Permissao Record");
			dbObj = new DatabaseOperations();		
			//dbObj.updatePermissaoRecord(this);
			setKeep_idallset(false);
	}
	
	public List<Permissao> searchPermissaobyIdUser(Integer iduser) {
		if (iduser!=null) {
			if (iduser.intValue() > 0) { 
				dbObj = new DatabaseOperations();		
				setPermissaoSet(dbObj.searchPermissaobyIdUser(iduser));
				setSelected_idper(new ArrayList<Integer>());
				for(Permissao p : getPermissaoSet()) {
					if (p!=null) {
						//Lista de strings com descrições de permissões			
						getSelected_idper().add(p.getIdper());
					}//if
				}//for
			}//if
		}//if
		return getPermissaoAll();
	}
	
	// Method To Fetch All From The Database
	public List<Permissao> getAllPermissaoAsList() {
		if (getPermissaoAll()!=null && isKeep_idallset()) return getPermissaoAll();
		dbObj = new DatabaseOperations();		
		setPermissaoAll(dbObj.retrieveAllPermissaoAsList());
		//Separa lista de strings e inteiros para mostar string no p:selectManyMenu
		setAll_descricao(new ArrayList<String>());
		setAll_idper(new ArrayList<Integer>());
		for(Permissao p : getPermissaoAll()) {
			if (p!=null) {
				//Lista de strings com descrições de permissões			
				getAll_descricao().add(p.getDescricao());
			    //Lista de inteiros com identificadores de permissões
				getAll_idper().add(p.getIdper());
			}
		}
		return getPermissaoAll();
	}
	
}
