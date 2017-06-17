package es.albarregas.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.persistence.Transient;

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
    @OneToOne
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;
    @OneToOne
    @JoinColumn(name = "idMedico")
    private Medico medico;
    @OneToOne
    @JoinColumn(name = "idCentro")
    private Centro centro;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    //a = Administrativa, d = demanda
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

    /**
     * Método para obtener el día de la semana en español
     * @return 
     */
    public String diaFormateado() {
        String dia = "";
        try {
            String[] dias = {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sábado"};
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            dia = dias[cal.get(Calendar.DAY_OF_WEEK) - 1];

        } catch (Exception e) {
            dia = "";
            e.printStackTrace();
        }

        return dia;
    }

    /**
     * Método para obtener la hora formateada con formato 24h
     * @return 
     */
    public String horaFormateada() {
        SimpleDateFormat smf = new SimpleDateFormat("HH:mm");

        return smf.format(fecha);
    }

}
