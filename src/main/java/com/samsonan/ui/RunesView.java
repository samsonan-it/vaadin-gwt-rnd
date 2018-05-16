package com.samsonan.ui;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.samsonan.model.Effect;
import com.samsonan.model.Rune;
import com.samsonan.repository.RuneRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = RunesView.VIEW_NAME)
public class RunesView extends VerticalLayout implements View {

    public final static String VIEW_NAME = "Runes";

    /**
     * 
     */
    private static final long serialVersionUID = 2385192672086551419L;

    private static final Logger log = LoggerFactory.getLogger(EffectsView.class);

    @Autowired
    private RunesEditor editor;

    @Autowired
    private RuneRepository runesRepo;

    private Grid<Rune> grid = new Grid<>(Rune.class);
    private Button addNewBtn;

    // tag::listEffects[]
    void listRunes(String filterText) {
        grid.setItems(runesRepo.findAll());
    }
    // end::listEffects[]

    @PostConstruct
    void init() {

        log.debug("RunesView :: init");

        setSizeFull();

        this.addNewBtn = new Button("New Rune");

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

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editRune(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editRune(new Rune()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            listRunes(null);
            editor.setVisible(false);
        });

        // Initialize listing
        listRunes(null);

        editor.setVisible(false);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        log.debug("RunesView :: enter");
    }

}
