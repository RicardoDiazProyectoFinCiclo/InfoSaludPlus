/*
    ManagedBeans Usuario
 */
package es.albarregas.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.servlet.http.HttpSession;
import org.hibernate.annotations.Type;

/**
 *
 * @author Ricardo
 */

@Entity
@ManagedBean(name = "usuario")
@Table(name = "Usuarios")
@Inheritance(strategy=InheritanceType.JOINED)
@SessionScoped
public abstract class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idUsuario")
    private int id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private String nif;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    @Type (type = "blob")
    private String clave;
    @Column(nullable = false)
    @Type (type = "datetime")
    private Date fechaAlta;
    @Column(nullable = false)
    private int idDireccion;
    @Column(nullable = false)
    private int idCentro;
    private Blob imagen;

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

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public Blob getImagen() {
        return imagen;
    }

    public void setImagen(Blob imagen) {
        this.imagen = imagen;
    }
    
    public String login(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", Usuario.this);
        System.out.println("Login");
        return "inicio";
    }
    
    public String cerrarSesion(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        HttpSession httpSession = (HttpSession) session;
        httpSession.invalidate();
        return "inicio";
    }
    
}
