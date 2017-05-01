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
import javax.persistence.Temporal;

/**
 *
 * @author Ricardo
 */
@Entity
@Table(name = "Citas")
public class Cita implements Serializable {

    @Id
    @Column(name = "idCita")
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idMedico")
    private Medico medico;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idCentro")
    private Centro centro;
    @Column(nullable = false)
    private Date fecha;
    @Column(nullable = false)
    private String tipo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
