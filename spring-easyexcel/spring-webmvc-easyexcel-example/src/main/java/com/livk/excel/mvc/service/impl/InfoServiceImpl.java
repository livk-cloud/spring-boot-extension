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
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * <p>
 * InfoServiceImpl
 * </p>
 *
 * @author livk
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

    public void insertBatchMultithreading(List<Info> records) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        InfoMapper infoMapper = sqlSession.getMapper(InfoMapper.class);
        List<List<Info>> partition = Lists.partition(records, (int) Math.pow(1000, 2));
        List<Callable<Integer>> callables = partition.stream()
                .map((Function<List<Info>, Callable<Integer>>) infos -> {
                    List<List<Info>> lists = Lists.partition(infos, 1000);
                    for (List<Info> list : lists) {
                        try {
                            infoMapper.insertBatch(list);
                        } catch (Exception e) {
                            return () -> 0;
                        }
                    }
                    return () -> 1;
                }).toList();
        try {
            List<Future<Integer>> futures = executor.invokeAll(callables);
            for (Future<Integer> future : futures) {
                if (future.get() <= 0) {
                    sqlSession.rollback();
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        sqlSession.commit();
        sqlSession.clearCache();
        sqlSession.close();
    }
}
