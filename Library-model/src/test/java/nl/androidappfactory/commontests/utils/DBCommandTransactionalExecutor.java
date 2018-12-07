package nl.androidappfactory.commontests.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;

@Ignore
public class DBCommandTransactionalExecutor {

    private EntityManager em;

    public DBCommandTransactionalExecutor(EntityManager em) {
        this.em = em;
    }

    public <T> T executeCommand(DBCommand<T> dbCommand) {

        T returnValue = null;
        try {
            em.getTransaction().begin();
            returnValue = dbCommand.execute();
            em.getTransaction().commit();
            return returnValue;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new IllegalStateException(e);
        }
    }
}
