package es.albarregas.modelo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ricardo
 */
@Entity
@Table(name = "Medicos")
public class Medico extends Usuario {

    @Column(nullable = false)
    private String numColegiado = "";
    @Column(columnDefinition = "set('n','s') DEFAULT 'n' not null")
    private String jefe = "n";
    @Column(nullable = false)
    private String titulos = "";
    @OneToOne
    @JoinColumn(name = "idServicio",columnDefinition = "int(11) DEFAULT 1")
    private Servicio servicio;


    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getTitulos() {
        return titulos;
    }

    public void setTitulos(String titulos) {
        this.titulos = titulos;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

}
