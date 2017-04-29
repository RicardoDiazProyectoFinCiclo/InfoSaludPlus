/*
    ManagedBeans Usuario
 */
package es.albarregas.beans;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author Ricardo
 */
@Entity
@ManagedBean(name = "usuario")
@Table(name = "Usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable, Cloneable {

    @Id
    @Column(name = "idUsuario")
    @GeneratedValue(strategy = IDENTITY)
    protected int id;
    @Column(nullable = false)
//    @Size(min = 3, max = 30, message = "El tamaño del campo NOMBRE debe ser entre {min} y {max} caracteres")
    protected String nombre = "";
    @Column(nullable = false)
//    @Size(min = 3, max = 30, message = "El tamaño del campo APELLIDOS debe ser entre {min} y {max} caracteres")
    protected String apellidos = "";
    @Column(nullable = false)
    protected String nif;
    @Column(nullable = false, unique = true)
    protected String email = "";
    @Column(nullable = false)
    protected String clave = "";
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date fechaNacimiento;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idDireccion")
    protected Direccion direccion;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCentro")
    protected Centro centro;
    protected Blob imagen;
    /*
    p = paciente, m = medico, a = administrador, e = enfermera, o = otro. enfermeros y otros serían para posteriores versiones
     */
    @Column(columnDefinition = "set('p','m','a','e','o') DEFAULT 'p' not null")
    protected String tipo = "p";
    @Column(columnDefinition = "set('s','n') DEFAULT 'n' not null")
    protected String bloqueado = "n";
    @Transient
    protected String claveRep = "";

    @Transient
    protected String mensaje;

    //Esto es para validar que las contraseñas del registro sean iguales
    @AssertTrue(message = "Las contraseñas son diferentes")
    public boolean isPasswordsEquals() {
        return clave.equals(claveRep);
    }

    public void cambioContrasenia() {
        FacesUtils.addMessage(null, "info", "La contraseña ha sido cambiada");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getClaveRep() {
        return claveRep;
    }

    public void setClaveRep(String claveRep) {
        this.claveRep = claveRep;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String login() {
        String salida = "";
        String entidad;

        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        entidad = "Usuario as u WHERE u.email = '" + this.getEmail() + "' AND u.clave = '" + this.getClave() + "'";
        List<Usuario> listaUsuarios = igd.get(entidad);

        //Si la lista que nos devuelve no está vacía debe contener el usuario en el primer elemento de la lista
        if (!listaUsuarios.isEmpty()) {
            FacesUtils.addSession("usuario", igd.getOne(listaUsuarios.get(0).id, listaUsuarios.get(0).getClass()));
        } else {
            System.out.println("Usuario " + this.getEmail() + " no encontrado");
            FacesUtils.addMessage(null, "error", "El usuario y/o contraseña no existe");
        }

        return salida;
    }

    public String cerrarSesion() {
        System.out.println("Cerrando sesión...");
        FacesUtils.deleteSession();
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
