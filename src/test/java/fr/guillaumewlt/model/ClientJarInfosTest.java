package fr.guillaumewlt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientJarInfosTest {

    @Test
    void record_shouldStoreUrlSha1AndSize() {
        ClientJarInfos infos = new ClientJarInfos(
                "https://example.com/client.jar",
                "abc123sha1hash",
                4531991L
        );

        assertEquals("https://example.com/client.jar", infos.url());
        assertEquals("abc123sha1hash", infos.sha1());
        assertEquals(4531991L, infos.size());
    }

    @Test
    void record_shouldAllowNullUrlAndSha1() {
        ClientJarInfos infos = new ClientJarInfos(null, null, 0L);

        assertNull(infos.url());
        assertNull(infos.sha1());
        assertEquals(0L, infos.size());
    }
}