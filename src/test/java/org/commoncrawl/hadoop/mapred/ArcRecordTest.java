package org.commoncrawl.hadoop.mapred;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.archive.io.arc.ARCRecord;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.*;

public class ArcRecordTest {

	@Test
	public void testArcRecord() throws Exception {
        ArcRecord record = new ArcRecord();
        try (InputStream stream = new GZIPInputStream(new FileInputStream("src/test/data/record.bin"))) {
        	record.readFrom(stream);

			assertNotNull(record.getHttpResponse());
			assertNotNull(record.getHttpResponse().getEntity());
			InputStream content = record.getHttpResponse().getEntity().getContent();
			assertNotNull(content);
			ByteArrayOutputStream outstr = new ByteArrayOutputStream();
			IOUtils.copy(content, outstr);
			byte[] data = outstr.toByteArray();
			assertTrue(data.length > 1);

			// had a problem that ArcRecord did leave a trailing newline in there
			assertFalse(data[data.length-1] == '\n');
        }
	}
	
	@Ignore("The resulting file is not correctly written, HTTP Headers are ignored!")
	@Test
	public void testArcRecordCommons() throws Exception {
        try (InputStream stream = new GZIPInputStream(new FileInputStream("src/test/data/record.bin"))) {
            try (ARCRecord record = new ARCRecord(stream, "name", 0, true, true, true)) {
            	assertEquals(72, record.read());
            }
        }
	}
}
