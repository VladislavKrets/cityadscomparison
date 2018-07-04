package online.omnia.comparison;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;

/**
 * Created by lollipop on 05.09.2017.
 */
public class MySQLDaoImpl {
    private static MySQLDaoImpl instance;

    private static Configuration masterDbConfiguration;
    private static SessionFactory masterDbSessionFactory;

    static {
        masterDbConfiguration = new Configuration()
                .addAnnotatedClass(TrackerEntity.class)
                .addAnnotatedClass(PostBackEntity.class)
                .addAnnotatedClass(ErrorPostBackEntity.class)
                .configure("/hibernate.cfg.xml");
        Map<String, String> properties = FileWorkingUtils.iniFileReader();
        masterDbConfiguration.setProperty("hibernate.connection.password", properties.get("password"));
        masterDbConfiguration.setProperty("hibernate.connection.username", properties.get("username"));
        masterDbConfiguration.setProperty("hibernate.connection.url", properties.get("url"));

        while (true) {
            try {
                masterDbSessionFactory = masterDbConfiguration.buildSessionFactory();
                break;
            } catch (PersistenceException e) {
                try {
                    e.printStackTrace();
                    System.out.println("Can't connect to master db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public boolean isClickidInDb(String clickId) {
        Session session = masterDbSessionFactory.openSession();
        List<PostBackEntity> postBackEntities = session.createQuery("from PostBackEntity where clickid=:clickId", PostBackEntity.class)
                .setParameter("clickId", clickId)
                .getResultList();
        session.close();
        return !postBackEntities.isEmpty();
    }
    public TrackerEntity getTrackerByPrefix(String prefix) {
        Session session = null;
        TrackerEntity trackerEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                try {
                    trackerEntity = session.createQuery("from TrackerEntity where prefix=:prefix",
                            TrackerEntity.class)
                            .setParameter("prefix", prefix).getSingleResult();
                } catch (NoResultException e) {
                    trackerEntity = null;
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return trackerEntity;
    }
    public PostBackEntity getPostbackByTransactionId(String transactionId) {
        Session session = null;
        PostBackEntity postBackEntity = null;
        while (true) {
            try {
                session = masterDbSessionFactory.openSession();
                postBackEntity = null;
                try {
                    postBackEntity = session.createQuery("from PostBackEntity where transactionid=:transactionid", PostBackEntity.class)
                            .setParameter("transactionid", transactionId).getSingleResult();
                } catch (NoResultException e) {
                    postBackEntity = null;
                }
                break;
            } catch (PersistenceException e) {
                try {
                    System.out.println("Can't connect to db");
                    System.out.println("Waiting for 30 seconds");
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        session.close();
        return postBackEntity;
    }
    public void updateDateTime(String clickId, java.sql.Date date, java.sql.Time time) {
        System.out.println(date);
        System.out.println(time);
        Session session = masterDbSessionFactory.openSession();
        session.beginTransaction();
        session.createQuery("update PostBackEntity set date=:date, time=:time where clickid=:clickId")
                .setParameter("date", date)
                .setParameter("time", time)
                .setParameter("clickId", clickId)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    private MySQLDaoImpl() {}

    public static SessionFactory getMasterDbSessionFactory() {
        return masterDbSessionFactory;
    }

    public static MySQLDaoImpl getInstance() {
        if (instance == null) instance = new MySQLDaoImpl();
        return instance;
    }
}
