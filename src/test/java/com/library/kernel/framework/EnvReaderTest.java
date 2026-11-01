package com.library.kernel.framework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EnvReaderTest {

    @Test
    void readsExactKey() {
        assertEquals("5023", EnvReader.parseLine("DATABASE_PORT=5023", "DATABASE_PORT"));
    }

    @Test
    void doesNotMatchKeysThatOnlySharePrefix() {
        assertNull(EnvReader.parseLine("DATABASE_PORT_EXTRA=1", "DATABASE_PORT"));
    }

    @Test
    void keepsEqualsSignsInsideValue() {
        assertEquals("abc=def==", EnvReader.parseLine("DATABASE_PASSWORD=abc=def==", "DATABASE_PASSWORD"));
    }

    @Test
    void stripsQuotesAndWhitespace() {
        assertEquals("my db", EnvReader.parseLine("  DATABASE_NAME = \"my db\"  ", "DATABASE_NAME"));
    }

    @Test
    void ignoresCommentsAndBlankLines() {
        assertNull(EnvReader.parseLine("# DATABASE_PORT=1", "DATABASE_PORT"));
        assertNull(EnvReader.parseLine("   ", "DATABASE_PORT"));
    }

    @Test
    void returnsEmptyStringForEmptyValue() {
        assertEquals("", EnvReader.parseLine("DATABASE_PASSWORD=", "DATABASE_PASSWORD"));
    }
}
