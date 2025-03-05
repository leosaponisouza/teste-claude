package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table(name = "E_AMOSTRA")
public class E_amostra implements java.io.Serializable {

	private String digitalizado_por = null;
	private Date data;
	@Id
	private Integer ideam;
	private String tecnica = "FTIR";
	private String codigo_acesso = null;
	private String codigo_biosample = null;
	@ManyToOne
	private Amostra refAmostra;
	@JoinColumn(name = "idamo", nullable = false)
	private Integer idamo;

	// private UploadedFile uploaded = null; //ATENÇAO!!!!
	// private String filename = null;
	private String message;
	private List<E_amostra> idlikeset;
	private List<E_amostra> idallset;
	private boolean keep_idallset = false;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;

	public void init() {
		getAllE_amostraAsList();
		if (getE_amostraAll() != null && !isKeep_idallset()) {
			if (getE_amostraAll().isEmpty() == false) {
				setIdeam(getE_amostraAll().get(0).getIdeam());
				searchOneE_amostra();
				setKeep_idallset(true);
			}
		}
	}

	public E_amostra() {
	}

	public E_amostra(Integer idamo) {
		setIdamo(idamo);
		setData();
	}

	public void reset() {
		setData(null);
		setTecnica(null);
		setDigitalizado_por(null);
		setIdamo(0);
		setIdeam(0);
		// setFile((String) null);
		// setUploadedname(null);
		// setUploaded(null);
		setKeep_idallset(false);
		setMessage(null);
	}

	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0, "");
		nogo.add(1, null);
		nogo.add(2, 0);

		if (!nogo.contains(getIdamo()) && !nogo.contains(getData()) && !nogo.contains(getTecnica()))
			return true;
		return false;
	}

	public Integer getIdamo() {
		return this.idamo;
	}

	public void setIdamo(Integer idamo) {
		this.idamo = idamo;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	/*
	 * public byte[] getFiletoDownload() {
	 * return Base64.getDecoder().decode(getFile().trim());
	 * }
	 * 
	 * public void setFile(String filecontent) {
	 * this.file = filecontent;
	 * }
	 * 
	 * public void setFile(UploadedFile uploaded) {
	 * byte[] byte_filecontent = uploaded.getContents();
	 * setFile(Base64.getEncoder().encodeToString(byte_filecontent));
	 * }
	 */

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setData() {
		setData(new java.sql.Date(System.currentTimeMillis()));
	}

	public List<E_amostra> getE_amostraSet() {
		return idlikeset;
	}

	public void setE_amostraSet(List<E_amostra> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public List<E_amostra> getE_amostraAll() {
		return idallset;
	}

	public void setE_amostraAll(List<E_amostra> e_amostraAll) {
		idallset = e_amostraAll;
	}

	/*
	 * public String getUploadedname() {
	 * return filename;
	 * }
	 * 
	 * public void setUploadedname(String filename) {
	 * this.filename = filename;
	 * }
	 */

	public String getDigitalizado_por() {
		return digitalizado_por;
	}

	public void setDigitalizado_por(String digitalizado_por) {
		this.digitalizado_por = digitalizado_por;
	}

	public Integer getIdeam() {
		return ideam;
	}

	public void setIdeam(Integer ideam) {
		this.ideam = ideam;
	}

	public String getTecnica() {
		return tecnica;
	}

	public void setTecnica(String tecnica) {
		this.tecnica = tecnica;
	}

	public String getCodigo_acesso() {
		return codigo_acesso;
	}

	public void setCodigo_acesso(String codigo_acesso) {
		this.codigo_acesso = codigo_acesso;
	}

	public String getCodigo_biosample() {
		return codigo_biosample;
	}

	public void setCodigo_biosample(String codigo_biosample) {
		this.codigo_biosample = codigo_biosample;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	// Method To Add New E_amostra Details In Database
	public void saveE_amostraRecord() {
		// System.out.println("Calling saveE_amostraRecord() Method To Save E_amostra
		// Record");
		setData();
		dbObj = new DatabaseOperations();
		dbObj.addE_amostraInDb(this);
		setKeep_idallset(false);
	}

	public void saveE_amostraRecordbyId(Integer idamo) {
		if (idamo > 0) {
			// System.out.println("Calling saveE_amostraRecordbyId("+ idamo +") Method To
			// Save E_amostra Record");
			setIdamo(idamo);
			setData();
			dbObj = new DatabaseOperations();
			dbObj.addE_amostraInDb(this);
			setKeep_idallset(false);
		}
	}

	// Method To Delete A Particular E_amostra Record From The Database
	public void deleteE_amostraRecord() {
		// System.out.println("Calling deleteE_amostraRecord() Method To Delete
		// E_amostra Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteE_amostraInDb(getIdamo());
		setKeep_idallset(false);
	}

	public void deleteE_amostraRecordbyId(Integer p_ideam) {
		// System.out.println("Calling deleteE_amostraRecord() Method To Delete
		// E_amostra Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteE_amostraInDb(p_ideam);
		setKeep_idallset(false);
	}
	// Method To Fetch Particular E_amostra Details From The Database

	public List<E_amostra> searchAnE_amostraSetbyAmostra(Integer idamo) {
		if (idamo != null) {
			if (idamo.intValue() > 0) {
				setIdamo(idamo);
				return searchAnE_amostraSet();
			}
		}
		return null;
	}

	public List<E_amostra> searchAnE_amostraSet() {
		// System.out.println("Calling searchAnE_amostraSet() Method Details For
		// E_amostra Idamo?= " + getIdamo());
		setE_amostraSet((new DatabaseOperations()).searchAnE_amostraSet(getIdamo()));
		// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: File=" +
		// getFile());
		return getE_amostraSet();
	}

	public E_amostra searchOneE_amostra() {
		E_amostra selectE_amostra = null;
		if (getIdeam().intValue() > 0) {
			// System.out.println("Calling searchOneE_amostra() Method Details For E_amostra
			// Idamo?= " + getIdamo());
			dbObj = new DatabaseOperations();
			selectE_amostra = dbObj.searchOneE_amostra(getIdeam());
			if (selectE_amostra != null) {
				setIdeam(selectE_amostra.getIdeam());
				setDigitalizado_por(selectE_amostra.getDigitalizado_por());
				setData(selectE_amostra.getData());
				// setFile(selectE_amostra.getFile());
				setTecnica(selectE_amostra.getTecnica());
				setCodigo_acesso(selectE_amostra.getCodigo_acesso());
				setCodigo_biosample(selectE_amostra.getCodigo_biosample());
				setIdamo(selectE_amostra.getIdamo());
				// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: File=" +
				// getFile() );
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectE_amostra;
	}

	public E_amostra searchOneE_amostrabyId(Integer p_ideam) {
		E_amostra selectE_amostra = null;

		if (p_ideam == null)
			return null;
		// setUploadedname(new String());
		if (p_ideam.intValue() > 0) {
			System.out.println("Calling searchOneE_amostrabyId() Method Details For E_amostra Idamo?= " + p_ideam);
			dbObj = new DatabaseOperations();
			selectE_amostra = dbObj.searchOneE_amostra(p_ideam);
			if (selectE_amostra != null) {
				setIdeam(selectE_amostra.getIdeam());
				setDigitalizado_por(selectE_amostra.getDigitalizado_por());
				setData(selectE_amostra.getData());
				// setFile(selectE_amostra.getFile());
				setTecnica(selectE_amostra.getTecnica());
				setCodigo_acesso(selectE_amostra.getCodigo_acesso());
				setCodigo_biosample(selectE_amostra.getCodigo_biosample());
				setIdamo(selectE_amostra.getIdamo());
				// System.out.println("Fetched Idamo? " + getIdamo() + " Details Are: File=" +
				// getFile() );
				setMessage(null);
			} else {
				reset();
				setMessage("Identificador não encontrado");
				/*
				 * if (getUploaded() == null)
				 * setUploadedname("Inexistente");
				 * else
				 * setUploadedname(getUploaded().getFileName());
				 */
			}
		}
		return selectE_amostra;
	}

	public List<E_amostra> searchE_amostrasbyIdAmostra(Integer p_idamo) {
		List<E_amostra> E_amostras = null;

		if (p_idamo == null)
			return null;
		// setUploadedname(new String());
		if (p_idamo > 0) {
			// System.out.println("Calling searchE_amostrasbyIdAmostra Method Details For
			// E_amostra Idamo?= " + p_idamo);
			dbObj = new DatabaseOperations();
			E_amostras = dbObj.searchE_amostrasbyIdAmostra(p_idamo);
			if (E_amostras != null) {
				if (!E_amostras.isEmpty()) {
					setMessage(null);
					// System.out.println("Fetched Idamo? " + E_amostras.get(0).getIdeam() + "
					// Details Are: File=" + E_amostras.get(0).getFile() );
				} else {
					reset();
					setMessage("Identificador não encontrado");
					// setUploadedname("Inexistente");
				}
			} else {
				reset();
				setMessage("Identificador não encontrado");
				/*
				 * if (getUploaded() == null)
				 * setUploadedname("Inexistente");
				 * else
				 * setUploadedname(getUploaded().getFileName());
				 */
			}
		}
		return E_amostras;
	}

	// Method To Update Particular E_amostra Details In Database

	public void addE_amostraRecordbyId(Integer idamo, String Tecnica, String Digitalizado_por) {
		System.out.println("Calling addE_amostraRecordbyId(" + idamo + ") Method To insert E_amostra Record");
		setTecnica(Tecnica);
		setDigitalizado_por(Digitalizado_por);
		setIdamo(idamo);
		addE_amostraRecordbyId(idamo);
		reset();
	}

	public void addE_amostraRecordbyId(Integer idamo) {
		// System.out.println("Calling addE_amostraRecordbyId("+ idamo +") Method To
		// insert E_amostra Record");
		// dbObj = new DatabaseOperations();
		/*
		 * if (getUploaded() != null) {
		 * }
		 */
		saveE_amostraRecordbyId(idamo);
		reset();
	}

	public void updateE_amostraDetails() {
		// System.out.println("Calling updateE_amostraDetails() Method To Update
		// E_amostra Record");
		dbObj = new DatabaseOperations();
		dbObj.updateE_amostraRecord(this);
	}

	// Method To Fetch All From The Database
	public List<E_amostra> getAllE_amostraAsList() {
		if (getE_amostraAll() != null && isKeep_idallset())
			return getE_amostraAll();
		// System.out.println("Calling getAllE_amostraAsList() Method To Fetch
		// E_amostras Record");
		dbObj = new DatabaseOperations();
		setE_amostraAll(dbObj.retrieveAllE_amostraAsList());
		return getE_amostraAll();
	}

	/*
	 * public UploadedFile getUploaded() {
	 * return uploaded;
	 * }
	 * 
	 * public void setUploaded(UploadedFile uploaded) {
	 * this.uploaded = uploaded;
	 * }
	 * 
	 * public void upload(FileUploadEvent event) throws IOException {
	 * setUploaded(event.getFile());
	 * if (getUploaded() != null) {
	 * setUploadedname(getUploaded().getFileName());
	 * message = getUploadedname() + " is successful uploaded.";
	 * System.out.println(message);
	 * setFile(getUploaded());
	 * } else {
	 * message = "File not uploaded.";
	 * System.out.println(message);
	 * }
	 * }
	 */

	/*
	 * public StreamedContent download(Integer ideam) throws IOException {
	 * 
	 * setFile("");
	 * if (searchOneE_amostrabyId(ideam) == null)
	 * return null;
	 * setUploadedname("sample.csv");
	 * if (getFile() != null) {
	 * InputStream stream = new ByteArrayInputStream(getFiletoDownload());
	 * DefaultStreamedContent dsc = new DefaultStreamedContent(stream,
	 * "application/csv",
	 * "sample" + getIdamo() + "." + getIdeam() + ".csv");
	 * message = "sample" + getIdamo() + "." + getIdeam() + ".csv" +
	 * " is successful downloaded.";
	 * System.out.println(message);
	 * return dsc;
	 * } else {
	 * message = "File not downloaded.";
	 * System.out.println(message);
	 * }
	 * return null;
	 * }
	 */
}
