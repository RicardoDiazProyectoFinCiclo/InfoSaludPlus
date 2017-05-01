package es.albarregas.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@Table(name = "Informes")
public class Informe implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idInforme")
    private int id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idMedico")
    private Medico medico;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String descripcion;
    private String tratamiento;
    private String observacion;
    @Column(nullable = false)
    private Date fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
