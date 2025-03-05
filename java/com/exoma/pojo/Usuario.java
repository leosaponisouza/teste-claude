package com.exoma.pojo;

import com.exoma.dao.DatabaseOperations;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Named
@SessionScoped
@Entity
@Table
public class Usuario implements java.io.Serializable {

	@Id
	private Integer idusu;
	private String email;
	private String senha;	
	private String nome;
	private Integer ativo;
	
	private String message;
	private boolean subuser=false;
	private Usuario edited=null;
	private Permissao permissao;
	private List<Usuario> idlikeset;
	private List<Usuario> idallset;
	private boolean keep_idallset=false;	
	private static DatabaseOperations dbObj;
	private static final long serialVersionUID = 1L;
	
	public void init() {
		getAllUsuarioAsList();
		if(getUsuarioAll()!=null && !isKeep_idallset() ){
			if (getUsuarioAll().isEmpty() == false) {
				setIdusu(getUsuarioAll().get(0).getIdusu());
				searchOneUsuario();
				setKeep_idallset(true);
			}
		}
	}
	
	public Usuario() { }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void reset() {
		setIdusu(0);
		setEmail(null);
		setSenha(null);		
		setNome(null);
		setAtivo(null);
		setMessage(null);		
		
	}
	
	public boolean notnull() {
		List<Object> nogo = new ArrayList<Object>();
		nogo.add(0,""); nogo.add(1,null); nogo.add(2,0);
		
		if ( !nogo.contains(getEmail()) && !nogo.contains(getSenha()) 
				&& !nogo.contains(getNome()) && !nogo.contains(getAtivo()))
			return true;
		return false;
	}	

	public Integer getIdusu() {
		return this.idusu;
	}

	public void setIdusu(Integer idusu) {
		this.idusu = idusu;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Usuario> getUsuarioSet() {
		return idlikeset;
	}

	public void setUsuarioSet(List<Usuario> idlikeset) {
		this.idlikeset = idlikeset;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public String getAtivoLabel() {
		if (getAtivo()!=null) {
			if (getAtivo().intValue()>1)
				return "Sim";
			else
				return "Não";
		}
		return null;
	}
	
	
	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public List<Usuario> getUsuarioAll() {
		return idallset;
	}

	public void setUsuarioAll(List<Usuario> UsuarioAll) {
		idallset = UsuarioAll;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isKeep_idallset() {
		return keep_idallset;
	}

	public void setKeep_idallset(boolean keep_idallset) {
		this.keep_idallset = keep_idallset;
	}
	
	public boolean isSubuser() {
		return subuser;
	}

	public void setSubuser(boolean subuser) {
		this.subuser = subuser;
	}

	public Usuario getEdited() {
		return edited;
	}

	public void setEdited(Usuario edited) {
		this.edited = edited;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	// Method To Add New Usuario Details In Database
	public void saveUsuarioRecord() {
		//System.out.println("Calling saveUsuarioRecord() Method To Save Usuario Record");
			dbObj = new DatabaseOperations();
			String cipherText = null;
			
		    IvParameterSpec ivParameterSpec = generateIv();
		    try {
				SecretKey key = getKeyFromPassword("SamplesDB","MeuAmigodoAlém");
				cipherText = encryptPasswordBased(getSenha(), key, ivParameterSpec);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}	
		    this.setSenha(cipherText);
			
			dbObj.addUsuarioInDb(this);
			setKeep_idallset(false);
	}

	// Method To Delete A Particular Usuario Record From The Database
	public void deleteUsuarioRecord() {
		//System.out.println("Calling deleteUsuarioRecord() Method To Delete Usuario Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteUsuarioInDb(getIdusu());
		setKeep_idallset(false);
	}
	
	public void deleteUsuarioRecordbyId(Integer p_idusu) {
		//System.out.println("Calling deleteUsuarioRecord() Method To Delete Usuario Record");
		dbObj = new DatabaseOperations();
		dbObj.deleteUsuarioInDb(p_idusu);
		setKeep_idallset(false);
	}	

	// Method To Fetch Particular Usuario Details From The Database
	public List<Usuario> searchAnUsuarioSet() {
		//System.out.println("Calling searchAnUsuarioSet() Method Details For Usuario Idusu?= " + getIdusu());
		dbObj = new DatabaseOperations();		
		setUsuarioSet(dbObj.searchAnUsuarioSet(getAtivo()));
		//System.out.println("Fetched Idusu? " + getIdusu() + " Details Are: Email=" + getEmail() + ", Local_coleta=" + getLocal_coleta());
		return getUsuarioSet();
	}
	
	public Usuario searchOneUsuario() {
		Usuario selectUsuario=null;
		if (getIdusu() != 0) {
			//System.out.println("Calling searchOneUsuario() Method Details For Usuario Idusu?= " + getIdusu());
			dbObj = new DatabaseOperations();		
			selectUsuario = dbObj.searchOneUsuario(getIdusu());
			if (selectUsuario!= null) {
				setIdusu(selectUsuario.getIdusu());
				setEmail(selectUsuario.getEmail());
				setSenha(selectUsuario.getSenha());				
				setNome(selectUsuario.getNome());
				setAtivo(selectUsuario.getAtivo());
				//System.out.println("Fetched Idusu? " + getIdusu() + " Details Are: Email=" + getEmail() + ", Local_coleta=" + getLocal_coleta());
				setMessage(null);
				setPermissao(new Permissao());
				getPermissao().init();
				getPermissao().searchPermissaobyIdUser(getIdusu());
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return selectUsuario;
	}
	
	public Usuario searchOneUsuariobyId(Integer p_idusu) {
		Usuario selectUsuario=null;
		if (p_idusu != 0) { 
			reset();
			//System.out.println("Calling searchOneUsuariobyId() Method Details For Usuario Idusu?= " + p_idusu);
			dbObj = new DatabaseOperations();		
			selectUsuario = dbObj.searchOneUsuario(p_idusu);
			if (selectUsuario!= null) {
				setIdusu(selectUsuario.getIdusu());
				setEmail(selectUsuario.getEmail());
				setSenha(selectUsuario.getSenha());				
				setNome(selectUsuario.getNome());
				setAtivo(selectUsuario.getAtivo());
				//System.out.println("Fetched Idusu? " + getIdusu() + " Details Are: Email=" + getEmail() + ", Local_coleta=" + getLocal_coleta());
				setMessage(null);
				setPermissao(new Permissao());
				getPermissao().init();
				getPermissao().searchPermissaobyIdUser(getIdusu());
			}else {reset(); setMessage("Identificador não encontrado");}
		}else {reset(); setMessage("Identificador não encontrado");}
		return selectUsuario;
	}

	// Method To Update Particular Usuario Details In Database
	public void updateUsuarioDetails() {
		//System.out.println("Calling updateUsuarioDetails() Method To Update Usuario Record");
			dbObj = new DatabaseOperations();
			
			String cipherText = null;
			
		    IvParameterSpec ivParameterSpec = generateIv();
		    try {
				SecretKey key = getKeyFromPassword("SamplesDB","MeuAmigodoAlém");
				cipherText = encryptPasswordBased(getSenha(), key, ivParameterSpec);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			}	
		    this.setSenha(cipherText);
			
			dbObj.updateUsuarioRecord(this);
			setKeep_idallset(false);
			getPermissao().savePermissaoRecord(getIdusu());
	}
	
	// Method To Fetch All From The Database
	public List<Usuario> getAllUsuarioAsList() {
		if (getUsuarioAll()!=null && isKeep_idallset()) return getUsuarioAll();
		//System.out.println("Calling getAllUsuarioAsList() Method To Fetch Usuarios Record");
		dbObj = new DatabaseOperations();		
		setUsuarioAll(dbObj.retrieveAllUsuarioAsList());
		return getUsuarioAll();
	}
	
	public void Login(String p_email, String p_senha) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String decryptedCipherText=null;
		if(p_email != null && p_senha != null) {
			Usuario user = (new DatabaseOperations()).searchOneUsuario(p_email);
			if (user != null) {
				IvParameterSpec ivParameterSpec = generateIv();
				SecretKey key;
				try {
					key = getKeyFromPassword("SamplesDB","MeuAmigodoAlém");
					decryptedCipherText = decryptPasswordBased(user.getSenha(), key, ivParameterSpec);
					
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
					e1.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				}
				
				if(p_senha.compareTo(decryptedCipherText)==0 && user.getAtivo() > 1 ) {
					setIdusu(user.getIdusu());
					setEmail(user.getEmail());
					setSenha(user.getSenha());				
					setNome(user.getNome());
					setAtivo(user.getAtivo());
					getAllUsuarioAsList();
					setMessage("Login succeeded for "+ p_email);
					System.out.println(getMessage());
					try {
						facesContext.getExternalContext().redirect("../login/main.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} //equal password
				else {
					if (user.getAtivo() > 1)
						setMessage("ERROR: Login failed! Please, try again.");
					else
						setMessage("ERROR: User inactive.");
					System.out.println(getMessage());
					try {
						facesContext.getExternalContext().redirect("../login/login.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}else {
				setMessage("ERROR: Invalid user.");
				System.out.println(getMessage());
				try {
					facesContext.getExternalContext().redirect("../login/login.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}//parameteres not null
	}
	
	public Integer access(Integer access) {
		Integer perm=(new DatabaseOperations()).searchAccessUsuario(getIdusu(), access);
		if (perm==null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			setMessage("ERROR: Access denied.");
			System.out.println(getMessage());
			FacesMessage facesMessage = new FacesMessage(getMessage());
			facesContext.addMessage("selectRecord:selectRecordmessage", facesMessage);
			try {
				facesContext.getExternalContext().redirect("../login/main.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			if (perm.intValue()==9 && !isSubuser()) {
				setEdited(new Usuario());
				getEdited().setPermissao(new Permissao());
				getEdited().getPermissao().init();
				this.setSubuser(true);
			}
		}
		return perm;
	}
	
 	public void Logout() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		setMessage("Logout succeeded for "+ getEmail());
		System.out.println(getMessage());
		reset();
		try {
			facesContext.getExternalContext().redirect("../login/login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 	
 	public static SecretKey getKeyFromPassword(String password, String salt)
 		    throws NoSuchAlgorithmException, InvalidKeySpecException {
 		    
 		    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
 		    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
 		    SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
 		    return secret;
 		} 	

 	public static IvParameterSpec generateIv() {
 		byte[] iv = {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1};
 	    return new IvParameterSpec(iv);
 	} 	
 	
    public static String encryptPasswordBased(String plainText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }
    
    public static String decryptPasswordBased(String cipherText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }    
}
