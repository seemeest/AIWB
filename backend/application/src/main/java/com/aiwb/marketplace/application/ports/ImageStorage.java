package com.aiwb.marketplace.application.ports;

import java.io.InputStream;

public interface ImageStorage {
    String store(String directory, String filename, InputStream content);
}
