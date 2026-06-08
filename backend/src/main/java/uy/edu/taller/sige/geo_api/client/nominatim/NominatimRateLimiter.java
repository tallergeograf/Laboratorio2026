package uy.edu.taller.sige.geo_api.client.nominatim;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import uy.edu.taller.sige.geo_api.client.properties.NominatimProperties;

@Component
public class NominatimRateLimiter {
    private final long minIntervalNanos;
    private final AtomicLong lastCallNanos = new AtomicLong(0);

    public NominatimRateLimiter(NominatimProperties properties) {
        this.minIntervalNanos = properties.getMinIntervalMs() * 1_000_000L;
    }

    public synchronized void acquire() {
        long elapsed = System.nanoTime() - lastCallNanos.get();
        long remaining = minIntervalNanos - elapsed;
        if (remaining > 0) {
            try {
                Thread.sleep(remaining / 1_000_000L, (int) (remaining % 1_000_000L));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastCallNanos.set(System.nanoTime());
    }
}
