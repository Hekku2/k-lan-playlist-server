package net.kokkeli.server;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Provider;

/**
 * Object mapper provider
 * @author Hekku2
 */
public class ObjectMapperProvider implements Provider<ObjectMapper> {
    @Override
    public ObjectMapper get() {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }
}
