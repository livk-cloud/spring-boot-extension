package com.livk.export.service.impl;

import com.livk.export.service.AuthorsService;
import com.livk.export.support.CustomResultHandler;
import com.livk.export.support.DownloadProcessor;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

/**
 * @author livk
 */
@Service
@RequiredArgsConstructor
public class AuthorsServiceImpl implements AuthorsService {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void download(PrintWriter writer) {
        CustomResultHandler customResultHandler = new CustomResultHandler(new DownloadProcessor(writer));
        sqlSessionTemplate.select("com.livk.export.mapper.AuthorsMapper.select", customResultHandler);
        writer.flush();
    }
}
