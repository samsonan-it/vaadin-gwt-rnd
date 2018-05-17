package com.samsonan.ui;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import com.samsonan.model.Effect;
import com.samsonan.model.Rune;
import com.samsonan.repository.RuneRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

/**
 * Using spring-vaadin event bus to de-couple Grid View and the Form
 */
@SpringView(name = RunesView.VIEW_NAME)
public class RunesView extends VerticalLayout implements View {

    private static final long serialVersionUID = 2385192672086551419L;
    private static final Logger log = LoggerFactory.getLogger(EffectsView.class);
    
    public final static String VIEW_NAME = "Runes";

    @Autowired
    private RunesForm editor;

    @Autowired
    private RuneRepository runesRepo;

    @Autowired
    private EventBus.UIEventBus eventBus;    
    
    private Grid<Rune> grid = new Grid<>(Rune.class);
    private Button addNewBtn = new Button("New Rune");

    @PostConstruct
    void init() {

        log.debug("RunesView :: init");

        setSizeFull();

        VerticalLayout mainLayout = new VerticalLayout(addNewBtn, grid, editor);
        addComponent(mainLayout);

        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);

        grid.setColumns("id", "name", "latinName", "pointOrder");

        grid.addColumn(e -> {
            Effect effect = e.getEffect();
            return effect != null ? effect.getName() : "-";
        }).setCaption("Effect");
        
        grid.addColumn("effectVal");


        grid.asSingleSelect().addValueChangeListener(e -> editor.editRune(e.getValue()));
        addNewBtn.addClickListener(e -> editor.editRune(new Rune()));

        // Initialize listing
        refreshList();

        editor.setVisible(false);
        
        eventBus.subscribe(this);
    }

    void refreshList() {
        grid.setItems(runesRepo.findAll());
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onRuneModified(RuneModifiedEvent event) {
        log.debug("RunesView :: onRuneModified event");
        refreshList();
        editor.setVisible(false);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        log.debug("RunesView :: enter");
    }

    static class RuneModifiedEvent implements Serializable {
        private static final long serialVersionUID = 1L;
    }
    
}
