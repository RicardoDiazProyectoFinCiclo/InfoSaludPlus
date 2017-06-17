/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Cita;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.Constantes;
import es.albarregas.util.FacesUtils;
import es.albarregas.util.UtilidadesFechas;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 * Managed bean para las citas previas de los pacientes
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "citaPreviaPacienteBean")
public class CitaPreviaPacienteManagedBean implements Serializable, Cloneable {

    private Integer[] horas;
    private Integer[] horasAdministrativa = new Integer[]{8, 9};
    private Integer[] horasConsulta = new Integer[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    private Integer[] minutos;
    private int horaBusqueda;
    private int minutoBusqueda;
    private Date fechaBusqueda;
    private Date fechaHoy;
    private String tipoCita = "administrativa";
    private Usuario usuario;
    private Medico medico;
    private Paciente paciente;
    private Centro centro;
    private List<Cita> listHuecos;
    private UIDataTable tablaHuecos;
    private int indiceMinutos;
    private int indiceHoras;
    private boolean indImprimir;
    private Cita citaElegida; //Es la cita que ha elegido para reservar, pero aún no está reservada
    private Cita citaReservada; //Es la cita que está ya reservada y se va a mostrar como tal
    private String estiloMessages = ""; //Estilo para mensajes en pantalla
    DAOFactory df;
    IGenericoDAO igd;

    public int getHoraBusqueda() {
        return horaBusqueda;
    }

    public void setHoraBusqueda(int horaBusqueda) {
        this.horaBusqueda = horaBusqueda;
    }

    public int getMinutoBusqueda() {
        return minutoBusqueda;
    }

    public void setMinutoBusqueda(int minutoBusqueda) {
        this.minutoBusqueda = minutoBusqueda;
    }

    public Cita getCitaReservada() {
        return citaReservada;
    }

    public void setCitaReservada(Cita citaReservada) {
        this.citaReservada = citaReservada;
    }

    /**
     * Las horas que puede elegir el paciente tiene que depender del tipo de
     * cita
     *
     * @return
     */
    public Integer[] getHoras() {
        if (tipoCita.equals("administrativa")) {
            horas = horasAdministrativa;
        } else if (tipoCita.equals("consulta")) {
            horas = horasConsulta;
        }
        return horas;

    }

    public void setHoras(Integer[] horas) {
        this.horas = horas;
    }

    public Integer[] getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer[] minutos) {
        this.minutos = minutos;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public Date getFechaBusqueda() {
        return fechaBusqueda;
    }

    public void setFechaBusqueda(Date fechaBusqueda) {
        this.fechaBusqueda = fechaBusqueda;
    }

    public List<Cita> getListHuecos() {
        return listHuecos;
    }

    public void setListHuecos(List<Cita> listHuecos) {
        this.listHuecos = listHuecos;
    }

    public UIDataTable getTablaHuecos() {
        return tablaHuecos;
    }

    public void setTablaHuecos(UIDataTable tablaHuecos) {
        this.tablaHuecos = tablaHuecos;
    }

    public Cita getCitaElegida() {
        return citaElegida;
    }

    public void setCitaElegida(Cita citaElegida) {
        this.citaElegida = citaElegida;
    }

    public boolean isIndImprimir() {
        return indImprimir;
    }

    public void setIndImprimir(boolean indImprimir) {
        this.indImprimir = indImprimir;
    }

    public Date getFechaHoy() {
        return fechaHoy;
    }

    public void setFechaHoy(Date fechaHoy) {
        this.fechaHoy = fechaHoy;
    }

    public String getEstiloMessages() {
        return estiloMessages;
    }

    public void setEstiloMessages(String estiloMessages) {
        this.estiloMessages = estiloMessages;
    }

    
    @PostConstruct
    public void init() {
        fechaHoy = new Date();
        horasConsulta = new Integer[]{10, 11, 12, 13, 14};
        minutos = new Integer[]{0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
        fechaBusqueda = new Date();
        this.listHuecos = new ArrayList();
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        if (FacesUtils.getSession("usuario") != null) {
            usuario = (Usuario) FacesUtils.getSession("usuario");
            paciente = (Paciente) igd.getOne(usuario.getId(), Paciente.class);
            obtenerCitaReservada();
        }
    }

    /**
     * Método para generar un List con las proximas 10 citas disponibles en base
     * a la fecha y hora de búsqueda
     *
     * @return
     */
    public String pedirCitaPrevia() {

        if (paciente.getMedico() != null) {
            medico = paciente.getMedico();
        } else {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Debe elegir un médico en Inicio Area Paciente");
            return "";
        }

        if (paciente.getCentro() != null) {
            centro = paciente.getCentro();
        } else {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Debe elegir un centro en Inicio Area Paciente");
            return "";
        }

        if (fechaBusqueda.before(fechaHoy)) {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Debe seleccionar una fecha posterior a hoy");
            return "";
        }

        //El límite es una cita previa por paciente. Se tiene en cuenta las fechas porque las pasadas ya no se consideran citas
        List<Cita> citasPreviasReservadas = igd.get("Cita Where idPaciente = " + usuario.getId() + " AND DATE(fecha) > CURDATE()");
        if (citasPreviasReservadas.size() > 0) {
            FacesUtils.addMessage("formularioCitaPrevia", "error", "Ya tiene una cita previa reservada. Para solicitad una nueva primero deberá eliminar la ya existente.");
            return "";
        }

        //Establecemos los datos de la fecha de busqueda 
        fechaBusqueda = UtilidadesFechas.getDateConFechaYHora(fechaBusqueda, horaBusqueda, minutoBusqueda);

        this.setListHuecos(new ArrayList<Cita>());
        //Mostramos las 10 siguientes posibles citas a elegir
        this.setListHuecos(getObtenerListHuecos(fechaBusqueda, Constantes.NUM_MAX_POSIBLES_CITAS));

        return "";
    }

    /**
     * Método para obtener el índice de la hora seleccionada en el array
     *
     * @param hora
     * @return
     */
    public int getIndiceHoraSeleccionada(int hora) {

        for (int i = 0; i < horas.length; i++) {
            if (hora == horas[i]) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Método para obtener el indice del minuto seleccionado en el array
     *
     * @param minuto
     * @return
     */
    public int getIndiceMinutoSeleccionado(int minuto) {

        for (int i = 0; i < minutos.length; i++) {
            if (minuto == minutos[i]) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Para obtener el listado de posibles citas a elegir por el paciente,
     * pasandole la fecha y el número de posibles citas que le vayamos a dar a
     * elegir al paciente
     *
     * @param fecha
     * @param numPosiblesCitas
     * @return
     */
    private List getObtenerListHuecos(Date fecha, Integer numPosiblesCitas) {

        List<Cita> lista = new ArrayList();
        indiceMinutos = getIndiceMinutoSeleccionado(minutoBusqueda);
        indiceHoras = getIndiceHoraSeleccionada(horaBusqueda);
        int contador = numPosiblesCitas;
        Date huecoOk = fechaBusqueda;
        Cita cita = null;
        huecoOk = UtilidadesFechas.getDateConFechaYHoraSinFindes(huecoOk, horaBusqueda, minutoBusqueda);
        while (contador > 0) {

            SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Cita> citasObtenidas = igd.get("Cita Where fecha = '" + smd.format(huecoOk) + "'"); //añadir medico

            if (citasObtenidas.isEmpty()) {
                cita = new Cita();
                cita.setFecha(huecoOk);
                cita.setPaciente(paciente);
                cita.setMedico(medico);
                cita.setCentro(centro);
                cita.setTipo(tipoCita);
                lista.add(cita);
                contador--;
            }
            //adelantar +1 la cita
            huecoOk = getSiguienteHueco(huecoOk);
        }

        return lista;
    }

    /**
     * Método para obtener la siguiente hora de cita a la que le pasemos.
     * Tenemos que tener en cuenta que si los minutos no cumplen un ciclo, la
     * hora pasada será la misma a la retornada
     *
     * @param fecha
     * @param indiceHoras
     * @param indiceMinutos
     * @return
     */
    private Date getSiguienteHueco(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        if (indiceMinutos < (minutos.length - 1)) {
            indiceMinutos++;
        } else if (indiceHoras < (horas.length - 1)) {
            indiceMinutos = 0;
            indiceHoras++;
        } else {
            //Añadimos un día
            if (cal.get(Calendar.DAY_OF_WEEK) == 6) {
                //Si cae en viernes tendremos que añadir 3 para pasar a Lunes
                cal.add(Calendar.DATE, 3);
            } else {
                cal.add(Calendar.DATE, 1);
            }
            indiceMinutos = 0;
            indiceHoras = 0;
        }

        cal.set(Calendar.HOUR_OF_DAY, horas[indiceHoras]);
        cal.set(Calendar.MINUTE, minutos[indiceMinutos]);

        return cal.getTime();
    }

    /**
     * Método para obtener los datos de la cita previa seleccionada por el
     * paciente
     *
     * @return
     * @throws Exception
     */
    public String seleccionarHueco() throws Exception {
        indImprimir = false;
        citaElegida = (Cita) tablaHuecos.getRowData();
        return "";
    }

    /**
     * Método en el que se guarda la cita previa seleccionada en base de datos
     *
     * @return
     */
    public String confirmarCita() {
        try {
            igd.add(citaElegida);
            this.setCitaReservada(citaElegida);
            FacesUtils.addMessage("formDatosCita", "info", "La cita ha sido reservada correctamente.\nPuede imprimirla en PDF.");
            indImprimir = true;
        } catch (Exception e) {
            FacesUtils.addMessage("formDatosCita", "error", "Error al reservar la cita. Cierre y vuelva a intentarlo.");
            indImprimir = false;
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Función para obtener la cita que ha sido reservada por el paciente (en
     * caso de existir)
     */
    public void obtenerCitaReservada() {
        try {
            List<Cita> citasReservadas = (List<Cita>) igd.get("Cita Where idPaciente = " + paciente.getId() + " and  Date(fecha) > CURDATE()");
            if (!citasReservadas.isEmpty()) {
                this.citaReservada = citasReservadas.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCitaPrevia() {
        try {
            if (citaReservada != null && citaReservada.getId() != 0) {
                igd.delete(citaReservada);
                this.setEstiloMessages("msgOkGlobal");
                FacesUtils.addMessage("formConfirmElimCita", "info", "La cita ha sido eliminada correctamente");
                citaReservada = null;
                indImprimir = false;
            }
        } catch (Exception e) {
            this.setEstiloMessages("msgErrorGlobal");
            FacesUtils.addMessage("formConfirmElimCita", "error", "Error al intentar eliminar la cita previa");
            e.printStackTrace();
        }
    }

}
