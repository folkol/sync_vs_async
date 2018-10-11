package com.folkol.resources;

import com.folkol.VaryingContentService;
import com.folkol.model.Content;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContentResource {
    private VaryingContentService contentService;

    public ContentResource(VaryingContentService contentService) {
        this.contentService = contentService;
    }

    @PUT
    @Path("sync/{id}")
    public void put(@PathParam("id") String id,
                    Content content)
    {
        contentService.updateContent(id, content);
    }

    @GET
    @Path("sync/{id}")
    public Content get(@PathParam("id") String id,
                       @QueryParam("variant") @DefaultValue("") String variant)
    {
        return contentService.getContentSync(id, variant);
    }

    @GET
    @Path("async/{id}")
    public void get(@Suspended AsyncResponse ar,
                    @PathParam("id") String id,
                    @QueryParam("variant") String variant)
    {
        contentService.getContentAsync(id, variant).subscribe(ar::resume, ar::resume);
    }
}
