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
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author Ricardo
 */
@ManagedBean(name = "accesoBean")
@ViewScoped
public class AccesoManagedBean implements Serializable {

    private String entidad;
    private Usuario usuario;
    private Paciente paciente;

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

    
    @PostConstruct
    public void init() {
        usuario = new Usuario();
        paciente = new Paciente();
        entidad = "";
    }

    public String login() {

        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        entidad = "Usuario as u WHERE u.email = '" + this.usuario.getEmail() + "' AND u.clave = '" + this.usuario.getClave() + "'";
        List<Usuario> listaUsuarios = igd.get(entidad);

        //Si la lista que nos devuelve no está vacía debe contener el usuario en el primer elemento de la lista
        if (!listaUsuarios.isEmpty()) {
            usuario = (Usuario) igd.getOne(listaUsuarios.get(0).getId(), listaUsuarios.get(0).getClass());
            FacesUtils.addSession("usuario", usuario);
        } else {
            System.out.println("Usuario " + this.usuario.getEmail() + " no encontrado");
            FacesUtils.addMessage(null, "error", "El usuario y/o contraseña no existe");
        }

        return "";
    }

    public String registro() {
        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        System.out.println("Registrando paciente ");
        if (!igd.get("Usuario as u WHERE u.email = '" + this.paciente.getEmail() + "'").isEmpty()) {
            FacesUtils.addMessage(null, "error", "Usuario y/o contraseña son incorrectos");
            //El email ya existe
        } else {
            paciente.setFechaAlta(new Date());
            igd.add(this);
            this.login();
            System.out.println("Email no encontrado...");
        }
        return "";
    }

    public String cerrarSesion() {
        System.out.println("Cerrando sesión...");
        FacesUtils.deleteSession();
        return "/index.xhtml?faces-redirect=true";
    }

    //Esto es para validar que las contraseñas del registro sean iguales
    @AssertTrue(message = "Las contraseñas son diferentes")
    public boolean isPasswordsEquals() {
        return usuario.getClave().equals(usuario.getClaveRep());
    }

    public void cambioContrasenia() {
        FacesUtils.addMessage(null, "info", "La contraseña ha sido cambiada");
    }

}
