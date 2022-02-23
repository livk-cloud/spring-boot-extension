package com.livk.excel.service.impl;

import com.google.common.collect.Lists;
import com.livk.excel.entity.Info;
import com.livk.excel.mapper.InfoMapper;
import com.livk.excel.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * InfoServiceImpl
 * </p>
 *
 * @author livk
 * @date 2022/1/18
 */
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

    private final SqlSessionFactory sqlSessionFactory;

    @Override
    public void insertBatch(List<Info> records) {
        var sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        var infoMapper = sqlSession.getMapper(InfoMapper.class);
        var partition = Lists.partition(records, 1000);
        int i = 0;
        for (final var infos : partition) {
            try {
                infoMapper.insertBatch(infos);
            } catch (Exception e) {
                e.printStackTrace();
                sqlSession.rollback();
                break;
            }
            i++;
            if (i % 1000 == 0) {
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }
        sqlSession.commit();
        sqlSession.clearCache();
        sqlSession.close();
    }
}
