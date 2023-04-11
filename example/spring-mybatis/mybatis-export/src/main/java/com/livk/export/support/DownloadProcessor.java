package com.livk.export.support;

import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class DownloadProcessor {

    private final PrintWriter writer;

    public <E> void processData(E record) {
        writer.write(record.toString());
        writer.write("\n");
    }
}
