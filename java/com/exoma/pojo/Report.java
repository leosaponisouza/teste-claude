package com.exoma.pojo;


import com.exoma.dao.ReportOperations;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

@SessionScoped
@Named
public class Report implements Serializable {

	private List<String> projetos;
	private List<String> selected_projetos;
	private List<String> resultados;
	private List<String> selected_resultados;
	private List<String> estados;
	private List<String> selected_estados;
	private List<String> cidades;
	private List<String> selected_cidades;
	private List<String> lotes;
	private List<String> selected_lotes;
	private List<Character> selected_sexos;
	private List<Character> sexos;
	private Integer idade_min;
	private Integer idade_max;
	private boolean keep = false;
	private List<E_amostra> result;
	private StreamedContent alle_amostra_zipfile;
	private String finalconds;

	public Report() {
	}

	public void init() {
		if (!isKeep()) {
			ReportOperations dbObj;
			dbObj = new ReportOperations();
			setProjetos(dbObj.getdistinctvalues("projeto", "titulo"));
			setResultados(dbObj.getdistinctvalues("diagnostico", "resultado"));
			setEstados(dbObj.getdistinctvalues("participante", "estado"));
			setCidades(dbObj.getdistinctvalues("participante", "cidade"));
			setLotes(dbObj.getdistinctvalues("amostra", "lote"));
			List<Character> sexo = new ArrayList<Character>();
			sexo.add(0, 'M');
			sexo.add(1, 'F');
			setSexos(sexo);
			setFinalconds();
			setKeep(true);
		}
	}

	public String createcondition(String colum_name, List<String> source) {
		String cond = "";
		if (source != null && colum_name != null) {
			if (!source.isEmpty()) {
				cond = cond + " " + colum_name + " in (";
				for (int i = 0; i < source.size(); i++) {
					cond = cond + "'" + source.get(i) + "'";
					if (i < source.size() - 1)
						cond = cond + ",";
				}
				cond = cond + ") ";
			}
		}
		return cond;
	}

	public String createcondition_sexo(List<Character> source) {
		String cond = "";
		if (source != null) {
			if (!source.isEmpty()) {
				cond = cond + " sexo in (";
				for (int i = 0; i < source.size(); i++) {
					cond = cond + "'" + source.get(i) + "'";
					if (i < source.size() - 1)
						cond = cond + ",";
				}
				cond = cond + ") ";
			}
		}
		return cond;
	}

	public String createcondition_idade(Integer min, Integer max) {
		String cond = "";
		LocalDate mindate;
		LocalDate maxdate;

		if (min != null && max != null) {
			mindate = LocalDate.now().minusYears(min);
			maxdate = LocalDate.now().minusYears(max);
			cond = " nascimento between '" + maxdate.toString() + "' and '" + mindate.toString() + "'";
		}
		if (min != null && max == null) {
			mindate = LocalDate.now().minusYears(min);
			cond = " nascimento <= '" + mindate.toString() + "'";
		}
		if (min == null && max != null) {
			maxdate = LocalDate.now().minusYears(max);
			cond = " nascimento >= '" + maxdate.toString() + "'";
		}
		return cond;
	}

	public String createfinalcondition(List<String> source) {
		String cond = "";
		if (source != null) {
			if (!source.isEmpty()) {
				for (int i = 0; i < source.size(); i++) {
					cond = cond + " " + source.get(i) + " ";
					if (i < source.size() - 1)
						cond = cond + " and ";
				}
			}
		}
		return cond;
	}

	public List<com.exoma.pojo.E_amostra> searchAnE_amostraSet() {
		ReportOperations dbObj = new ReportOperations();
		String projeto_cond = createcondition("titulo", getSelected_projetos());
		String resultado_cond = createcondition("resultado", getSelected_resultados());
		String estado_cond = createcondition("estado", getSelected_estados());
		String lote_cond = createcondition("lote", getSelected_lotes());
		String cidade_cond = createcondition("cidade", getSelected_cidades());
		String sexo_cond = createcondition_sexo(getSelected_sexos());
		String idade_cond = createcondition_idade(getIdade_min(), getIdade_max());

		List<String> allconds = new ArrayList<String>();
		int i = 0;
		if (projeto_cond != null)
			if (!projeto_cond.isBlank())
				allconds.add(i++, projeto_cond);
		if (resultado_cond != null)
			if (!resultado_cond.isBlank())
				allconds.add(i++, resultado_cond);
		if (estado_cond != null)
			if (!estado_cond.isBlank())
				allconds.add(i++, estado_cond);
		if (lote_cond != null)
			if (!lote_cond.isBlank())
				allconds.add(i++, lote_cond);
		if (cidade_cond != null)
			if (!cidade_cond.isBlank())
				allconds.add(i++, cidade_cond);
		if (sexo_cond != null)
			if (!sexo_cond.isBlank())
				allconds.add(i++, sexo_cond);
		if (idade_cond != null)
			if (!idade_cond.isBlank())
				allconds.add(i++, idade_cond);
		setFinalconds(allconds);
		setResult(dbObj.searchAnE_amostraSet(createfinalcondition(allconds)));

		return (getResult());
	}

	private byte[] zipBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		if (getResult() != null) {
			try {
				/*
				 * for (E_amostra eamo : getResult()) {
				 * if (eamo.getFile() != null) {
				 * String idamo5 = String.format("%05d", eamo.getIdamo());
				 * String ideam3 = String.format("%08d", eamo.getIdeam());
				 * ZipEntry entry = new ZipEntry("sample" + idamo5 + "_digital" + ideam3 +
				 * ".csv");
				 * zos.putNextEntry(entry);
				 * zos.write(eamo.getFiletoDownload());
				 * zos.setComment(getFinalconds());
				 * }
				 * }
				 */

				setFinalconds();
				zos.closeEntry();
				zos.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	public List<String> getEstados() {
		return estados;
	}

	public void setEstados(List<String> estados) {
		this.estados = estados;
	}

	public List<String> getCidades() {
		return cidades;
	}

	public void setCidades(List<String> cidades) {
		this.cidades = cidades;
	}

	public List<String> getLotes() {
		return lotes;
	}

	public void setLotes(List<String> lotes) {
		this.lotes = lotes;
	}

	public List<Character> getSexos() {
		return sexos;
	}

	public void setSexos(List<Character> sexos) {
		this.sexos = sexos;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	public List<String> getSelected_estados() {
		return selected_estados;
	}

	public void setSelected_estados(List<String> selected_estados) {
		this.selected_estados = selected_estados;
	}

	public List<String> getSelected_cidades() {
		return selected_cidades;
	}

	public void setSelected_cidades(List<String> selected_cidades) {
		this.selected_cidades = selected_cidades;
	}

	public List<String> getSelected_lotes() {
		return selected_lotes;
	}

	public void setSelected_lotes(List<String> selected_lotes) {
		this.selected_lotes = selected_lotes;
	}

	public List<Character> getSelected_sexos() {
		return selected_sexos;
	}

	public void setSelected_sexos(List<Character> selected_sexos) {
		this.selected_sexos = selected_sexos;
	}

	public Integer getIdade_min() {
		return idade_min;
	}

	public void setIdade_min(Integer idade_min) {
		this.idade_min = idade_min;
	}

	public Integer getIdade_max() {
		return idade_max;
	}

	public void setIdade_max(Integer idade_max) {
		this.idade_max = idade_max;
	}

	public List<E_amostra> getResult() {
		return result;
	}

	public void setResult(List<E_amostra> result) {
		this.result = result;
	}

	public List<String> getResultados() {
		return resultados;
	}

	public void setResultados(List<String> resultados) {
		this.resultados = resultados;
	}

	public List<String> getSelected_resultados() {
		return selected_resultados;
	}

	public void setSelected_resultados(List<String> selected_resultados) {
		this.selected_resultados = selected_resultados;
	}

	public List<String> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<String> projetos) {
		this.projetos = projetos;
	}

	public List<String> getSelected_projetos() {
		return selected_projetos;
	}

	public void setSelected_projetos(List<String> selected_projetos) {
		this.selected_projetos = selected_projetos;
	}

	public StreamedContent getAlle_amostra_zipfile() {
		return alle_amostra_zipfile;
	}

	public void setAlle_amostra_zipfile(StreamedContent alle_amostra_zipfile) {
		this.alle_amostra_zipfile = alle_amostra_zipfile;
	}

	public void setAlle_amostra_zipfile() {
		ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes());
		InputStream stream = bais;
		DefaultStreamedContent dsc = DefaultStreamedContent.builder()
				.contentType("application/zip")
				.name("exoma.zip")
				.stream(() -> stream)
				.build();
		this.alle_amostra_zipfile = dsc;
	}

	public String getFinalconds() {
		return finalconds;
	}

	public void setFinalconds(String finalconds) {
		this.finalconds = finalconds;
	}

	public void setFinalconds() {
		this.finalconds = "Filtros de download:\n";
	}

	public void setFinalconds(List<String> conds) {
		setFinalconds();
		if (conds != null) {
			for (int i = 0; i < conds.size(); i++) {
				System.out.println(conds.get(i));
				this.finalconds = this.finalconds + conds.get(i) + "\n";
			}
		}
	}

}
