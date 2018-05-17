package com.samsonan.ui;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = MapView.VIEW_NAME)
public class MapView extends VerticalLayout implements View {

    /**
     * 
     */
    private static final long serialVersionUID = 2232944723264657746L;

    @Value("${google.maps.api}")
    private String googleMapApi;
    
    private Label latLbl; 
    private Label lonLbl; 
    
    public final static String VIEW_NAME = "Map";
    
    private static final Logger log = LoggerFactory.getLogger(MapView.class);    
    
    @PostConstruct
    void init() {
        
        setSizeFull();
        
        GoogleMap googleMap = new GoogleMap(googleMapApi, null, "english");
        googleMap.setSizeFull();
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        
        addComponent(googleMap);

        initFloatingwindow();
        
        googleMap.addMapClickListener(e -> { 
            log.debug("map clicked: ({}; {})", e.getLat(), e.getLon());

            GoogleMapMarker customMarker = new GoogleMapMarker("marker", new LatLon( e.getLat(), e.getLon()),true, null);
            
            latLbl.setValue(e.getLat() + "");
            lonLbl.setValue(e.getLon() + "");
            
            googleMap.addMarker(customMarker);
        
        });
        
    }
    
    private void initFloatingwindow() {
        Window mapToolBox = new Window("Map Tool Box");
        mapToolBox.setClosable(false);
        mapToolBox.setResizable(false);
        mapToolBox.setPosition(10, 100);
        mapToolBox.setWidth("350px");
        mapToolBox.setHeight("250px");
        mapToolBox.addStyleName("mywindowstyle");
        UI.getCurrent().addWindow(mapToolBox);        

        VerticalLayout toolLayout = new VerticalLayout();
        toolLayout.setMargin(true);
        toolLayout.setSpacing(true);
        mapToolBox.setContent(toolLayout);
        
        ComboBox<String> type = new ComboBox<String>("Тип");    
        Button save = new Button("Save", VaadinIcons.CHECK);
        Button delete = new Button("Delete", VaadinIcons.TRASH);    

        type.setItems("Magic","Tech");
        
        CssLayout actions = new CssLayout(save, delete);        

        latLbl = new Label();
        lonLbl = new Label();        
        
        toolLayout.addComponent(latLbl);
        toolLayout.addComponent(lonLbl);
        toolLayout.addComponent(type);
        toolLayout.addComponent(actions);
    }
    
}
