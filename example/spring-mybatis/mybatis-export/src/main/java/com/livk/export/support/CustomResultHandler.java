package com.livk.export.support;

import com.livk.export.entity.Authors;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * @author livk
 */
public class CustomResultHandler implements ResultHandler<Authors> {

    private final DownloadProcessor downloadProcessor;

    public CustomResultHandler(DownloadProcessor downloadProcessor) {
        this.downloadProcessor = downloadProcessor;
    }

    @Override
    public void handleResult(ResultContext<? extends Authors> resultContext) {
        Authors authors = resultContext.getResultObject();
        downloadProcessor.processData(authors);
    }
}
