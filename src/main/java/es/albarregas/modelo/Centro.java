package es.albarregas.modelo;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import java.io.Serializable;
import java.util.ArrayList;
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
 * Modelo relacionado con la tabla centros
 * @author Ricardo
 */
@Entity
@Table(name = "Centros")
public class Centro implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idCentro")
    private int id;
    @Column(nullable = false)
    private String nombre = "";
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "idDireccion")
    private Direccion direccion;
    @JoinColumn(name = "idImagen")
    @OneToOne(cascade = {CascadeType.ALL})
    private Imagen imagen;

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

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtener un solo centro
     */
    public void oneCentro() {
        if (this.id > 0) {
            DAOFactory df = DAOFactory.getDAOFactory();
            IGenericoDAO igd = df.getGenericoDAO();
            Centro centro = (Centro) igd.getOne(this.id, Centro.class);
            this.id = centro.getId();
            this.nombre = centro.getNombre();
        }

    }

    /**
     * Obtener todos los centros
     * @return 
     */
    public ArrayList allCentros() {

        DAOFactory df = DAOFactory.getDAOFactory();
        IGenericoDAO igd = df.getGenericoDAO();
        ArrayList listaCentros = (ArrayList<Centro>) igd.get("Centro");
        if (listaCentros == null) {
            listaCentros = new ArrayList();
        }
        return listaCentros;
    }
}
