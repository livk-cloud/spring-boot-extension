package com.livk.excel.mvc.service.impl;

import com.google.common.collect.Lists;
import com.livk.excel.mvc.entity.Info;
import com.livk.excel.mvc.mapper.InfoMapper;
import com.livk.excel.mvc.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
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
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        InfoMapper infoMapper = sqlSession.getMapper(InfoMapper.class);
        List<List<Info>> partition = Lists.partition(records, 1000);
        int i = 0;
        for (List<Info> infos : partition) {
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
