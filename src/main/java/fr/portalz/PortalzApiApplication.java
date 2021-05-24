package fr.portalz;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "Portalz API",
        version = "1.0.0",
        contact = @Contact(
            name = "API Support",
            url = "http://portalz.thibaultdct.fr/contact",
            email = "thibault.doucet44@gmail.com"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    )
)
public class PortalzApiApplication extends Application {
}
