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
 * Home object for domain model class Artistinfo.
 * @see hebernate.Artistinfo
 * @author Hibernate Tools
 */
public class ArtistinfoHome {

	private static final Log log = LogFactory.getLog(ArtistinfoHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Artistinfo transientInstance) {
		log.debug("persisting Artistinfo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Artistinfo instance) {
		log.debug("attaching dirty Artistinfo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Artistinfo instance) {
		log.debug("attaching clean Artistinfo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Artistinfo persistentInstance) {
		log.debug("deleting Artistinfo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Artistinfo merge(Artistinfo detachedInstance) {
		log.debug("merging Artistinfo instance");
		try {
			Artistinfo result = (Artistinfo) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Artistinfo findById(java.lang.String id) {
		log.debug("getting Artistinfo instance with id: " + id);
		try {
			Artistinfo instance = (Artistinfo) sessionFactory.getCurrentSession().get("hebernate.Artistinfo", id);
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

	public List<Artistinfo> findByExample(Artistinfo instance) {
		log.debug("finding Artistinfo instance by example");
		try {
			List<Artistinfo> results = (List<Artistinfo>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Artistinfo").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
