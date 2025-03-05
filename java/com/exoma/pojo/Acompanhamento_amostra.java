package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name = "ACOMPANHAMENTO_AMOSTRA")
public class Acompanhamento_amostra implements java.io.Serializable{

    @Id
    private Integer idacompanhamento;
    private Date data_saida;
    private String status;
    private String observacao;
    @OneToOne
    @JoinColumn(name="idamo", nullable = false)
    private Integer idamo;
    private Amostra amostra;
    private List<Acompanhamento_amostra> idallset;
    private boolean keep_idallset=false;
    private List<Acompanhamento_amostra> idlikeset;
    private String message;
    private List<String> Alltipo_status;
    private static DatabaseOperations dbObj;

    public void init(){
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Pragma", "no-cache");
        //setAmostra(new Amostra()); // Inicializa o objeto relacionado de Amostra, caso necessário
        getAllAcompAmostraAsList();

        // Carrega o primeiro acompanhamento existente, se disponível
        if (getAcompAmostraAll() != null && !isKeep_idallset()) {
            if (!getAcompAmostraAll().isEmpty()) {
                setIdacompanhamento(getAcompAmostraAll().get(0).getIdacompanhamento());
                searchOneAcompAmostra(); // Busca o primeiro acompanhamento na lista
                setKeep_idallset(true);
            }
        }
    }

    public Acompanhamento_amostra(){
    }

    public void reset() {
        setIdacompanhamento(0);
        setData_saida(null);
        setIdamo(0);
        setStatus(null);
        setObservacao(null);
        setMessage(null);
        if (getAmostra() != null)
            getAmostra().reset();
    }

    public boolean notnull() {
        List<Object> nogo = new ArrayList<Object>();
        nogo.add(0, "");
        nogo.add(1, null);
        nogo.add(2, 0);

        if (!nogo.contains(getIdamo()) && !nogo.contains(getStatus())
                && !nogo.contains(getData_saida()) && !nogo.contains(getObservacao()) )
            return true;
        return false;
    }

    public void saveAllAcompAmostraRecord() {
        // System.out.println("Calling saveAmostraRecord() Method To Save Amostra
        // Record");
        dbObj = new DatabaseOperations();
        dbObj.addAcompAmostraInDb(this);
        setKeep_idallset(false);
    }

    // Method To Fetch Particular Acompanhamento Details From The Database
    public List<Acompanhamento_amostra> searchAnAcompAmostraSetbyamostra(int idamo) {
        if (idamo > 0) {
            setAcompAmostraSet((new DatabaseOperations()).searchAnAcompAmostraSet(idamo));
            return getAcompAmostraSet();
        }
        return null;
    }

    // Method To Fetch Particular Amostra Details From The Database
    public List<Acompanhamento_amostra> searchAnAcompAmostraSet() {
        dbObj = new DatabaseOperations();
        setAcompAmostraSet(dbObj.searchAnAcompAmostraSet(getIdamo()));
        // System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: Etiqueta="
        // + getEtiqueta() + ", Local_coleta=" + getLocal_coleta());
        return getAcompAmostraSet();
    }

    public void updateAllAcompAmostraDetails() {
        //System.out.println("=================Calling updateAllAcompAmostraDetails() Method To Update Amostra Record=================");
        dbObj = new DatabaseOperations();
        dbObj.updateAcompAmostraRecord(this);
    }

    // Method To Delete A Particular Amostra Record From The Database
    public void deleteAcompAmostraRecord() {
        // System.out.println("Calling deleteAmostraRecord() Method To Delete Amostra
        // Record");
        dbObj = new DatabaseOperations();
        dbObj.deleteAcompAmostraInDb(getIdacompanhamento());
        setKeep_idallset(false);
    }

    public void deleteAcompAmostraRecordbyId(Integer p_idacompanhamento) {
        // System.out.println("Calling deleteAmostraRecord() Method To Delete Amostra
        // Record");
        dbObj = new DatabaseOperations();
        dbObj.deleteAcompAmostraInDb(p_idacompanhamento);
        setKeep_idallset(false);
    }

    public Acompanhamento_amostra searchOneAcompAmostra() {
        Acompanhamento_amostra selectAcompAmostra = null;
        if (getIdacompanhamento() == null)
            return null;
        if (getIdacompanhamento() != 0) {
            // System.out.println("Calling searchOneAmostra() Method Details For Amostra
            // Idamo?= " + getIdamo());
            dbObj = new DatabaseOperations();
            selectAcompAmostra = dbObj.searchOneAcompAmostra(getIdacompanhamento());
            if (selectAcompAmostra != null) {
                setData_saida(selectAcompAmostra.getData_saida());
                setIdacompanhamento(selectAcompAmostra.getIdacompanhamento());
                setStatus(selectAcompAmostra.getStatus());
                setObservacao(selectAcompAmostra.getObservacao());
                setIdamo(selectAcompAmostra.getIdamo());
                // System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: Etiqueta="
                // + getEtiqueta() + ", Local_coleta=" + getLocal_coleta());
                setAmostra(new Amostra());
                getAmostra().searchOneAmostrabyId(getIdamo());
                setMessage(null);
            } else {
                reset();
                setMessage("Identificador não encontrado");
            }
        } else {
            reset();
            setMessage("Identificador não encontrado");
        }
        return selectAcompAmostra;
    }

    public Acompanhamento_amostra searchOneAcompanhamentobyId(Integer p_idacompanhamento) {
        Acompanhamento_amostra selectAcompanhamento = null;
        if (p_idacompanhamento == null)
            return null;
        if (p_idacompanhamento.intValue() > 0) {
            // System.out.println("Calling searchOneAmostrabyId() Method Details For Amostra
            // Idamo?= " + p_idamo);
            dbObj = new DatabaseOperations();
            selectAcompanhamento = dbObj.searchOneAcompAmostra(p_idacompanhamento);
            if (selectAcompanhamento != null) {
                setIdacompanhamento(selectAcompanhamento.getIdacompanhamento());
                setData_saida(selectAcompanhamento.getData_saida());
                setStatus(selectAcompanhamento.getStatus());
                setObservacao(selectAcompanhamento.getObservacao());
                setIdamo(selectAcompanhamento.getIdamo());
                setAmostra(new Amostra());
                getAmostra().searchOneAmostrabyId(getIdamo());
                setMessage(null);
            } else {
                reset();
                setMessage("Identificador não encontrado");
            }
        } else {
            reset();
            setMessage("Identificador não encontrado");
        }
        return selectAcompanhamento;
    }


    // Method To Fetch All From The Database
    public List<Acompanhamento_amostra> getAllAcompAmostraAsList() {
        if (getAcompAmostraAll() != null && isKeep_idallset())
            return getAcompAmostraAll();
        // System.out.println("Calling getAllAmostraAsList() Method To Fetch Amostras
        // Record");
        dbObj = new DatabaseOperations();
        setAcompAmostraAll(dbObj.retrieveAllAcompAmostraAsList());
        return getAcompAmostraAll();
    }

    public List<Acompanhamento_amostra> searchOneAcompanhamentobyAmostra(int idamo){
        System.out.println("Amostra: "+idamo);
        if(idamo > 0){
            setAcompAmostraSet((new DatabaseOperations()).searchAnAcompAmostraSet(idamo));
            return getAcompAmostraSet();
        }
        return null;
    }

    public List<String> getAlltipo_status() {
        Alltipo_status = new ArrayList<String>();
        Alltipo_status.add(0, "Pronto para envio");
        Alltipo_status.add(1, "Em Transporte");
        Alltipo_status.add(2, "Chegou ao destinatário");
        Alltipo_status.add(3, "Sequenciamento em produção");
        Alltipo_status.add(4, "Sequenciamento Pronto");
        Alltipo_status.add(5, "Retornando");
        Alltipo_status.add(6, "Concluído");

        return Alltipo_status;
    }

    public java.sql.Date getData_saida() {
        return data_saida;
    }

    public void setData_saida(java.sql.Date data_saida) {
        this.data_saida = data_saida;
    }

    public String getStatus() {return this.status;}

    public void setStatus(String status) {this.status = status;}

    public String getObservacao() {return this.observacao;}

    public void setObservacao(String observacao) {this.observacao = observacao;}

    public Integer getIdamo() {return this.idamo;}

    public void setIdamo(Integer idamo) {this.idamo = idamo;}

    public void setAcompAmostraAll(List<Acompanhamento_amostra> amostraAll) {
        idallset = amostraAll;
    }

    public void setKeep_idallset(boolean keep_idallset) {
        this.keep_idallset = keep_idallset;
    }

    public boolean isKeep_idallset() {return keep_idallset;}

    public List<Acompanhamento_amostra> getAcompAmostraAll() {return idallset;}

    public List<Acompanhamento_amostra> getAcompAmostraSet() {
        return idlikeset;
    }

    public void setAcompAmostraSet(List<Acompanhamento_amostra> idlikeset) {
        this.idlikeset = idlikeset;
    }

    public void setIdacompanhamento(Integer idacompanhamento) {
        this.idacompanhamento = idacompanhamento;
    }

    public Integer getIdacompanhamento() {return this.idacompanhamento;}

    public Amostra getAmostra() {return amostra;}

    public void setAmostra(Amostra amostra) {this.amostra = amostra;}

    public String getMessage(){return message;}

    public void setMessage(String message) {
        this.message = message;
    }

}
