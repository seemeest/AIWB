package com.aiwb.marketplace.infrastructure.geoip;

import com.aiwb.marketplace.infrastructure.config.GeoIpProperties;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

@Service
public class GeoIpService {
    private static final Logger log = LoggerFactory.getLogger(GeoIpService.class);
    private final DatabaseReader reader;

    public GeoIpService(GeoIpProperties properties) {
        this.reader = initReader(properties.getDatabasePath());
    }

    public Optional<GeoIpLocation> resolve(String ip) {
        if (reader == null || ip == null || ip.isBlank()) {
            return Optional.empty();
        }
        try {
            InetAddress address = InetAddress.getByName(ip);
            CityResponse response = reader.city(address);
            String country = response.getCountry() == null ? null : response.getCountry().getName();
            String region = response.getMostSpecificSubdivision() == null ? null : response.getMostSpecificSubdivision().getName();
            String city = response.getCity() == null ? null : response.getCity().getName();
            if (country == null && region == null && city == null) {
                return Optional.empty();
            }
            return Optional.of(new GeoIpLocation(country, region, city));
        } catch (IOException | GeoIp2Exception ex) {
            log.warn("GeoIP lookup failed for ip={}", ip, ex);
            return Optional.empty();
        }
    }

    private DatabaseReader initReader(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            log.warn("GeoIP database not found at {}", file.getAbsolutePath());
            return null;
        }
        try {
            return new DatabaseReader.Builder(file).build();
        } catch (IOException ex) {
            log.warn("Failed to initialize GeoIP database", ex);
            return null;
        }
    }
}
