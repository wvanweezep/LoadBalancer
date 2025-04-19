package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.exception.CacheExpiredException;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CacheMapTest {

    private CacheMap status;

    @BeforeEach
    void setUp() {
        status = new CacheMap();
        status.setValue("key1", 1, Instant.now());
        status.setValue("key2", 2, Instant.now());
        status.setValue("key3", 3, Instant.now());
    }

    @Test
    void setValueTest() {
        assertThat(status.getValue("key1")).isEqualTo(1);
        status.setValue("key1", 2, Instant.now());
        assertThat(status.getValue("key1")).isEqualTo(2);

        status.setValue("key4", 4, Instant.now());
        assertThat(status.getValue("key4")).isEqualTo(4);
    }

    @Test
    void setValueInvalidKeyTest() {
        assertThatThrownBy(() -> {
            status.setValue(null, 2, Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setValueInvalidTimestampTest() {
        assertThatThrownBy(() -> {
            status.setValue("key1", 2, Instant.now().minusSeconds(1));
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            status.setValue("key1", 2, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getValueTest() {
        assertThat(status.getValue("key1")).isEqualTo(1);
        assertThat(status.getValue("key2")).isEqualTo(2);
        assertThat(status.getValue("key3")).isEqualTo(3);
    }

    @Test
    void getValueInvalidKeyTest() {
        assertThatThrownBy(() -> {
            status.getValue(null);
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            status.getValue("key4");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getValueStaleDataTest() {
        status.setValue("key4", 4, Instant.now().minusSeconds(11));
        status.setValue("key5", 5, Instant.now().minusSeconds(10));

        assertThatThrownBy(() -> {
            status.getValue("key4");
        }).isInstanceOf(CacheExpiredException.class);

        assertThat(status.getValue("key5")).isEqualTo(5);
    }

    @Test
    void isValueOverdueTest() {
        assertThat(status.isValueOverdue("key1")).isEqualTo(false);
        status.setValue("key4", 4, Instant.now().minusSeconds(11));
        assertThat(status.isValueOverdue("key4")).isEqualTo(true);
    }

    @Test
    void isValueOverdueInvalidKey() {
        assertThatThrownBy(() -> {
            status.isValueOverdue("key4");
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            status.isValueOverdue(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}