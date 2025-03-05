package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name="termo_assentimento")
public class Termo_assentimento implements java.io.Serializable {

    private String pdf = null;
    private String filename = null;
    private Date data;
    @ManyToOne
    @JoinColumn(name="cpf", nullable=false)
    private long cpf;
    
    private UploadedFile file = null;
    private String message;
    private List<Termo_assentimento> idlikeset;
    private List<Termo_assentimento> idallset;
    public static DatabaseOperations dbObj;
    private static final long serialVersionUID = 1L;

    public void init() {
        getAllTermoAssentimentoAsList();
        if (getTermoAssentimentoAll() != null) {
            if (!getTermoAssentimentoAll().isEmpty()) {
                setCpf(getTermoAssentimentoAll().get(0).getCpf());
                searchOneTermoAssentimento();
            }
        }
    }
    
    public Termo_assentimento() {}
    
    public Termo_assentimento(long cpf) {
        setCpf(cpf);
        setData();
    }
    
    public void reset() {
        setData(null);
        setCpf(0);
        setPdf((String)null);
        setMessage(null);
        setFilename(null);
        setFile(null);
    }
    
    public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if (!nogo.contains(getCpf()) && !nogo.contains(getPdf())
			&& !nogo.contains(getData()) && !nogo.contains(getFilename()) 
				) return true;
		return false;
	}	
    public long getCpf() {
        return this.cpf;
    }   
    public void setCpf(long cpf) {
        this.cpf = cpf;
    }
    public String getPdf() {
        return this.pdf;
    }   

    public void setMessage(String message) {
        this.message = message;
    }    
    public String getMessage() {
        return this.message;
    }    

    public byte[] getPdftoDownload() {
        return Base64.getDecoder().decode(getPdf().trim());
    }     

    public void setPdf(String filecontent) {
        this.pdf = filecontent;
    }    
    
    public void setPdf(UploadedFile file) {
        byte[] byte_filecontent = file.getContent();
        setPdf(Base64.getEncoder().encodeToString(byte_filecontent));
    }
    
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    
    public void setData() {
        setData(new java.sql.Date(System.currentTimeMillis()));
    }    
    
    public List<Termo_assentimento> getTermoAssentimentoSet() {
        return idlikeset;
    }
    public void setTermoAssentimentoSet(List<Termo_assentimento> idlikeset) {
        this.idlikeset = idlikeset;
    }
    public List<Termo_assentimento> getTermoAssentimentoAll() {
        return idallset;
    }    
    public void setTermoAssentimentoAll(List<Termo_assentimento> termoAssentimentoAll) {
        idallset = termoAssentimentoAll;
    }
    
    // Method To Add New Termo_assentimento Details In Database
    public void saveTermoAssentimentoRecord() {
        // System.out.println("Calling saveTermoAssentimentoRecord() Method To Save Termo_assentimento Record");
        setData();
        dbObj = new DatabaseOperations();
        dbObj.addTermoAssentimentoInDb(this);
    }

    public void saveTermoAssentimentoRecordbyId(long cpf) {
        if (cpf > 0) {
            // System.out.println("Calling saveTermoAssentimentoRecordbyId(" + cpf + ") Method To Save Termo_assentimento Record");
            setCpf(cpf);
            setData();
            dbObj = new DatabaseOperations();
            dbObj.addTermoAssentimentoInDb(this);
        }
    }

    // Method To Delete A Particular Termo_assentimento Record From The Database
    public void deleteTermoAssentimentoRecord() {
        // System.out.println("Calling deleteTermoAssentimentoRecord() Method To Delete Termo_assentimento Record");
        dbObj = new DatabaseOperations();
        dbObj.deleteTermoAssentimentoInDb(getCpf());
    }

    public void deleteTermoAssentimentoRecordbyId(long p_cpf) {
        // System.out.println("Calling deleteTermoAssentimentoRecord() Method To Delete Termo_assentimento Record");
        dbObj = new DatabaseOperations();
        dbObj.deleteTermoAssentimentoInDb(p_cpf);
    }
    // Method To Fetch Particular Termo_assentimento Details From The Database
    public List<Termo_assentimento> searchAnTermoAssentimentoSet() {
        // System.out.println("Calling searchAnTermoAssentimentoSet() Method Details For Termo_assentimento Cpf?= " + getCpf());
        dbObj = new DatabaseOperations();
        setTermoAssentimentoSet(dbObj.searchAnTermoAssentimentoSet(getCpf()));
        // System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" + getPdf());
        return getTermoAssentimentoSet();
    }

    public Termo_assentimento searchOneTermoAssentimento() {
        Termo_assentimento selectTermoAssentimento = null;
        if (getCpf() != 0) {
            // System.out.println("Calling searchOneTermoAssentimento() Method Details For Termo_assentimento Cpf?= " + getCpf());
            dbObj = new DatabaseOperations();
            selectTermoAssentimento = dbObj.searchOneTermoAssentimento(getCpf());
            if (selectTermoAssentimento != null) {
                setData(selectTermoAssentimento.getData());
                setCpf(selectTermoAssentimento.getCpf());
                setPdf(selectTermoAssentimento.getPdf());
                setFilename(selectTermoAssentimento.getFilename());
                // System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" + getPdf());
            }
        }
        return selectTermoAssentimento;
    }

    public Termo_assentimento searchOneTermoAssentimentobyId(long p_cpf) {
        Termo_assentimento selectTermoAssentimento = null;

        setFilename(new String());
        if (p_cpf != 0) {
            // System.out.println("Calling searchOneTermoAssentimentobyId() Method Details For Termo_assentimento Cpf?= " + p_cpf);
            dbObj = new DatabaseOperations();
            selectTermoAssentimento = dbObj.searchOneTermoAssentimento(p_cpf);
            if (selectTermoAssentimento != null) {
                setData(selectTermoAssentimento.getData());
                setCpf(selectTermoAssentimento.getCpf());
                setPdf(selectTermoAssentimento.getPdf());
                setFilename(selectTermoAssentimento.getFilename());
                // System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" + getPdf());
            } else if (getFile() == null) {
                setFilename("Inexistente");
            } else {
                setFilename(getFile().getFileName());
            }
        }
        return selectTermoAssentimento;
    }

    // Method To Update Particular Termo_assentimento Details In Database
    public void updateTermoAssentimentoRecordbyId(long cpf) {
        // System.out.println("Calling updateTermoAssentimentoRecordbyId(" + cpf + ") Method To Update Termo_assentimento Record");
        dbObj = new DatabaseOperations();
        Termo_assentimento termoAssentimento = dbObj.searchOneTermoAssentimento(cpf);
        if (termoAssentimento != null) {
            if (getFile() != null) {
                termoAssentimento.setPdf(getFile());
                termoAssentimento.setFilename(getFile().getFileName());
                termoAssentimento.setData();
            }
            dbObj.updateTermoAssentimentoRecord(termoAssentimento);
        } else {
            if (getFile() != null) {
                saveTermoAssentimentoRecordbyId(cpf);
            }
        }
        reset();
    }

    public void updateTermoAssentimentoDetails() {
        // System.out.println("Calling updateTermoAssentimentoDetails() Method To Update Termo_assentimento Record");
        dbObj = new DatabaseOperations();
        dbObj.updateTermoAssentimentoRecord(this);
    }
    // Method To Fetch All From The Database
    public List<Termo_assentimento> getAllTermoAssentimentoAsList() {
        if (getTermoAssentimentoAll() != null)
            return getTermoAssentimentoAll();
        // System.out.println("Calling getAllTermoAssentimentoAsList() Method To Fetch Termo_assentimento Record");
        dbObj = new DatabaseOperations();
        setTermoAssentimentoAll(dbObj.retrieveAllTermoAssentimentoAsList());
        return getTermoAssentimentoAll();
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload(FileUploadEvent event) throws IOException {
        setFile(event.getFile());
        if (getFile() != null) {
            setFilename(getFile().getFileName());
            message = getFilename() + " is successful uploaded.";
            System.out.println(message);
            setPdf(file);

        } else {
            message = "File not uploaded.";
            System.out.println(message);
        }
    }

    public StreamedContent download(long cpf) throws IOException {
        setFilename(null);
        setPdf("");
        searchOneTermoAssentimentobyId(cpf);
        if (getPdf() != null) {

            String filename = getFilename().toLowerCase();
            String format = "";
            if (filename.endsWith(".pdf")) {
                format = "application/pdf";
            } else if (filename.endsWith(".png")) {
                format = "image/png";
            } else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
                format = "image/jpeg";
            } else {
                format = "text/plain";
                message = "Unsupported format.";
                System.out.println(message);
            }
            InputStream stream = new ByteArrayInputStream(getPdftoDownload());
            DefaultStreamedContent dsc = DefaultStreamedContent.builder()
                    .contentType(format)
                    .name(filename)
                    .stream(() -> stream)
                    .build();
            message = getFilename() + " is successful downloaded.";
            System.out.println(message);
            reset();
            return dsc;
        } else {
            message = "File not downloaded.";
            System.out.println(message);
        }
        return null;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        }
}