package managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.validation.constraints.AssertTrue;
import util.Utilidades;

/**
 *
 * @author Ricardo
 */
@ManagedBean(name = "accesoBean")
@ViewScoped
public class AccesoManagedBean implements Serializable{

    private String entidad;
    private Usuario usuario;
    private Paciente paciente;
    DAOFactory df = null;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    
    

    /**
     *
     */
    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        usuario = new Usuario();
        paciente = new Paciente();
        entidad = "";
        if (FacesUtils.getSession("usuario") != null) {
            usuario = (Usuario) FacesUtils.getSession("usuario");
            switch (usuario.getTipo()) {
                case "p":
                    paciente = (Paciente) df.getGenericoDAO().getOne(usuario.getId(), paciente.getClass());
                    break;
                case "m": ;
                    break;
                case "a": ;
                    break;
                default:
                    break;
            }
        }

    }

    /**
     *
     * @return
     * @throws java.lang.Exception
     */
    public String login() throws Exception {
        IGenericoDAO igd = df.getGenericoDAO();
        entidad = "Usuario as u WHERE u.email = '" + this.getUsuario().getEmail() + "' AND u.clave = '" + Utilidades.encriptarMD5(this.getUsuario().getClave()) + "'";
        List<Usuario> listaUsuarios = igd.get(entidad);
        System.out.println(Utilidades.encriptarMD5(this.getUsuario().getClave()));
        //Si la lista que nos devuelve no está vacía debe contener el usuario en el primer elemento de la lista
        if (!listaUsuarios.isEmpty()) {
            usuario = (Usuario) igd.getOne(listaUsuarios.get(0).getId(), listaUsuarios.get(0).getClass());
            FacesUtils.addSession("usuario", usuario);
        } else {
            System.out.println("Usuario " + this.usuario.getEmail() + " no encontrado");
            //La primera no va a null cuando el rich:messages no es globalOnly (el for no puede ser para el graphValidator
            FacesUtils.addMessage("formularioLogin", "error", "Usuario y/o contraseña incorrectos");
        }

        return "";
    }

    /**
     *
     * @return
     * @throws java.lang.Exception
     */
    public String registro() throws Exception {
        IGenericoDAO igd = df.getGenericoDAO();
        System.out.println("Registrando paciente ");
        if (!igd.get("Usuario as u WHERE u.email = '" + this.paciente.getEmail() + "'").isEmpty()) {
            FacesUtils.addMessage("formularioRegistro", "error", "El email " +this.getPaciente().getEmail() +" ya está registrado.");
            //El email ya existe 
        } else {
            paciente.setFechaAlta(new Date());
            String claveSinEncriptar = paciente.getClave();
            paciente.setClave(Utilidades.encriptarMD5(paciente.getClave()));
            igd.add(paciente);
            //Seteamos el email y clave en usuario para tenerlo en el login, sino petaría porque el login es por usuario
            usuario.setEmail(paciente.getEmail());
            usuario.setClave(claveSinEncriptar);
            this.login();
        }
        return "";
    }

    /**
     *
     * @return
     */
    public String cerrarSesion() {
        System.out.println("Cerrando sesión...");
        FacesUtils.deleteSession();
        return "/index.xhtml?faces-redirect=true";
    }

    //Esto es para validar que las contraseñas del registro sean iguales
    @AssertTrue(message = "Las contraseñas son diferentes")
    public boolean isPasswordsEquals() {
        boolean pass = false;
        
        switch(usuario.getTipo()){
            case "p": pass = paciente.getClave().equals(paciente.getClaveRep()); break;
            case "m": break;
            case "a": break;
            default: break;
        }
        
        System.out.println("Contraseñas iguales?: " +pass);
        return pass;
    }

    public void cambioContrasenia() {
        FacesUtils.addMessage(null, "info", "La contraseña ha sido cambiada");
    }

    public String modificarDatosPer() {
        switch(usuario.getTipo()){
            case "p": df.getGenericoDAO().update(paciente); ;break;
            case "m": break;
            case "a": break;
            default: break;
        }
        df.getGenericoDAO().update(this);
        
        return null;
    }

}
