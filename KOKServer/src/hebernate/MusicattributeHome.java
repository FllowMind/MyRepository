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
 * Home object for domain model class Musicattribute.
 * @see hebernate.Musicattribute
 * @author Hibernate Tools
 */
public class MusicattributeHome {

	private static final Log log = LogFactory.getLog(MusicattributeHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Musicattribute transientInstance) {
		log.debug("persisting Musicattribute instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Musicattribute instance) {
		log.debug("attaching dirty Musicattribute instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Musicattribute instance) {
		log.debug("attaching clean Musicattribute instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Musicattribute persistentInstance) {
		log.debug("deleting Musicattribute instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Musicattribute merge(Musicattribute detachedInstance) {
		log.debug("merging Musicattribute instance");
		try {
			Musicattribute result = (Musicattribute) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Musicattribute findById(java.lang.String id) {
		log.debug("getting Musicattribute instance with id: " + id);
		try {
			Musicattribute instance = (Musicattribute) sessionFactory.getCurrentSession()
					.get("hebernate.Musicattribute", id);
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

	public List<Musicattribute> findByExample(Musicattribute instance) {
		log.debug("finding Musicattribute instance by example");
		try {
			List<Musicattribute> results = (List<Musicattribute>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Musicattribute").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
