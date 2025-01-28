package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.MultivaluedMap;

import java.net.URI;
import java.net.URISyntaxException;

@Path("/proxy")
public class CORSProxyResource {

    private static final Client client = ClientBuilder.newClient();

    @GET
    @Path("/{path: .*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response proxyGet(@jakarta.ws.rs.PathParam("path") String path, @jakarta.ws.rs.core.Context HttpHeaders headers) {
        try {
            URI targetUri = new URI("https://polar-crag-35639-b43d5e9f6815.herokuapp.com/" + path);
            MultivaluedMap<String, Object> requestHeaders = (MultivaluedMap<String, Object>) (MultivaluedMap) headers.getRequestHeaders();
            Response targetResponse = client.target(targetUri)
                                            .request()
                                            .headers(requestHeaders)
                                            .get();

            return buildResponse(targetResponse);
        } catch (URISyntaxException e) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid URI").build();
        }
    }

    @POST
    @Path("/{path: .*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response proxyPost(@jakarta.ws.rs.PathParam("path") String path, String body, @jakarta.ws.rs.core.Context HttpHeaders headers) {
        try {
            URI targetUri = new URI("https://polar-crag-35639-b43d5e9f6815.herokuapp.com/" + path);
            MultivaluedMap<String, Object> requestHeaders = (MultivaluedMap<String, Object>) (MultivaluedMap) headers.getRequestHeaders();
            Response targetResponse = client.target(targetUri)
                                            .request()
                                            .headers(requestHeaders)
                                            .post(Entity.json(body));

            return buildResponse(targetResponse);
        } catch (URISyntaxException e) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid URI").build();
        }
    }

    private Response buildResponse(Response targetResponse) {
        Response.ResponseBuilder responseBuilder = Response.status(targetResponse.getStatus())
                                                           .entity(targetResponse.getEntity());

        targetResponse.getHeaders().forEach((key, value) -> responseBuilder.header(key, value));

        responseBuilder.header("Access-Control-Allow-Origin", "*")
                       .header("Access-Control-Allow-Credentials", "true")
                       .header("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, OPTIONS")
                       .header("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
                       .header("Vary", "Origin");

        return responseBuilder.build();
    }
}
