package com.chenjing.transactional.parse;

import com.chenjing.transactional.MultiTransactional;

import javax.sql.DataSource;

/**
 * @author Chenjing
 * @date 2020/9/18
 */
public interface DatasourceParse {

    DataSource[] parse(MultiTransactional multiTransactional);
}
