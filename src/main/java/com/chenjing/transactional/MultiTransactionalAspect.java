package com.chenjing.transactional;

import com.chenjing.transactional.parse.DataSourceParseSummary;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author Chenjing
 * @date 2020/9/17
 */
@Aspect
public class MultiTransactionalAspect implements Transaction, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(MultiTransactionalAspect.class);

    @Pointcut("@annotation(com.chenjing.transactional.MultiTransactional)")
    public void transactionalPointCut() {
    }

    @Around("transactionalPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.debug("Into multi transactional aspect point cut...");
        MethodSignature signature = (MethodSignature) point.getSignature();
        MultiTransactional transactional = signature.getMethod().getAnnotation(MultiTransactional.class);
        DataSource[] dataSources = new DataSourceParseSummary(applicationContext).parse(transactional);

        Object result;
        List<TransactionSynchronization> synchronizations = Collections.emptyList();
        try {
            TransactionSynchronizationManager.initSynchronization();
            log.debug("Multi transaction begin...");
            beginTransaction(dataSources);
            result = point.proceed();
            commit(dataSources);
            log.debug("Multi transaction commit...");
            synchronizations = TransactionSynchronizationManager.getSynchronizations();
            synchronizations.forEach(TransactionSynchronization::beforeCompletion);
        } catch (Exception e) {
            log.info("Multi transaction rollback...");
            rollback(dataSources);
            throw e;
        } finally {
            log.debug("Multi transaction release connection...");
            synchronizations.forEach(x -> x.afterCompletion(TransactionSynchronization.STATUS_UNKNOWN));
        }
        return result;
    }

    private Connection[] getConnections(DataSource[] dataSources) {
        Connection[] connections = new Connection[dataSources.length];

        for (int i = 0; i < dataSources.length; i++) {
            Connection connection = DataSourceUtils.getConnection(dataSources[i]);
            connections[i] = connection;
        }
        return connections;
    }

    @Override
    public void commit(DataSource[] dataSources) throws SQLException {
        Connection[] connections = getConnections(dataSources);
        for (Connection connection : connections) {
            connection.commit();
        }
    }

    @Override
    public void beginTransaction(DataSource[] dataSources) throws SQLException {
        Connection[] connections = getConnections(dataSources);
        for (Connection connection : connections) {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
        }
    }

    @Override
    public void rollback(DataSource[] dataSources) throws SQLException {
        Connection[] connections = getConnections(dataSources);
        for (Connection connection : connections) {
            connection.rollback();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
