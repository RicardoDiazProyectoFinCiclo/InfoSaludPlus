package es.albarregas.daofactory;

import es.albarregas.dao.GenericoDAO;
import es.albarregas.dao.IGenericoDAO;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public IGenericoDAO getGenericoDAO() {
        return new GenericoDAO();
    }

}
