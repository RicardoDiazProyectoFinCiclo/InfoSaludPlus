package es.albarregas.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  Modelo relacionado con la tabla provincias
 * @author Ricardo
 */
@Entity
@Table(name = "Provincias")
public class Provincia implements Serializable {

    @Id
    @Column(name = "idProvincia")
    private int id;
    private String nombre = "";

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
    
    
}
