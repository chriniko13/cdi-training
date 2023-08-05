package com.example.rest;

import com.example.infra.MachineSpecProvider;
import com.example.infra.MachineSpecProviderBasicAnnotationLiteral;
import com.example.interceptor.statistic.StatisticsAware;
import com.example.interceptor.statistic.StatisticsEndpointName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;
import org.jboss.logging.Logger;


/**
 * Provides an example on how to use: <code>CDI.current().select(...)</code>
 * in order to retrieve a cdi managed bean.
 */
@Path("/cdi-current")
@ApplicationScoped
public class CdiCurrentShowcaseResource {

    private static final Logger LOG = Logger.getLogger(CdiCurrentShowcaseResource.class);


    @StatisticsAware
    @StatisticsEndpointName("/cdi-current")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpec() {

        MachineSpecProvider machineSpecProvider = CDI.current().select(
              MachineSpecProvider.class,
              MachineSpecProviderBasicAnnotationLiteral.INSTANCE
        ).get();

        String jvmName = machineSpecProvider.getJvmName();
        return Response.ok(
              Map.of("jvmName", jvmName)
        ).build();

    }
}
