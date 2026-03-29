package fr.guillaumewlt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryInfosTest {

    @Test
    void record_shouldStoreAllFields() {
        LibraryInfos lib = new LibraryInfos(
                "com.mojang:authlib:1.5.21",
                "a223667b28f2e5bc2b61a914fe23cc5b8e25e000",
                "com/mojang/authlib/1.5.21/authlib-1.5.21.jar",
                153302L,
                "https://libraries.minecraft.net/com/mojang/authlib/1.5.21/authlib-1.5.21.jar"
        );

        assertEquals("com.mojang:authlib:1.5.21", lib.name());
        assertEquals("a223667b28f2e5bc2b61a914fe23cc5b8e25e000", lib.sha1());
        assertEquals("com/mojang/authlib/1.5.21/authlib-1.5.21.jar", lib.path());
        assertEquals(153302L, lib.size());
        assertEquals("https://libraries.minecraft.net/com/mojang/authlib/1.5.21/authlib-1.5.21.jar", lib.url());
    }

    @Test
    void record_shouldAllowNullFields() {
        LibraryInfos lib = new LibraryInfos(null, null, null, 0L, null);

        assertNull(lib.name());
        assertNull(lib.sha1());
        assertNull(lib.path());
        assertEquals(0L, lib.size());
        assertNull(lib.url());
    }
}