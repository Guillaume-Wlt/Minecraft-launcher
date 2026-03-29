package fr.guillaumewlt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelectedVersionTest {

    @Test
    void record_shouldStoreVersionAndUrl() {
        SelectedVersion sv = new SelectedVersion("1.6.4", "https://example.com/1.6.4.json");

        assertEquals("1.6.4", sv.selectedVersion());
        assertEquals("https://example.com/1.6.4.json", sv.url());
    }

    @Test
    void record_shouldAllowNullFields() {
        SelectedVersion sv = new SelectedVersion(null, null);

        assertNull(sv.selectedVersion());
        assertNull(sv.url());
    }
}