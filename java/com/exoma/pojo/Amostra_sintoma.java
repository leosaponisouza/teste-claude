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
@Table(name="AMOSTRA_SINTOMA")
public class Amostra_sintoma implements java.io.Serializable {
	
	private String sintoma=null;
	@Id
    @ManyToOne
    @JoinColumn(name="idamo", nullable=false)
	private Integer idamo;
    
	private List<String> items;
	private List<Amostra_sintoma> idlikeset;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public Amostra_sintoma() {}

	public void reset() {
		setIdamo(0);
		setItems(new ArrayList<String>());
		setSintoma("empty");
		setAmostra_sintomaSet(null);
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getIdamo()) && !nogo.contains(getSintoma())) return true;
		return false;
	}
	
	public String getSintoma() {
		return this.sintoma;
	}
	public void setSintoma(String sintoma) {
		this.sintoma = sintoma;
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
    
	public List<Amostra_sintoma> getAmostra_sintomaSet() {
		return idlikeset;
	}
	public void setAmostra_sintomaSet(List<Amostra_sintoma> idlikeset) {
		this.idlikeset = idlikeset;
	}

    public void uploadSintomas(Integer idamo) {
    	if(idamo!=null) {
        	if (idamo.intValue()>0 && getItems()!=null) {
        		if(!getItems().isEmpty()) {
            		for(String item:getItems()) {
            			Amostra_sintoma novo = new Amostra_sintoma();
            			novo.setIdamo(idamo);
            			novo.setSintoma(item);
            			saveAmostra_sintomaRecordbyId(novo);
            		}
        		}
        	}
        	
    	}
    }
    
    public void updateSintomas(Integer idamo, List<String> formitems) {
		if(getItems()==null) {
    		deleteAmostra_sintomaRecord(idamo); 
    	}else if (idamo>0 && getItems() != null && idamo.intValue() == getIdamo().intValue() ) {
    			deleteAmostra_sintomaRecord(idamo);
    			uploadSintomas(idamo);
    		}
    }
	
	private void saveAmostra_sintomaRecordbyId(Amostra_sintoma item) {
		if (item!=null) {
			//System.out.println("Calling saveAmostra_sintomaRecordbyId("+ item.getSintoma() +") Method To Save Amostra_sintoma Record");
			dbObj = new DatabaseOperations();
			dbObj.addAmostra_sintomaInDb(item);
		}
	}
	
	// Method To Delete A Particular Amostra_sintoma Record From The Database
	private void deleteAmostra_sintomaRecord(Integer idamo) {
		//System.out.println("Calling deleteAmostra_sintomaRecord() Method To Delete Amostra_sintoma Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteAmostra_sintomaInDb(idamo);
	}
	
	public Amostra_sintoma searchAmostra_sintomasbyId(Integer idamo) {
		Amostra_sintoma selectAmostra_sintoma=null;
		if (idamo > 0 ) { 
			System.out.println("Calling searchAmostra_sintomasbyId() Method Details For Amostra_sintoma Idamo?= " + idamo);
			dbObj = new DatabaseOperations();	
			List<Amostra_sintoma> Amostra_sintomas_searched = dbObj.searchAmostra_sintomasbyId(idamo);
			if (!Amostra_sintomas_searched.isEmpty()){
				setAmostra_sintomaSet(Amostra_sintomas_searched);
				selectAmostra_sintoma = getAmostra_sintomaSet().get(0);
				setIdamo( selectAmostra_sintoma.getIdamo());
				setSintoma( selectAmostra_sintoma.getSintoma());
				System.out.println("Fetched Sintoma? " + getSintoma() + " Details Are: Sintoma=" + getSintoma() );
				selectAmostra_sintoma.setItems(new ArrayList<String>());
				for (int i=0; i< getAmostra_sintomaSet().size(); i++)
						selectAmostra_sintoma.getItems().add(getAmostra_sintomaSet().get(i).getSintoma());
				setItems( selectAmostra_sintoma.getItems());
			}

		}
		
		return selectAmostra_sintoma;
	}
	
}
