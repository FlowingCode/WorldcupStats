package com.flowingcode.fixture.view.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.flowingcode.addons.applayout.PaperCard;
import com.flowingcode.fixture.view.component.MarkedElement;
import com.flowingcode.fixture.view.presenter.WelcomePresenter;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@SuppressWarnings("serial")
@Route(value="about", layout=MainLayout.class)
public class AboutScreen extends VerticalLayout {

	@Autowired
	public AboutScreen(final WelcomePresenter presenter) {
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        
        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vl.add(new H4("Worldcup Stats Vaadin 10 Demo Application"));
        String markdown = "This is a demo application to try some new technologies:\r\n" + 
        		"* Vaadin 10. You can learn more about it in [Vaadin's official site](https://www.vaadin.com/docs).\r\n" + 
        		"* Spring framework. Find out more [here](https://spring.io/).\r\n" + 
        		"* App Layout Addon for Vaadin 10. Find out more [here](https://vaadin.com/directory/component/app-layout-addon).\r\n" + 
        		"* Polymer. Some components from [here](https://www.webcomponents.org), that you can easily integrate in Vaadin 10\r\n\r\n" +
        		"This application consumes the API from [http://worldcup.sfg.io/](http://worldcup.sfg.io/)\\r\\n\\r\\n" + 
        		"Developed by [Flowing Code S.A.](https://www.flowingcode.com)";
        
        vl.add(new MarkedElement(markdown));
        
        PaperCard pc = new PaperCard(vl);
        
        this.add(pc);
        
	}
	
}
