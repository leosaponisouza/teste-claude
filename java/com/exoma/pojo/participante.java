package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import com.exoma.dao.RespostaDcDAO;
import com.exoma.dto.PerguntaRespostaDTO;
import com.exoma.dto.PerguntaRespostaGroup;
import com.exoma.dto.RespostaDTO;
import com.exoma.model.PerguntaDc;
import com.exoma.model.RespostaDc;
import com.exoma.service.pergunta.PerguntaService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.sql.Date;
import java.util.*;

@Named
@SessionScoped
@Entity
@Table(name = "PARTICIPANTE")
public class participante implements java.io.Serializable {

	private Date nascimento;
	private long cpf;
	private String num_prontuario;
	private String nome;
	private String logradouro;
	private String cidade;
	private String estado;
	private char sexo;
	private int cep;
	private String bairro;
	private int numero;
	private String complemento;
	private int tipo = 1;
	private List<participante> idlikeset;
	private List<participante> idallset;
	private boolean keep_idallset = false;
	private String tipo_participante_desc;
	private List<String> Alltipo_participante_desc;
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;
	private String message;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "refparticipante")
	private String pdf;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "participante")
	private Set<RespostaDc> respostas = new HashSet<>();
	private List<PerguntaRespostaGroup> perguntasRespostasGroups;
	private participante_contato indcon;
	private Termo termo;
	private Termo_assentimento termo_assentimento;

	public void init() {
		setTermo(new Termo());
		setTermoAssentimento(new Termo_assentimento());
		setIndcon(new participante_contato());
		getAllparticipanteAsList();
		if (getparticipanteAll() != null && !isKeep_idallset()) {
			if (getparticipanteAll().isEmpty() == false) {
				setCpf(getparticipanteAll().get(0).getCpf());
				searchOneparticipante();
				setKeep_idallset(true);
			}
		}
		loadPerguntasRespostas();
	}

	public participante() {
	}

	public void reset() {
		setNascimento(null);
		setCpf(0);
		setNumProntuario(null);
		setNome(null);
		setLogradouro(null);
		setCidade(null);
		setEstado(null);
		setSexo('M');
		setCep(0);
		setBairro(null);
		setNumero(0);
		setComplemento(null);
		if (getIndcon() != null)
			getIndcon().reset();
		setRespostas(null);
		setPerguntasRespostasGroups(null);
		loadPerguntasRespostas();
	}

	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0, "");
		nogo.add(1, null);
		nogo.add(2, 0);

		List<Object> gosexo = new ArrayList<Object>();
		gosexo.add(0, 'M');
		gosexo.add(1, 'm');
		gosexo.add(2, 'f');
		gosexo.add(3, 'F');

		if (!nogo.contains(getNascimento()) && !nogo.contains(getCpf()) &&
				!nogo.contains(getNome()) && !nogo.contains(getCidade()) &&
				!nogo.contains(getEstado()) && gosexo.contains(getSexo()) &&
				!nogo.contains(tipo))
			return true;
		return false;
	}

	public long getCpf() {
		return this.cpf;
	}

	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	public String getNumProntuario() {return this.num_prontuario;}

	public void setNumProntuario(String num_prontuario) {this.num_prontuario = num_prontuario;}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCidade() {
		return this.cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public List<participante> getparticipanteSet() {
		return idlikeset;
	}

	public void setparticipanteSet(List<participante> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public int getCep() {
		return cep;
	}

	public void setCep(int cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public int getTipo() {
		return tipo;
	}

	public String getTipo(int p_tipo) {
		if (p_tipo == 1)
			setTipo_participante_desc("Humano");
		else
			setTipo_participante_desc("Cobaia");

		return getTipo_participante_desc();
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public void setTipo(String p_tipo) {
		if (p_tipo.equals("Humano"))
			setTipo(1);
		else
			setTipo(2);
	}

	public List<participante> getparticipanteAll() {
		return idallset;
	}

	public void setparticipanteAll(List<participante> participanteAll) {
		idallset = participanteAll;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}

	public participante_contato getIndcon() {
		return indcon;
	}

	public void setIndcon(participante_contato indcon) {
		this.indcon = indcon;
	}

	public Termo getTermo() {
		return termo;
	}

	public void setTermo(Termo termo) {
		this.termo = termo;
	}

	public Termo_assentimento getTermoAssentimento() {
		return termo_assentimento;
	}

	public void setTermoAssentimento(Termo_assentimento termo_assentimento) {
		this.termo_assentimento = termo_assentimento;
	}

	public String getTipo_participante_desc() {
		return tipo_participante_desc;
	}

	public void setTipo_participante_desc(String tipo_participante_desc) {
		this.tipo_participante_desc = tipo_participante_desc;
	}

	public List<String> getAlltipo_participante_desc() {
		Alltipo_participante_desc = new ArrayList<String>();
		Alltipo_participante_desc.add(0, "Humano");
		Alltipo_participante_desc.add(1, "Cobaia");
		return Alltipo_participante_desc;
	}

	public void setAlltipo_participante_desc(List<String> alltipo_participante_desc) {
		Alltipo_participante_desc = alltipo_participante_desc;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// Method To Add New participante Details In Database
	public void saveparticipanteRecord(Termo termo, participante_contato indcon,
									   Termo_assentimento termo_assentimento) {
		if (termo == null || termo.getFile() == null) {
			String message = "Não é possível adicionar o participante sem um termo de consentimento";
			System.out.println(message);
			this.setMessage(message);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", message));
			return;
		}

		try {
			// Clear existing responses
			if (this.respostas != null) {
				this.respostas.clear();
			} else {
				this.respostas = new HashSet<>();
			}

			// Collect responses from perguntasRespostasGroups
			for (PerguntaRespostaGroup group : perguntasRespostasGroups) {
				for (PerguntaRespostaDTO prDTO : group.getPerguntasRespostas()) {
					RespostaDTO resposta = prDTO.getResposta();
					if (resposta.getResposta() != null && !resposta.getResposta().isEmpty()) {
						RespostaDc respostaDc = new RespostaDc();
						respostaDc.setParticipante(this);
						respostaDc.setPergunta(prDTO.getPergunta());
						respostaDc.setDescricao(resposta.getDescricao());
						respostaDc.setResposta(resposta.getResposta());
						this.respostas.add(respostaDc);
					}
				}
			}

			dbObj = new DatabaseOperations();
			dbObj.addparticipanteInDb(this);  // Salva o participante no banco de dados
			// Salva termos e contatos relacionados
			termo.saveTermoRecordbyId(getCpf());
			termo_assentimento.saveTermoAssentimentoRecordbyId(getCpf());
			indcon.uploadContatos(getCpf());

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Participante e respostas salvos com sucesso!"));

			// Limpa os dados após o salvamento
			this.reset();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("termo", new Termo());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar o participante.", e.getMessage()));
			e.printStackTrace();
		}
	}


	// Method To Delete A Particular participante Record From The Database
	public void deleteparticipanteRecord() {
		// System.out.println("Calling deleteparticipanteRecord() Method To Delete
		// participante Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteparticipanteInDb(getCpf());
		setKeep_idallset(false);
	}

	public void deleteparticipanteRecordbyId(long p_cpf) {
		// System.out.println("Calling deleteparticipanteRecord() Method To Delete
		// participante Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteparticipanteInDb(p_cpf);
		setKeep_idallset(false);
	}

	// Method To Fetch Particular participante Details From The Database
	public List<participante> searchAnparticipanteSet() {
		// System.out.println("Calling searchAnparticipanteSet() Method Details For
		// participante Cpf?= " + getCpf());
		dbObj = new DatabaseOperations();
		setparticipanteSet(dbObj.searchAnparticipanteSet(getCpf()));
		// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Nome=" +
		// getNome() + ", Cidade=" + getCidade());
		return getparticipanteSet();
	}

	public participante searchOneparticipante() {
		participante selectparticipante = null;
		if (getCpf() != 0) {
			// System.out.println("Calling searchOneparticipante() Method Details For
			// participante Cpf?= " + getCpf());
			dbObj = new DatabaseOperations();
			selectparticipante = dbObj.searchOneparticipante(getCpf());
			if (selectparticipante != null) {
				setNascimento(selectparticipante.getNascimento());
				setCpf(selectparticipante.getCpf());
				setNumProntuario(selectparticipante.getNumProntuario());
				setNome(selectparticipante.getNome());
				setLogradouro(selectparticipante.getLogradouro());
				setCidade(selectparticipante.getCidade());
				setEstado(selectparticipante.getEstado());
				setSexo(selectparticipante.getSexo());
				setCep(selectparticipante.getCep());
				setBairro(selectparticipante.getBairro());
				setNumero(selectparticipante.getNumero());
				setComplemento(selectparticipante.getComplemento());
				setTipo(selectparticipante.getTipo());
				getTipo(selectparticipante.getTipo());
				// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Nome=" +
				// getNome() + ", Cidade=" + getCidade());
				setTermo(new Termo());
				getTermo().searchOneTermobyId(getCpf());
				setTermoAssentimento(new Termo_assentimento());
				getTermoAssentimento().searchOneTermoAssentimentobyId(getCpf());
				setIndcon(new participante_contato());
				getIndcon().searchOneparticipante_contatobyId(getCpf());
				setMessage(null);
				loadPerguntasRespostas();
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectparticipante;
	}
	private void loadPerguntasRespostas() {
		// Initialize the list
		perguntasRespostasGroups = new ArrayList<>();

		// Load active questions
		PerguntaService perguntaService = new PerguntaService();
		List<PerguntaDc> perguntasAtivas = perguntaService.findAtivas();

		// Load existing responses only if CPF is set
		Map<Integer, RespostaDc> respostasExistentesMap = new HashMap<>();
		if (this.cpf != 0) {
			RespostaDcDAO respostaDcDAO = new RespostaDcDAO();
			List<RespostaDc> respostasExistentes = respostaDcDAO.findByParticipanteCpf(this.cpf);

			// Map existing responses by question ID
			for (RespostaDc resposta : respostasExistentes) {
				respostasExistentesMap.put(resposta.getPergunta().getId(), resposta);
			}
		}

		// Group questions and responses by type
		Map<String, PerguntaRespostaGroup> groupMap = new LinkedHashMap<>();
		for (PerguntaDc pergunta : perguntasAtivas) {
			String tipo = pergunta.getTipo();

			// Get or create the group
			PerguntaRespostaGroup group = groupMap.computeIfAbsent(tipo, PerguntaRespostaGroup::new);

			// Create RespostaDTO
			RespostaDTO respostaDTO = new RespostaDTO();
			respostaDTO.setPerguntaDc(pergunta);

			// If existing response, set values; else, set defaults
			RespostaDc respostaExistente = respostasExistentesMap.get(pergunta.getId());
			if (respostaExistente != null) {
				respostaDTO.setResposta(respostaExistente.getResposta());
				respostaDTO.setDescricao(respostaExistente.getDescricao());
			} else {
				respostaDTO.setResposta(""); // Default value
				respostaDTO.setDescricao("");
			}

			// Create PerguntaRespostaDTO and add to group
			PerguntaRespostaDTO prDTO = new PerguntaRespostaDTO(pergunta, respostaDTO);
			group.addPerguntaResposta(prDTO);
		}

		// Convert map values to list
		perguntasRespostasGroups.addAll(groupMap.values());
	}
	public participante searchOneparticipantebyId(long p_cpf) {
		participante selectparticipante = null;
		if (p_cpf != 0) {
			reset();
			// System.out.println("Calling searchOneparticipantebyId() Method Details For
			// participante Cpf?= " + p_cpf);
			dbObj = new DatabaseOperations();
			selectparticipante = dbObj.searchOneparticipante(p_cpf);
			if (selectparticipante != null) {
				setNascimento(selectparticipante.getNascimento());
				setCpf(selectparticipante.getCpf());
				setNumProntuario(selectparticipante.getNumProntuario());
				setNome(selectparticipante.getNome());
				setLogradouro(selectparticipante.getLogradouro());
				setCidade(selectparticipante.getCidade());
				setEstado(selectparticipante.getEstado());
				setSexo(selectparticipante.getSexo());
				setCep(selectparticipante.getCep());
				setBairro(selectparticipante.getBairro());
				setNumero(selectparticipante.getNumero());
				setComplemento(selectparticipante.getComplemento());
				setTipo(selectparticipante.getTipo());
				getTipo(selectparticipante.getTipo());
				// System.out.println("Fetched Cpf? " + getCpf() + " Details Are: Nome=" +
				// getNome() + ", Cidade=" + getCidade());
				setTermo(new Termo());
				getTermo().searchOneTermobyId(getCpf());
				setTermoAssentimento(new Termo_assentimento());
				getTermoAssentimento().searchOneTermoAssentimentobyId(getCpf());
				setIndcon(new participante_contato());
				getIndcon().searchOneparticipante_contatobyId(getCpf());
				setMessage(null);
				loadPerguntasRespostas();
			} else {
				reset();
				setMessage("Identificador não encontrado");
			}
		} else {
			reset();
			setMessage("Identificador não encontrado");
		}
		return selectparticipante;
	}

	// Method To Update Particular participante Details In Database
	public void updateparticipanteDetails() {
		// System.out.println("Calling updateparticipanteDetails() Method To Update
		// participante Record");
		dbObj = new DatabaseOperations();
		dbObj.updateparticipanteRecord(this);
	}

	public void updateAllparticipanteDetails(Termo termo, participante_contato indcon,
			Termo_assentimento termo_assentimento) {
		// System.out.println("Calling updateAllparticipanteDetails() Method To Update
		// participante Record");
		dbObj = new DatabaseOperations();
		dbObj.updateparticipanteRecord(this);
		termo.updateTermoRecordbyId(getCpf());
		termo_assentimento.updateTermoAssentimentoRecordbyId(getCpf());
		indcon.updateContatos(getCpf());

		// Clear existing responses to avoid duplications
		if (this.respostas != null) {
			this.respostas.clear();
		} else {
			this.respostas = new HashSet<>();
		}

		// Collect responses from perguntasRespostasGroups
		for (PerguntaRespostaGroup group : perguntasRespostasGroups) {
			for (PerguntaRespostaDTO prDTO : group.getPerguntasRespostas()) {
				RespostaDTO resposta = prDTO.getResposta();
				if (resposta.getResposta() != null && !resposta.getResposta().isEmpty()) {
					RespostaDc respostaDc = new RespostaDc();
					respostaDc.setParticipante(this);
					respostaDc.setPergunta(prDTO.getPergunta());
					respostaDc.setDescricao(resposta.getDescricao());
					respostaDc.setResposta(resposta.getResposta());
					this.respostas.add(respostaDc);
				}
			}
		}
	}

	// Method To Fetch All From The Database
	// @PostConstruct
	public List<participante> getAllparticipanteAsList() {
		if (getparticipanteAll() != null && isKeep_idallset())
			return getparticipanteAll();
		// System.out.println("Calling getAllparticipanteAsList() Method To Fetch
		// participantes Record");
		dbObj = new DatabaseOperations();
		setparticipanteAll(dbObj.retrieveAllparticipanteAsList());
		return getparticipanteAll();
	}

	public List<PerguntaRespostaGroup> getPerguntasRespostasGroups() {
		return perguntasRespostasGroups;
	}

	public void setPerguntasRespostasGroups(List<PerguntaRespostaGroup> perguntasRespostasGroups) {
		this.perguntasRespostasGroups = perguntasRespostasGroups;
	}

	public Set<RespostaDc> getRespostas() {
		return respostas;
	}

	public void setRespostas(Set<RespostaDc> respostas) {
		this.respostas = respostas;
	}
}
