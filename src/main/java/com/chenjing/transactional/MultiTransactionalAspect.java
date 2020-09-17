package com.chenjing.transactional;

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
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Chenjing
 * @date 2020/9/17
 */
@Aspect
public class MultiTransactionalAspect implements ApplicationContextAware, Transaction {

    private static final Logger log = LoggerFactory.getLogger(MultiTransactionalAspect.class);

    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.chenjing.transactional.MultiTransactional)")
    public void transactionalPointCut() {
    }

    @Around("transactionalPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("Into multi transactional aspect point cut...");
        MethodSignature signature = (MethodSignature) point.getSignature();
        MultiTransactional transactional = signature.getMethod().getAnnotation(MultiTransactional.class);

        String[] datasourceNames = transactional.datasourceNames();
        DataSource[] dataSources = new DataSource[datasourceNames.length];
        for (int i = 0; i < datasourceNames.length; i++) {
            DataSource bean = applicationContext.getBean(datasourceNames[i], DataSource.class);
            dataSources[i] = bean;
        }
        Object result;
        try {
            TransactionSynchronizationManager.initSynchronization();
            log.info("Begin...");
            beginTransaction(getConnections(dataSources));
            result = point.proceed();
            commit(getConnections(dataSources));
            log.debug("Commit...");
        } catch (Exception e) {
            log.error("Rollback...");
            rollback(getConnections(dataSources));
            throw e;
        }
        return result;
    }

    private Connection[] getConnections(DataSource[] dataSources) {
        Connection[] connections = new Connection[dataSources.length];

        for (int i = 0; i < dataSources.length; i++) {
            Connection connection = DataSourceUtils.getConnection(dataSources[0]);
            connections[i] = connection;
        }
        return connections;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void commit(Connection[] connections) throws SQLException {
        for (Connection connection : connections) {
            connection.commit();
        }
    }

    @Override
    public void beginTransaction(Connection[] connections) throws SQLException {
        for (Connection connection : connections) {
            connection.setAutoCommit(false);
        }
    }

    @Override
    public void rollback(Connection[] connections) throws SQLException {
        for (Connection connection : connections) {
            connection.rollback();
        }
    }
}
