package com.livk.excel.mvc.service;

import com.livk.excel.mvc.entity.Info;

import java.util.List;

/**
 * <p>
 * InfoMapper
 * </p>
 *
 * @author livk
 */
public interface InfoService {

    void insertBatch(List<Info> records);

    void insertBatchMultithreading(List<Info> records);

}
