package es.albarregas.modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
 *  Modelo relacionado con la tabla informes
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
    private String titulo = "";
    @Column(nullable = false)
    private String descripcion = "";
    private String tratamiento = "";
    private String observacion = "";
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

    /**
     * Método para obtener el día de la semana en español
     *
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
     *
     * @return
     */
    public String horaFormateada() {
        SimpleDateFormat smf = new SimpleDateFormat("HH:mm");

        return smf.format(fecha);
    }

    /**
     * Método para obtener la fecha formateada en formato largo
     * @return 
     */
    public String fechaFormateada() {
        SimpleDateFormat smf = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        return smf.format(fecha);
    }

}
