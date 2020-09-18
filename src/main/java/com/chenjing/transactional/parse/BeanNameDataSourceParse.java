package com.chenjing.transactional.parse;

import com.chenjing.transactional.MultiTransactional;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * @author Chenjing
 * @date 2020/9/18
 */
class BeanNameDataSourceParse implements DataSourceParse {

    private final ApplicationContext applicationContext;

    public BeanNameDataSourceParse(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public DataSource[] doParse(MultiTransactional multiTransactional) {
        String[] datasourceNames = multiTransactional.datasourceBeanNames();
        if (datasourceNames.length == 0) {
            throw new NotFoundDataSourceException();
        }
        DataSource[] dataSources = new DataSource[datasourceNames.length];
        for (int i = 0; i < datasourceNames.length; i++) {
            DataSource bean = applicationContext.getBean(datasourceNames[i], DataSource.class);
            dataSources[i] = bean;
        }
        return dataSources;
    }
}
