package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Cita;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.FacesUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 * Managed Bean para la gestión de las citas por parte del administrador
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "gestionarCitasBean")
public class GestionarCitasManagedBean implements Serializable {

    private Cita cita;
    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;
    private List<Cita> listCitas;
    private UIDataTable tablaCitas;
    private DAOFactory df;
    private IGenericoDAO igd;
    private List<Medico> listMedicosAusentes;
    private List<Medico> listMedicosNoAusentes;
    private Integer idMedicoAusente;
    private Integer idMedicoNoAusente;
    private String claseMsg = "";

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public List<Medico> getListMedicosAusentes() {
        return listMedicosAusentes;
    }

    public void setListMedicosAusentes(List<Medico> listMedicosAusentes) {
        this.listMedicosAusentes = listMedicosAusentes;
    }

    public List<Medico> getListMedicosNoAusentes() {
        return listMedicosNoAusentes;
    }

    public void setListMedicosNoAusentes(List<Medico> listMedicosNoAusentes) {
        this.listMedicosNoAusentes = listMedicosNoAusentes;
    }

    public Integer getIdMedicoAusente() {
        return idMedicoAusente;
    }

    public void setIdMedicoAusente(Integer idMedicoAusente) {
        this.idMedicoAusente = idMedicoAusente;
    }

    public Integer getIdMedicoNoAusente() {
        return idMedicoNoAusente;
    }

    public void setIdMedicoNoAusente(Integer idMedicoNoAusente) {
        this.idMedicoNoAusente = idMedicoNoAusente;
    }

    public String getClaseMsg() {
        return claseMsg;
    }

    public void setClaseMsg(String claseMsg) {
        this.claseMsg = claseMsg;
    }

    /**
     * Cargamos al entrar en la vista
     */
    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();

        usuario = (Usuario) FacesUtils.getSession("usuario");
        listCitas = igd.get("Cita Where DATE(fecha) > CURDATE()");
    }

    /**
     * Se ejecuta al levantar el modal de cambio de médicos
     */
    public void levantarModalCambioMedicos() {
        try {
            listMedicosAusentes = igd.get("Medico Where ausencia = 's'");
            listMedicosNoAusentes = igd.get("Medico Where ausencia = 'n'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cambiar de medicos, uno que está de baja o ausente por otro disponible
     */
    public void cambioMedicoAusente() {
        try {
            //Vemos que se han seleccionado los médicos
            claseMsg = "msgErrorGlobal";
            if (idMedicoAusente == 0) {
                FacesUtils.addMessage("formCambioMedicos", "error", "Debe seleccionar un médico ausente");
            } else if (idMedicoNoAusente == 0) {
                FacesUtils.addMessage("formCambioMedicos", "error", "Debe seleccionar un médico no ausente");
            } else {
                List<Cita> listCitasCambiar = igd.get("Cita Where idMedico = " + idMedicoAusente +" AND CURDATE() <= DATE(fecha)");
                Medico medicoNoAusente = (Medico) igd.getOne(idMedicoNoAusente, Medico.class);

                if (listCitasCambiar.size() > 0) {
                    for (Cita cita : listCitasCambiar) {
                        cita.setMedico(medicoNoAusente);
                        igd.update(cita);
                    }
                    claseMsg = "msgOkGlobal";
                    FacesUtils.addMessage("formCambioMedicos", "info", "El médico ha sido cambiado para cada paciente correctamente");
                }else{
                    FacesUtils.addMessage("formCambioMedicos", "error", "Ningún paciente tiene el médico a cambiar");
                }
            }
        } catch (Exception e) {
            FacesUtils.addMessage("formCambioMedicos", "error", "Los médicos no se han podido cambiar");
            e.printStackTrace();
        }
    }

}
