package com.rohlik.data.commons.objects;
import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UseIdOrGenerate extends IdentityGenerator {
	private static Logger logger = LoggerFactory.getLogger(UseIdOrGenerate.class);
	@Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
        return id != null ? id : super.generate(session, object);
    }
}
