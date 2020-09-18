package com.chenjing.transactional.parse;

import com.chenjing.transactional.MultiTransactional;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

/**
 * @author Chenjing
 * @date 2020/9/18
 */
public class DataSourceParseSummary {

    private final ApplicationContext applicationContext;

    public DataSourceParseSummary(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DataSource[] parse(MultiTransactional transactional) {
        if (transactional.datasourceBeanNames().length > 0 && transactional.value().length > 0) {
            throw new IllegalArgumentException("Please choose one attr config datasource name");
        }
        DataSourceParse dataSourceParse = transactional.datasourceBeanNames().length == 0 ?
                new DefaultDataSourceParse(applicationContext) :
                new BeanNameDataSourceParse(applicationContext);
        return dataSourceParse.doParse(transactional);
    }
}
