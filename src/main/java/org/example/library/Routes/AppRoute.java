package org.example.library.Routes;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("app")
@AnonymousAllowed
public class AppRoute extends Div {
    public AppRoute() {
        IFrame iframe = new IFrame("/react/index.html#/");
        iframe.setSizeFull();
        iframe.getElement().getStyle()
                .set("border", "none")
                .set("width", "100%")
                .set("height", "100vh");
        add(iframe);
    }
}
