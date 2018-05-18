package com.samsonan.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    @Autowired  
    private SpringViewProvider viewProvider;      
    
    /**
     * 
     */
    private static final long serialVersionUID = -4584994477743310118L;
    
    @Override
    protected void init(VaadinRequest request) {
        
        final VerticalLayout root = new VerticalLayout();  
//        final HorizontalLayout root = new HorizontalLayout();  
        root.setSizeFull();  
        setContent(root);
        
        /**
         * Init Nav Bar
         */
        final CssLayout navigationBar = new CssLayout();  
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
//      navigationBar.addStyleName(ValoTheme.MENU_ROOT);
      
        navigationBar.addComponent(createNavButton("Effects", EffectsView.VIEW_NAME));          
        navigationBar.addComponent(createNavButton("Runes", RunesView.VIEW_NAME));  
        navigationBar.addComponent(createNavButton("Map", MapView.VIEW_NAME));  
          
        root.addComponent(navigationBar);  
  
//      CssLayout viewContainer = new CssLayout();
        final Panel viewContainer = new Panel();  
        viewContainer.setSizeFull();  
        root.addComponent(viewContainer);  
        root.setExpandRatio(viewContainer, 1.0f);  
  
        Navigator navigator = new Navigator(this, viewContainer);  
        navigator.addProvider(viewProvider);
        navigator.addView("", new EffectsView()); // default view - this way it won't work, but at least there will be no errors
    }

    private Button createNavButton(String caption, final String viewName) {  
        Button button = new Button(caption);  
        button.addStyleName(ValoTheme.BUTTON_SMALL);
//      button.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));  
        return button;  
    } 
    


    
}