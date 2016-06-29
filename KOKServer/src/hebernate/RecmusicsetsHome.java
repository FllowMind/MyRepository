package hebernate;
// Generated 2016-6-27 19:09:29 by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Recmusicsets.
 * @see hebernate.Recmusicsets
 * @author Hibernate Tools
 */
public class RecmusicsetsHome {

	private static final Log log = LogFactory.getLog(RecmusicsetsHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Recmusicsets transientInstance) {
		log.debug("persisting Recmusicsets instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Recmusicsets instance) {
		log.debug("attaching dirty Recmusicsets instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Recmusicsets instance) {
		log.debug("attaching clean Recmusicsets instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Recmusicsets persistentInstance) {
		log.debug("deleting Recmusicsets instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Recmusicsets merge(Recmusicsets detachedInstance) {
		log.debug("merging Recmusicsets instance");
		try {
			Recmusicsets result = (Recmusicsets) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Recmusicsets findById(java.lang.String id) {
		log.debug("getting Recmusicsets instance with id: " + id);
		try {
			Recmusicsets instance = (Recmusicsets) sessionFactory.getCurrentSession().get("hebernate.Recmusicsets", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Recmusicsets> findByExample(Recmusicsets instance) {
		log.debug("finding Recmusicsets instance by example");
		try {
			List<Recmusicsets> results = (List<Recmusicsets>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Recmusicsets").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
