package net.kokkeli.server;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Jakson json provider provider
 * @author Hekku2
 *
 */
public class JacksonJsonProviderProvider implements Provider<JacksonJsonProvider> {
    private final ObjectMapper mapper;

    @Inject
    JacksonJsonProviderProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonJsonProvider get() {
        return new JacksonJsonProvider(mapper);
    }
}