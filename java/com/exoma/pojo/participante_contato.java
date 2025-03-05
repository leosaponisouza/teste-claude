package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="PARTICIPANTE_CONTATO")
public class participante_contato implements java.io.Serializable {

	private String contato=null;
    @ManyToOne
    @JoinColumn(name="cpf", nullable=false)
	private long cpf;
    
	private List<String> items;
	private List<participante_contato> idlikeset;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public participante_contato() {}
		
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getCpf()) && !nogo.contains(getContato())) return true;
		return false;
	}
	
	public void reset() {
		setContato(null);
		setCpf(0);
		setparticipante_contatoSet(null);
		setItems(null);
	}
	
	public String getContato() {
		return this.contato;
	}
	public void setContato(String contato) {
		this.contato = contato;
	}
	
	public long getCpf() {
		return this.cpf;
	}
	public void setCpf(long cpf) {
		this.cpf = cpf;
	}
	
    public List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> items) {
        this.items=items;
    }
    
    public void uploadContatos(long cpf) {
    	if (cpf>0 && getItems()!=null) {
    		for(String item:getItems()) {
    			participante_contato novo = new participante_contato();
    			novo.setCpf(cpf);
    			novo.setContato(item);
    			saveparticipante_contatoRecordbyId(novo);
    		}
    	}
    }
    
    public void updateContatos(long cpf) {
    	if(getItems()==null) {
    		deleteparticipante_contatoRecord(cpf);
    	} else if (cpf>0 && getItems() != null) {
    				deleteparticipante_contatoRecord(cpf);
    				uploadContatos(cpf);
    			}
    }
    
	public List<participante_contato> getparticipante_contatoSet() {
		return idlikeset;
	}
	public void setparticipante_contatoSet(List<participante_contato> idlikeset) {
		this.idlikeset = idlikeset;
	}
	
	private void saveparticipante_contatoRecordbyId(participante_contato item) {
		if (item!=null) {
			//System.out.println("Calling saveparticipante_contatoRecordbyId("+ item.getContato() +") Method To Save participante_contato Record");
			dbObj = new DatabaseOperations();
			dbObj.addparticipante_contatoInDb(item);
		}
	}
	
	// Method To Delete A Particular participante_contato Record From The Database
	private void deleteparticipante_contatoRecord(long cpf) {
		//System.out.println("Calling deleteparticipante_contatoRecord() Method To Delete participante_contato Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteparticipante_contatoInDb(cpf);
	}
	
	public participante_contato searchOneparticipante_contatobyId(long cpf) {
		participante_contato selectparticipante_contato=null;
		setItems(new ArrayList<String>());
		if (cpf > 0) { 
			//System.out.println("Calling searchOneparticipante_contatobyId() Method Details For participante_contato Cpf?= " + cpf);
			dbObj = new DatabaseOperations();	
			setparticipante_contatoSet(dbObj.searchAnparticipante_contatoSet(cpf));
			if (!getparticipante_contatoSet().isEmpty()){
				selectparticipante_contato = getparticipante_contatoSet().get(0);
				setCpf( selectparticipante_contato.getCpf());
				setContato( selectparticipante_contato.getContato());
				//System.out.println("Fetched Contato? " + getContato() + " Details Are: Contato=" + getContato() );
				selectparticipante_contato.setItems(new ArrayList<String>());
				for (int i=0; i< getparticipante_contatoSet().size(); i++)
						selectparticipante_contato.getItems().add(getparticipante_contatoSet().get(i).getContato());
				setItems( selectparticipante_contato.getItems());
			}
		}
		return selectparticipante_contato;
	}
	
	public participante_contato searchOneparticipante_contatobyId(String contato) {
		participante_contato selectparticipante_contato=null;
		if (contato != null) { 
			//System.out.println("Calling searchOneparticipante_contatobyId() Method Details For participante_contato Cpf?= " + contato);
			dbObj = new DatabaseOperations();		
			selectparticipante_contato = dbObj.searchOneparticipante_contato(contato);
			if (selectparticipante_contato!= null) {
				setCpf( selectparticipante_contato.getCpf());
				setContato( selectparticipante_contato.getContato());
				//System.out.println("Fetched Contato? " + contato + " Details Are: Contato=" + contato );
			}
		}
		return selectparticipante_contato;
	}
}
