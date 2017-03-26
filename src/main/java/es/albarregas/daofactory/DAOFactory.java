package es.albarregas.daofactory;

import es.albarregas.dao.IGenericoDAO;

public abstract class DAOFactory {

    public abstract IGenericoDAO getGenericoDAO();

    public static DAOFactory getDAOFactory() {
        DAOFactory daof = null;

        daof = new MySQLDAOFactory();

        return daof;
    }

}
