package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
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

@Named("termo")
@SessionScoped
@Entity
@Table(name = "TERMO")
public class Termo implements java.io.Serializable {

	private String pdf = null;
	private String filename = null;
	private Date data;
	@ManyToOne
	@JoinColumn(name = "cpf", nullable = false)
	private long cpf;

	private UploadedFile file = null;
	private String message;
	private List<Termo> idlikeset;
	private List<Termo> idallset;
	public static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public void init() {
		getAllTermoAsList();
		if (getTermoAll() != null) {
			if (getTermoAll().isEmpty() == false) {
				setCpf(getTermoAll().get(0).getCpf());
				searchOneTermo();
			}
		}
	}

	public Termo() {
	}

	public Termo(long cpf) {
		setCpf(cpf);
		setData();
	}

	public void reset() {
		setData(null);
		setCpf(0);
		setPdf((String) null);
		setMessage(null);
		setFilename(null);
		setFile(null);
	}

	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0, "");
		nogo.add(1, null);
		nogo.add(2, 0);

		if (!nogo.contains(getCpf()) && !nogo.contains(getPdf())
				&& !nogo.contains(getData()) && !nogo.contains(getFilename()))
			return true;
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

	public List<Termo> getTermoSet() {
		return idlikeset;
	}

	public void setTermoSet(List<Termo> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public List<Termo> getTermoAll() {
		return idallset;
	}

	public void setTermoAll(List<Termo> termoAll) {
		idallset = termoAll;
	}

	// Method To Add New Termo Details In Database
	public void saveTermoRecord() {
		// System.out.println("Calling saveTermoRecord() Method To Save Termo Record");
		setData();
		dbObj = new DatabaseOperations();
		dbObj.addTermoInDb(this);
	}

	public void saveTermoRecordbyId(long cpf) {
		if (cpf > 0) {
			// System.out.println("Calling saveTermoRecordbyId("+ cpf +") Method To Save
			// Termo Record");
			setCpf(cpf);
			setData();
			dbObj = new DatabaseOperations();
			dbObj.addTermoInDb(this);

		}
	}

	// Method To Delete A Particular Termo Record From The Database
	public void deleteTermoRecord() {
		// System.out.println("Calling deleteTermoRecord() Method To Delete Termo
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteTermoInDb(getCpf());
	}

	public void deleteTermoRecordbyId(long p_cpf) {
		// System.out.println("Calling deleteTermoRecord() Method To Delete Termo
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteTermoInDb(p_cpf);
	}

	// Method To Fetch Particular Termo Details From The Database
	public List<Termo> searchAnTermoSet() {
		// System.out.println("Calling searchAnTermoSet() Method Details For Termo Cpf?=
		// " + getCpf());
		dbObj = new DatabaseOperations();
		setTermoSet(dbObj.searchAnTermoSet(getCpf()));
		// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" +
		// getPdf());
		return getTermoSet();
	}

	public Termo searchOneTermo() {
		Termo selectTermo = null;
		if (getCpf() != 0) {
			// System.out.println("Calling searchOneTermo() Method Details For Termo Cpf?= "
			// + getCpf());
			dbObj = new DatabaseOperations();
			selectTermo = dbObj.searchOneTermo(getCpf());
			if (selectTermo != null) {
				setData(selectTermo.getData());
				setCpf(selectTermo.getCpf());
				setPdf(selectTermo.getPdf());
				setFilename(selectTermo.getFilename());
				// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" +
				// getPdf() );
			}
		}
		return selectTermo;
	}

	public Termo searchOneTermobyId(long p_cpf) {
		Termo selectTermo = null;

		setFilename(new String());
		if (p_cpf != 0) {
			// System.out.println("Calling searchOneTermobyId() Method Details For Termo
			// Cpf?= " + p_cpf);
			dbObj = new DatabaseOperations();
			selectTermo = dbObj.searchOneTermo(p_cpf);
			if (selectTermo != null) {
				setData(selectTermo.getData());
				setCpf(selectTermo.getCpf());
				setPdf(selectTermo.getPdf());
				setFilename(selectTermo.getFilename());
				// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Pdf=" +
				// getPdf() );
			} else if (getFile() == null)
				setFilename("Inexistente");
			else
				setFilename(getFile().getFileName());
		}
		return selectTermo;
	}

	// Method To Update Particular Termo Details In Database
	public void updateTermoRecordbyId(long cpf) {
		// System.out.println("Calling updateTermoRecordbyId("+ cpf +") Method To Update
		// Termo Record");
		dbObj = new DatabaseOperations();
		Termo termo = dbObj.searchOneTermo(cpf);
		if (termo != null) {
			if (getFile() != null) {
				termo.setPdf(getFile());
				termo.setFilename(getFile().getFileName());
				termo.setData();
			}
			dbObj.updateTermoRecord(termo);
		} else {
			if (getFile() != null) {
				saveTermoRecordbyId(cpf);
			}
		}
		reset();
	}

	public void updateTermoDetails() {
		// System.out.println("Calling updateTermoDetails() Method To Update Termo
		// Record");
		dbObj = new DatabaseOperations();
		dbObj.updateTermoRecord(this);
	}

	// Method To Fetch All From The Database
	public List<Termo> getAllTermoAsList() {
		if (getTermoAll() != null)
			return getTermoAll();
		// System.out.println("Calling getAllTermoAsList() Method To Fetch Termos
		// Record");
		dbObj = new DatabaseOperations();
		setTermoAll(dbObj.retrieveAllTermoAsList());
		return getTermoAll();
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
		searchOneTermobyId(cpf);
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
