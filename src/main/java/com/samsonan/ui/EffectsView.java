package com.samsonan.ui;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.samsonan.model.Effect;
import com.samsonan.model.Element;
import com.samsonan.repository.EffectRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = EffectsView.VIEW_NAME)
public class EffectsView extends VerticalLayout implements View {

    /**
     * 
     */
    private static final long serialVersionUID = -1043036701987685506L;

    public final static String VIEW_NAME = "Effects";
    
    private static final Logger log = LoggerFactory.getLogger(EffectsView.class);
        
    @Autowired
    private EffectEditor editor;

    @Autowired
    private EffectRepository effectRepo;
    
    private Grid<Effect> grid = new Grid<>(Effect.class);
    private Button addNewBtn;
    
    public EffectsView() {
        log.debug("EffectsView :: constructor");
        
    }
    
    // tag::listEffects[]
    void listEffects(String filterText) {
        grid.setItems(effectRepo.findAll());
    }
    // end::listEffects[]    
    
    @PostConstruct
    void init() {
        log.debug("EffectsView :: init");

        setSizeFull();
        
        this.addNewBtn = new Button("New effect");
        
        VerticalLayout mainLayout = new VerticalLayout(addNewBtn, grid, editor);
        addComponent(mainLayout);    
        
        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);
        
        grid.setColumns("id", "name");
        grid.addColumn(e -> { Element element = e.getElement(); return element != null? element.getName():"-";}).setCaption("Element");
        
        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEffect(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editEffect(new Effect()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            listEffects(null);
            editor.setVisible(false);
        });        

        // Initialize listing
        listEffects(null);
        
        editor.setVisible(false);    
    }
    
    
    @Override
    public void enter(ViewChangeEvent event) {
        log.debug("EffectsView :: enter");
    }


}
