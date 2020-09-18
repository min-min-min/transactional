package com.chenjing.transactional.parse;

import com.chenjing.transactional.MultiTransactional;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;

/**
 * @author Chenjing
 * @date 2020/9/18
 */
public class DefaultDatasourceParse implements DatasourceParse, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public DataSource[] parse(MultiTransactional multiTransactional) {
        String[] datasourceNames = multiTransactional.datasourceBeanNames();
        DataSource[] dataSources = new DataSource[datasourceNames.length];
        for (int i = 0; i < datasourceNames.length; i++) {
            DataSource bean = applicationContext.getBean(datasourceNames[i], DataSource.class);
            dataSources[i] = bean;
        }
        return dataSources;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
