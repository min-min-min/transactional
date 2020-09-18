package com.chenjing.transactional.parse;

import com.chenjing.transactional.MultiTransactional;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Chenjing
 * @date 2020/9/18
 */
class DefaultDataSourceParse implements DataSourceParse {

    private final ApplicationContext applicationContext;

    public DefaultDataSourceParse(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public DataSource[] doParse(MultiTransactional multiTransactional) {
        Map<String, DataSource> beans = applicationContext.getBeansOfType(DataSource.class);
        if (CollectionUtils.isEmpty(beans)) {
            throw new NotFoundDataSourceException();
        }
        DataSource[] result = new DataSource[beans.size()];
        return beans.values().toArray(result);
    }
}
