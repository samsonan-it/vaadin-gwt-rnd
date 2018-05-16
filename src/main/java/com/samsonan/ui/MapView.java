package com.samsonan.ui;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = MapView.VIEW_NAME)
public class MapView extends VerticalLayout implements View {

    /**
     * 
     */
    private static final long serialVersionUID = 2232944723264657746L;
    
    public final static String VIEW_NAME = "Map";
    
    private static final Logger log = LoggerFactory.getLogger(MapView.class);    
    
    @PostConstruct
    void init() {
        
        setSizeFull();
        
        GoogleMap googleMap = new GoogleMap("AIzaSyA2CK25IyV3vUdmTQtyMqNvczotJ7duxho", null, "english");
        googleMap.setSizeFull();
        googleMap.addMarker("DRAGGABLE: Paavo Nurmi Stadion", new LatLon(
                60.442423, 22.26044), true, "VAADIN/1377279006_stadium.png");
        GoogleMapMarker kakolaMarker = googleMap.addMarker("DRAGGABLE: Kakolan vankila",
                new LatLon(60.44291, 22.242415), true, null);
        googleMap.addMarker("NOT DRAGGABLE: Iso-Heikkilä", new LatLon(
                60.450403, 22.230399), false, null);
        googleMap.setMinZoom(4);
        googleMap.setMaxZoom(16);
        
        addComponent(googleMap);
        
        googleMap.addMapClickListener(e -> { 
            log.debug("map clicked: ({}; {})", e.getLat(), e.getLon());

            GoogleMapMarker customMarker = new GoogleMapMarker("marker", new LatLon( e.getLat(), e.getLon()),true, null);
            googleMap.addMarker(customMarker);
        
        });
        
    }
    
}
