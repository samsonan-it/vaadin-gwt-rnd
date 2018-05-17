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
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Using pure Vaadin grids to work with grids and forms.
 */
@SpringView(name = EffectsView.VIEW_NAME)
public class EffectsView extends VerticalLayout implements View {

    private static final long serialVersionUID = -1043036701987685506L;
    private static final Logger log = LoggerFactory.getLogger(EffectsView.class);

    public final static String VIEW_NAME = "Effects";
    
    @Autowired
    private EffectForm editor;

    @Autowired
    private EffectRepository effectRepo;
    
    private Grid<Effect> grid = new Grid<>(Effect.class);
    private Button addNewBtn = new Button("New effect");
    
    public EffectsView() {
        log.debug("EffectsView :: constructor");
    }
    
    void refreshList() {
        grid.setItems(effectRepo.findAll());
    }
    
    @PostConstruct
    void init() {
        log.debug("EffectsView :: init");

        setSizeFull();
        
        VerticalLayout mainLayout = new VerticalLayout(addNewBtn, grid/*, editor*/);
        addComponent(mainLayout);    
        
        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);
        
        grid.setColumns("id", "name");
        grid.addColumn(e -> { 
            Element element = e.getElement(); 
            return element != null ? element.getName() : "-";
        }).setCaption("Element");
        
        grid.asSingleSelect().addValueChangeListener(e -> edit(e.getValue()));

        addNewBtn.addClickListener(e -> edit(new Effect()));

        editor.setSavedHandler(effect -> {
            try {
                this.effectRepo.save(effect);
                Notification.show("Effect successfully saved");
                closeForm();
            } catch (Exception e) {
                Notification.show("Effect could not be saved: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        
        editor.setDeleteHandler(effect -> {
            if (effect.getId() != null) {
                effectRepo.delete(effect);
                closeForm();
            }
        });        

        refreshList();
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        log.debug("EffectsView :: enter");
    }

    private void closeForm() {
        editor.closePopup();
        refreshList();
    }

    private void edit(Effect effect) {
        if (effect != null) {
            editor.setEntity(effect);
            editor.openInModalPopup();
        }
    }
    
}
