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
@Table(name="AMOSTRA_MEDICAMENTO")
public class Amostra_medicamento implements java.io.Serializable {
	
	private String medicamento=null;
	@Id
    @ManyToOne
    @JoinColumn(name="idamo", nullable=false)
	private Integer idamo;
    
	private List<String> items;
	private List<Amostra_medicamento> idlikeset;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public Amostra_medicamento() {}

	public void reset() {
		setIdamo(0);
		setItems(new ArrayList<String>());
		setMedicamento("empty");
		setAmostra_medicamentoSet(null);
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getIdamo()) && !nogo.contains(getMedicamento())) return true;
		return false;
	}
	
	public String getMedicamento() {
		return this.medicamento;
	}
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
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
    
	public List<Amostra_medicamento> getAmostra_medicamentoSet() {
		return idlikeset;
	}
	public void setAmostra_medicamentoSet(List<Amostra_medicamento> idlikeset) {
		this.idlikeset = idlikeset;
	}

    public void uploadMedicamentos(Integer idamo) {
    	if(idamo!=null) {
        	if (idamo.intValue()>0 && getItems()!=null) {
        		if(!getItems().isEmpty()) {
            		for(String item:getItems()) {
            			Amostra_medicamento novo = new Amostra_medicamento();
            			novo.setIdamo(idamo);
            			novo.setMedicamento(item);
            			saveAmostra_medicamentoRecordbyId(novo);
            		}
        		}
        	}
        	
    	}
    }
    
    public void updateMedicamentos(Integer idamo, List<String> formitems) {
		if(getItems()==null) {
    		deleteAmostra_medicamentoRecord(idamo); 
    	}else if (idamo>0 && getItems() != null && idamo.intValue() == getIdamo().intValue() ) {
    				deleteAmostra_medicamentoRecord(idamo);
    				uploadMedicamentos(idamo);
    			}
    }
	
	private void saveAmostra_medicamentoRecordbyId(Amostra_medicamento item) {
		if (item!=null) {
			//System.out.println("Calling saveAmostra_medicamentoRecordbyId("+ item.getMedicamento() +") Method To Save Amostra_medicamento Record");
			dbObj = new DatabaseOperations();
			dbObj.addAmostra_medicamentoInDb(item);
		}
	}
	
	// Method To Delete A Particular Amostra_medicamento Record From The Database
	private void deleteAmostra_medicamentoRecord(Integer idamo) {
		//System.out.println("Calling deleteAmostra_medicamentoRecord() Method To Delete Amostra_medicamento Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteAmostra_medicamentoInDb(idamo);
	}
	
	public Amostra_medicamento searchAmostra_medicamentosbyId(Integer idamo) {
		Amostra_medicamento selectAmostra_medicamento=null;
		if (idamo > 0 ) { 
			System.out.println("Calling searchAmostra_medicamentosbyId() Method Details For Amostra_medicamento Idamo?= " + idamo);
			dbObj = new DatabaseOperations();	
			List<Amostra_medicamento> Amostra_medicamentos_searched = dbObj.searchAmostra_medicamentosbyId(idamo);
			if (!Amostra_medicamentos_searched.isEmpty()){
				setAmostra_medicamentoSet(Amostra_medicamentos_searched);
				selectAmostra_medicamento = getAmostra_medicamentoSet().get(0);
				setIdamo( selectAmostra_medicamento.getIdamo());
				setMedicamento( selectAmostra_medicamento.getMedicamento());
				System.out.println("Fetched Medicamento? " + getMedicamento() + " Details Are: Medicamento=" + getMedicamento() );
				selectAmostra_medicamento.setItems(new ArrayList<String>());
				for (int i=0; i< getAmostra_medicamentoSet().size(); i++)
						selectAmostra_medicamento.getItems().add(getAmostra_medicamentoSet().get(i).getMedicamento());
				setItems( selectAmostra_medicamento.getItems());
			}

		}
		
		return selectAmostra_medicamento;
	}
	
}
