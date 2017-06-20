package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Cita;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.FacesUtils;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 * Managed Bean para las citas previas de los médicos
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "citaPreviaMedicoBean")
public class CitaPreviaMedicoManagedBean implements Serializable, Cloneable {

    private Medico medico;
    private Paciente paciente;
    private Usuario usuario;
    private Date inicioFecha;
    private Date finFecha;
    private List<Cita> listCitas;
    DAOFactory df;
    IGenericoDAO igd;
    private UIDataTable tablaCitas;
    private Date fechaHoy;

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

    public List<Cita> getListCitas() {
        return listCitas;
    }

    public void setListCitas(List<Cita> listCitas) {
        this.listCitas = listCitas;
    }

    public UIDataTable getTablaCitas() {
        return tablaCitas;
    }

    public void setTablaCitas(UIDataTable tablaCitas) {
        this.tablaCitas = tablaCitas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    /**
     * Se ejecuta al entrar en la vista
     */
    @PostConstruct
    public void init() {
        try {
            df = DAOFactory.getDAOFactory();
            igd = df.getGenericoDAO();
            fechaHoy = new Date();

            if (FacesUtils.getSession("usuario") != null) {
                usuario = (Usuario) FacesUtils.getSession("usuario");
            }

            inicioFecha = new Date();
            finFecha = new Date();

            //Seleccionamos solo las citas que tiene el médico hoy, para ello cogemos la parte fecha de la fecha y hora (datetime) y la igualamos a la fecha de hoy (CURDATE())
            this.setListCitas(igd.get("Cita WHERE idMedico = " + usuario.getId() + " AND CURDATE() = DATE(fecha) ORDER BY fecha asc"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
