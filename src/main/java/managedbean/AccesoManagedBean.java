package managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Direccion;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Provincia;
import es.albarregas.modelo.Pueblo;
import es.albarregas.modelo.Servicio;
import es.albarregas.modelo.Usuario;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private Medico medico;
    private Direccion direccion;
    private Provincia provincia;
    private Pueblo pueblo;
    private Centro centro;
    private Servicio servicio;
    private List<Centro> listCentros;
    private List<Pueblo> listPueblos;
    private List<Servicio> listServicios;
    private List<Medico> listMedicos;
    private String codigoPostal;

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

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
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

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public List<Centro> getListCentros() {
        return listCentros;
    }

    public void setListCentros(List<Centro> listCentros) {
        this.listCentros = listCentros;
    }

    public List<Servicio> getListServicios() {
        return listServicios;
    }

    public void setListServicios(List<Servicio> listServicios) {
        this.listServicios = listServicios;
    }

    public List<Medico> getListMedicos() {
        return listMedicos;
    }

    public void setListMedicos(List<Medico> listMedicos) {
        this.listMedicos = listMedicos;
    }

    /**
     * Función que se ejecuta cada vez que se acceder a una función de esta
     * clase.
     */
    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        this.usuario = new Usuario();
        this.paciente = new Paciente();
        this.medico = new Medico();
        this.direccion = new Direccion();
        this.provincia = new Provincia();
        this.pueblo = new Pueblo();
        this.centro = new Centro();
        this.servicio = new Servicio();
        this.listCentros = igd.get("Centro");
        this.listServicios = igd.get("Servicio");

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
                    if(paciente.getMedico() != null){
                        medico = paciente.getMedico();
                    }          
                    //El paciente solo puede elegir médicos de su centro
                    medicosPorCentros();
                    break;
                case "m":
                    medico = (Medico) df.getGenericoDAO().getOne(usuario.getId(), medico.getClass());
                    direccion = medico.getDireccion();
                    pueblo = direccion.getPueblo();
                    servicio = medico.getServicio();
                    listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
                    provincia = pueblo.getProvincia();
                    centro = medico.getCentro();
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * Acceder al area de usuario - panel de control de la aplicación (cada tipo
     * de usuario tiene su área)
     *
     * @return @throws java.lang.Exception
     */
    public String login() throws Exception {
        entidad = "Usuario as u WHERE u.email = '" + this.getUsuario().getEmail() + "' AND u.clave = '" + Utilidades.encriptarMD5(this.getUsuario().getClave()) + "'";
        List<Usuario> listaUsuarios = igd.get(entidad);
        //Si la lista que nos devuelve no está vacía debe contener el usuario en el primer elemento de la lista
        if (!listaUsuarios.isEmpty()) {
            usuario = (Usuario) listaUsuarios.get(0);
            //Los demás datos los cargaremos en el postconstructor
            FacesUtils.addSession("usuario", usuario);
        } else {
            //La primera no va a null cuando el rich:messages no es globalOnly (el for no puede ser para el graphValidator
            FacesUtils.addMessage("formularioLogin", "error", "Usuario y/o contraseña incorrectos");
        }

        return "";
    }

    /* REGISTROS -Los hacemos independientes para evitar problemas por las herencias, y para diferenciarlos - */
    /**
     * Registramos los usuarios pacientes
     *
     * @return @throws java.lang.Exception
     */
    public String registroPaciente() throws Exception {
        //El email y el nif serán unique porque tienen que ser únicos para el funcionamiento correcto de la aplicación
        System.out.println("Registrando paciente ");
        if (!paciente.getClave().equals(paciente.getClaveRep())) {
            FacesUtils.addMessage("formularioRegistro", "error", "Las contraseñas son diferentes");
        } else if (!igd.get("Usuario as u WHERE u.email = '" + this.paciente.getEmail() + "'").isEmpty()) {
            FacesUtils.addMessage("formularioRegistro", "error", "El email " + this.getPaciente().getEmail() + " ya está registrado.");
            //El email ya existe 
        } else if (!igd.get("Usuario as u WHERE u.nif = '" + this.paciente.getNif() + "'").isEmpty()) {
            FacesUtils.addMessage("formularioRegistro", "error", "El NIF " + this.getPaciente().getNif() + " ya está registrado.");
            //El email ya existe 
        } else {
            paciente.setFechaAlta(new Date());
            String claveSinEncriptar = paciente.getClave();
            paciente.setClave(Utilidades.encriptarMD5(paciente.getClave()));
            pueblo.setProvincia(provincia);
            direccion.setPueblo(pueblo);
            paciente.setDireccion(direccion);
            paciente.setCentro(centro);
            paciente.setTipo("p"); // p = paciente, m = medico, a = admin
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
     * Registro de los usuarios medicos
     *
     * @return
     * @throws Exception
     */
    public String registroMedico() throws Exception {

        //El email y el nif serán unique porque tienen que ser únicos para el funcionamiento correcto de la aplicación
        System.out.println("Registrando medico ");
        if (!medico.getClave().equals(medico.getClaveRep())) {
            FacesUtils.addMessage("formularioRegistroPersonalSanitario", "error", "Las contraseñas son diferentes");
        } else if (!igd.get("Usuario as u WHERE u.email = '" + this.medico.getEmail() + "'").isEmpty()) {
            FacesUtils.addMessage("formularioRegistroPersonalSanitario", "error", "El email " + this.getMedico().getEmail() + " ya está registrado.");
            //El email ya existe 
        } else if (!igd.get("Usuario as u WHERE u.nif = '" + this.medico.getNif() + "'").isEmpty()) {
            FacesUtils.addMessage("formularioRegistroPersonalSanitario", "error", "El NIF " + this.getMedico().getNif() + " ya está registrado.");
            //El email ya existe 
        } else {
            medico.setFechaAlta(new Date());
            String claveSinEncriptar = medico.getClave();
            medico.setClave(Utilidades.encriptarMD5(medico.getClave()));
            pueblo.setProvincia(provincia);
            direccion.setPueblo(pueblo);
            medico.setDireccion(direccion);
            medico.setServicio(servicio);
            medico.setCentro(centro);
            medico.setTipo("m"); // p = paciente, m = medico, a = admin
            igd.add(medico);
            //Seteamos el email y clave en usuario para tenerlo en el login, sino petaría porque el login es por usuario
            usuario.setEmail(medico.getEmail());
            usuario.setClave(claveSinEncriptar);
            System.out.println("Medico registrado! ");
            // hacer login después del registro solo está pensado para pacientes, en el futuro ya veremos
            // this.login();
        }
        return "";
    }

    /**
     * Función donde se cierra la sesión
     *
     * @return
     */
    public String cerrarSesion() {
        System.out.println("Cerrando sesión...");
        FacesUtils.deleteSession();
        return "/index.xhtml?faces-redirect=true";
    }

    //Esto es para validar que las contraseñas del registro sean iguales
//    @AssertTrue(message = "Las contraseñas son diferentes")
//    public boolean isPasswordsEquals() {
//        boolean pass = false;
//
//        switch (usuario.getTipo()) {
//            case "p":
//                pass = paciente.getClave().equals(paciente.getClaveRep());
//                break;
//            case "m":
//                pass = medico.getClave().equals(medico.getClaveRep());
//                break;
//            default:
//                break;
//        }
//
//        System.out.println("Contraseñas iguales?: " + pass);
//        return pass;
//    }
    /**
     * Función para lanzar un mensaje de cambio de contraseña
     */
    public void cambioContrasenia() {
        FacesUtils.addMessage(null, "info", "La contraseña ha sido cambiada");
    }

    /**
     * Función escuchador del codigo postal, para cambiar provincia y pueblo en
     * función del código postal introducido
     */
    public void escuchadorCP() {
        if (this.getPueblo().getCodigoPostal().trim().length() == 5) {
            listPueblos = igd.get("Pueblo Where codigoPostal = '" + this.getPueblo().getCodigoPostal() + "'");
            if (!listPueblos.isEmpty()) {
                this.provincia = (Provincia) igd.getOne(listPueblos.get(0).getProvincia().getId(), provincia.getClass());
            } else {
                this.provincia.setNombre("");
            }
        } else {
            listPueblos = null;
            provincia.setNombre("");
        }

    }

    /**
     * Modificar los datos personales del usuario
     *
     * @return
     */
    public String modificarDatos() {
        try {
            switch (usuario.getTipo()) {
                case "p":
                    modificarDatosPaciente();
                    break;
                case "m":
                    modificarDatosMedico();
                    break;
                case "a":
                    modificarDatosAdmin();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addMessage("formularioCambioDatos", "error", "Ha habido un error un error, y no se han podido modificar los datos");
        };

        return null;
    }

    /**
     * Función donde se actualizan los datos de los pacientes
     */
    public void modificarDatosPaciente() {
        pueblo.setProvincia(provincia);
        direccion.setPueblo(pueblo);
        paciente.setDireccion(direccion);
        paciente.setCentro(centro);
        paciente.setMedico(medico);
        df.getGenericoDAO().update(paciente);
    }

    /**
     * Función donde se actualizan los datos de los médicos
     */
    public void modificarDatosMedico() {
        pueblo.setProvincia(provincia);
        direccion.setPueblo(pueblo);
        medico.setDireccion(direccion);
        medico.setCentro(centro);
        df.getGenericoDAO().update(medico);
    }

    /**
     * Función donde se actualizan los datos del administrador
     */
    public void modificarDatosAdmin() {
        df.getGenericoDAO().update(usuario);
    }

    /**
     * Función para seleccionar los médicos disponibles (solo pueden tener x pacientes) por centro seleccionado
     */
    public void medicosPorCentros() {
        
        //Consulta en la que seleccionamos los medicos por el centro seleccionado y que tengan menos de x pacientes (subconsulta)
        //Para que los médicos tengan así un tope de pacientes (ponemos 2 de prueba).
        String consulta = "Medico m Where m.centro.id = " + paciente.getCentro().getId() 
                + " AND  m.id NOT IN (SELECT p.medico.id from Paciente p WHERE NOT (p.medico.id IS NULL) GROUP BY p.medico.id HAVING COUNT(p.medico.id) > 4 ) ";
        listMedicos = igd.get(consulta);

    }

}
