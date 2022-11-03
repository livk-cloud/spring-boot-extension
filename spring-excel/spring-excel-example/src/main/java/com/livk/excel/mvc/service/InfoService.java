package com.livk.excel.mvc.service;

import com.livk.excel.mvc.entity.Info;

import java.util.List;

/**
 * <p>
 * InfoMapper
 * </p>
 *
 * @author livk
 * @date 2022/1/12
 */
public interface InfoService {

    void insertBatch(List<Info> records);

}
