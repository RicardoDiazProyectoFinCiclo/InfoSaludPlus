package managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Direccion;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Provincia;
import es.albarregas.modelo.Pueblo;
import es.albarregas.modelo.Usuario;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.AssertTrue;
import util.Utilidades;

/**
 *
 * @author Ricardo
 */
@ManagedBean(name = "accesoBean")
@ViewScoped
public class AccesoManagedBean implements Cloneable, Serializable {

    private String entidad;
    private Usuario usuario;
    private Paciente paciente;
    private Direccion direccion;
    private Provincia provincia;
    private Pueblo pueblo;
    private Centro centro;
    private List<Centro> listCentros;
    private List<Pueblo> listPueblos;
    String codigoPostal;

    private IGenericoDAO igd = null;
    private DAOFactory df = null;

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

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Pueblo getPueblo() {
        return pueblo;
    }

    public void setPueblo(Pueblo pueblo) {
        this.pueblo = pueblo;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public List<Pueblo> getListPueblos() {
        return listPueblos;
    }

    public void setListPueblos(List<Pueblo> listPueblos) {
        this.listPueblos = listPueblos;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public List<Centro> getListCentros() {
        return listCentros;
    }

    public void setListCentros(List<Centro> listCentros) {
        this.listCentros = listCentros;
    }

    /**
     *
     */
    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        this.usuario = new Usuario();
        this.paciente = new Paciente();
        this.direccion = new Direccion();
        this.provincia = new Provincia();
        this.pueblo = new Pueblo();
        this.centro = new Centro();
        this.listCentros = igd.get("Centro");

        System.out.println("Entrando en el postconstructor");
        entidad = "";
        if (FacesUtils.getSession("usuario") != null) {
            usuario = (Usuario) FacesUtils.getSession("usuario");
            switch (usuario.getTipo()) {
                case "p":
                    paciente = (Paciente) df.getGenericoDAO().getOne(usuario.getId(), paciente.getClass());
                    direccion = paciente.getDireccion();
                    pueblo = direccion.getPueblo();
                    listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
                    provincia = pueblo.getProvincia();
                    centro = paciente.getCentro();
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
     * @return @throws java.lang.Exception
     */
    public String login() throws Exception {
        entidad = "Usuario as u WHERE u.email = '" + this.getUsuario().getEmail() + "' AND u.clave = '" + Utilidades.encriptarMD5(this.getUsuario().getClave()) + "'";
        List<Usuario> listaUsuarios = igd.get(entidad);
        //Si la lista que nos devuelve no está vacía debe contener el usuario en el primer elemento de la lista
        if (!listaUsuarios.isEmpty()) {
            usuario = (Usuario) igd.getOne(listaUsuarios.get(0).getId(), listaUsuarios.get(0).getClass());
            //Los demás datos los cargaremos en el postconstructor
            FacesUtils.addSession("usuario", usuario);
        } else {
            //La primera no va a null cuando el rich:messages no es globalOnly (el for no puede ser para el graphValidator
            FacesUtils.addMessage("formularioLogin", "error", "Usuario y/o contraseña incorrectos");
        }

        return "";
    }

    /**
     *
     * @return @throws java.lang.Exception
     */
    public String registro() throws Exception {

            System.out.println("Registrando paciente ");
            if (!igd.get("Usuario as u WHERE u.email = '" + this.paciente.getEmail() + "'").isEmpty()) {
                FacesUtils.addMessage("formularioRegistro", "error", "El email " + this.getPaciente().getEmail() + " ya está registrado.");
                //El email ya existe 
            } else {
                paciente.setFechaAlta(new Date());
                String claveSinEncriptar = paciente.getClave();
                paciente.setClave(Utilidades.encriptarMD5(paciente.getClave()));
                pueblo.setProvincia(provincia);
                direccion.setPueblo(pueblo);
                paciente.setDireccion(direccion);
                paciente.setCentro(centro);
                igd.add(paciente);
                //Seteamos el email y clave en usuario para tenerlo en el login, sino petaría porque el login es por usuario
                usuario.setEmail(paciente.getEmail());
                usuario.setClave(claveSinEncriptar);
                System.out.println("Paciente registrado! ");
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

        switch (usuario.getTipo()) {
            case "p":
                pass = paciente.getClave().equals(paciente.getClaveRep());
                break;
            case "m":
                break;
            case "a":
                break;
            default:
                break;
        }

        System.out.println("Contraseñas iguales?: " + pass);
        return pass;
    }

    public void cambioContrasenia() {
        FacesUtils.addMessage(null, "info", "La contraseña ha sido cambiada");
    }

    public void escuchadorCP() {
        if (this.getPueblo().getCodigoPostal().trim().length() == 5) {
            listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
            if (!listPueblos.isEmpty()) {
                this.provincia = (Provincia) igd.getOne(listPueblos.get(0).getProvincia().getId(), provincia.getClass());
            } else {
                this.provincia.setNombre("");
                this.paciente.getDireccion().getPueblo().getProvincia().setNombre("");
            }
        } else {
            listPueblos = null;
            provincia.setNombre("");
        }

    }

    /**
     * Modificar los datos personales del usuario
     * @return 
     */
    public String modificarDatos() {
        try{
        switch (usuario.getTipo()) {
            case "p":

                pueblo.setProvincia(provincia);
                direccion.setPueblo(pueblo);
                paciente.setDireccion(direccion);
                paciente.setCentro(centro);
                df.getGenericoDAO().update(paciente);
                break;
            case "m":
                break;
            case "a":
                break;
            default:
                break;
        }
        
        }catch(Exception e){
            e.printStackTrace();
            FacesUtils.addMessage("formularioCambioDatos", "error", "Ha habido un error un error, y no se han podido modificar los datos");
        };

        return null;
    }
    
    /**
     * Modificar la dirección del usuario
     * @return 
     */
    public String modificarDatosDir(){
        return "";
    }
    
    /**
     * Modificar los datos del Login de usuario
     * @return 
     */
    public String modificarDatosLogin(){
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
