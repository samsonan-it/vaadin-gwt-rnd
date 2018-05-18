package com.samsonan.ui;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.samsonan.model.Effect;
import com.samsonan.model.Rune;
import com.samsonan.repository.EffectRepository;
import com.samsonan.repository.RuneRepository;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Using pure vaadin (No viritin) and spring-vaadin event bus to de-couple Grid View and the Form
 */
@SpringComponent
@UIScope
public class RunesForm extends VerticalLayout {

    private static final long serialVersionUID = 3124506745817456455L;
    private static final Logger log = LoggerFactory.getLogger(RunesForm.class);
    
    private final RuneRepository runeRepo;    
    private final EffectRepository effectRepo;    
    private final EventBus.UIEventBus eventBus;

    
    /**
     * The currently edited rune
     */
    private Rune rune;
    
    // TODO: i18n
    private TextField name = new TextField("Название");
    private TextField latinName = new TextField("Лат. название");
    private ComboBox<Effect> effects = new ComboBox<Effect>("Эффект");    
    private TextField effectVal = new TextField("Значение эффекта");
    
    /* Action buttons */
    private Button save = new Button("Save", VaadinIcons.CHECK);
    private Button delete = new Button("Delete", VaadinIcons.TRASH);    

    private CssLayout actions = new CssLayout(save, delete);

    private Binder<Rune> binder = new BeanValidationBinder<>(Rune.class);    

    @Autowired
    public RunesForm(RuneRepository runeRepo, EffectRepository effectRepo, EventBus.UIEventBus b) {
        
        log.debug("RunesForm :: constructor");
        
        this.runeRepo = runeRepo;
        this.effectRepo = effectRepo;
        this.eventBus = b;
        
        effects.setItemCaptionGenerator(Effect::getName);
        
        effects.addSelectionListener(event -> {
            effectVal.setVisible(event.getSelectedItem().isPresent());
        });
                
        addComponents(name, latinName, effects, effectVal, actions);

        binder.bind(name, Rune::getName, Rune::setName);    
        binder.bind(latinName, Rune::getLatinName, Rune::setLatinName);    
        binder.bind(effects, Rune::getEffect, Rune::setEffect);
        binder.bind(effectVal, Rune::getEffectValStr, Rune::setEffectValStr);    
        
        binder.setBean(rune);
        
        // Configure and style components
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(event -> {
            try {
                binder.validate();//.writeBean(effect);
                runeRepo.save(rune);
                Notification.show("Rune successfully saved");

                eventBus.publish(this, new RunesView.RuneModifiedEvent());
                
            } catch (Exception e) {
                Notification.show("Rune could not be saved: " + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        
        delete.addClickListener(e -> {
            if (rune.getId() != null) {
                runeRepo.delete(rune);
                eventBus.publish(this, new RunesView.RuneModifiedEvent());
            }
        });
            
        setVisible(false);
    }        
    
    public final void editRune(Rune r) {
        log.debug("editRune: r = {}", r);

        effectVal.setVisible(r != null && r.getEffect() != null);
        
        if (r == null) {
            setVisible(false);
            return;
        }
        
        List<Effect> allEffects = effectRepo.findAll();
        effects.setItems(allEffects);        
        
        final boolean persisted = r.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            rune = runeRepo.findById(r.getId()).get();
        } else {
            rune = r;
        }

        delete.setEnabled(persisted);
        
        binder.setBean(rune);

        setVisible(true);

        save.focus();
    }

}
