package org.dstadler.commoncrawl.oldindex;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.dstadler.commoncrawl.DocumentLocation;
import org.dstadler.commoncrawl.Utils;
import org.dstadler.commons.http.HttpClientWrapper;

/**
 * Specialized Processor which reads the position in the Common Crawl
 * from the URL in the Block and then downloads and unwraps the actual
 * document in one go.
 *
 * @author dominik.stadler
 */
public class ProcessAndDownload extends ProcessImpl implements Closeable {
    private final HttpClientWrapper client = new HttpClientWrapper("", null, 30_000); 

    public ProcessAndDownload(File file, boolean append) throws IOException {
        super(file, append);
    }

    @Override
    protected void handle(String url, byte[] block, int headerStart, long blockIndex) throws IOException {
        // read location information
        DocumentLocation header = DocumentLocation.readFromOldIndexBlock(block, headerStart);

        Utils.downloadFileFromCommonCrawl(client.getHttpClient(), url, header, false);
    }

    @Override
    public void close() throws IOException {
        super.close();

        client.close();
    }
}