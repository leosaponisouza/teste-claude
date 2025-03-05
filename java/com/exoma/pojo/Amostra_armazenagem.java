package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="AMOSTRA_ARMAZENAGEM")
public class Amostra_armazenagem implements java.io.Serializable {
	
	private String armazenagem=null;
	@Id
    @ManyToOne
    @JoinColumn(name="idamo", nullable=false)
	private Integer idamo;
    
	private List<String> items;
	private List<Amostra_armazenagem> idlikeset;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public Amostra_armazenagem() {}

	public void reset() {
		setIdamo(0);
		setItems(new ArrayList<String>());
		setArmazenagem("empty");
		setAmostra_armazenagemSet(null);
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getIdamo()) && !nogo.contains(getArmazenagem())) return true;
		return false;
	}
	
	public String getArmazenagem() {
		return this.armazenagem;
	}
	public void setArmazenagem(String armazenagem) {
		this.armazenagem = armazenagem;
	}
	
	public Integer getIdamo() {
		return this.idamo;
	}
	public void setIdamo(Integer idamo) {
		this.idamo = idamo;
	}
	
    public List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> items) {
        this.items=items;
    }
    
	public List<Amostra_armazenagem> getAmostra_armazenagemSet() {
		return idlikeset;
	}
	public void setAmostra_armazenagemSet(List<Amostra_armazenagem> idlikeset) {
		this.idlikeset = idlikeset;
	}

    public void uploadArmazenagens(Integer idamo) {
    	if(idamo!=null) {
        	if (idamo.intValue()>0 && getItems()!=null) {
        		if(!getItems().isEmpty()) {
            		for(String item:getItems()) {
            			Amostra_armazenagem novo = new Amostra_armazenagem();
            			novo.setIdamo(idamo);
            			novo.setArmazenagem(item);
            			saveAmostra_armazenagemRecordbyId(novo);
            		}
        		}
        	}
        	
    	}
    }
    
    public void updateArmazenagens(Integer idamo, List<String> formitems) {
		if(getItems()==null) {
    		deleteAmostra_armazenagemRecord(idamo); 
    	}
    	if(getIdamo()!=null && idamo!=null && getItems() != null)
    		if (idamo>0 && idamo.intValue() == getIdamo().intValue() ) {
    			deleteAmostra_armazenagemRecord(idamo);
    			uploadArmazenagens(idamo);
    		}
    }
	
	private void saveAmostra_armazenagemRecordbyId(Amostra_armazenagem item) {
		if (item!=null) {
			//System.out.println("Calling saveAmostra_armazenagemRecordbyId("+ item.getArmazenagem() +") Method To Save Amostra_armazenagem Record");
			dbObj = new DatabaseOperations();
			dbObj.addAmostra_armazenagemInDb(item);
		}
	}
	
	// Method To Delete A Particular Amostra_armazenagem Record From The Database
	private void deleteAmostra_armazenagemRecord(Integer idamo) {
		//System.out.println("Calling deleteAmostra_armazenagemRecord() Method To Delete Amostra_armazenagem Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteAmostra_armazenagemInDb(idamo);
	}
	
	public Amostra_armazenagem searchAmostra_armazenagemsbyId(Integer idamo) {
		Amostra_armazenagem selectAmostra_armazenagem=null;
		if (idamo > 0 ) { 
			System.out.println("Calling searchAmostra_armazenagemsbyId() Method Details For Amostra_armazenagem Idamo?= " + idamo);
			dbObj = new DatabaseOperations();	
			List<Amostra_armazenagem> Amostra_armazenagems_searched = dbObj.searchAmostra_armazenagemsbyId(idamo);
			if (!Amostra_armazenagems_searched.isEmpty()){
				setAmostra_armazenagemSet(Amostra_armazenagems_searched);
				selectAmostra_armazenagem = getAmostra_armazenagemSet().get(0);
				setIdamo( selectAmostra_armazenagem.getIdamo());
				setArmazenagem( selectAmostra_armazenagem.getArmazenagem());
				System.out.println("Fetched Armazenagem? " + getArmazenagem() + " Details Are: Armazenagem=" + getArmazenagem() );
				selectAmostra_armazenagem.setItems(new ArrayList<String>());
				for (int i=0; i< getAmostra_armazenagemSet().size(); i++)
						selectAmostra_armazenagem.getItems().add(getAmostra_armazenagemSet().get(i).getArmazenagem());
				setItems( selectAmostra_armazenagem.getItems());
			}

		}
		
		return selectAmostra_armazenagem;
	}
	
}
