package com.samsonan.ui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.samsonan.model.Effect;
import com.samsonan.model.Element;
import com.samsonan.repository.EffectRepository;
import com.samsonan.repository.ElementRepository;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
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

@SpringComponent
@UIScope
public class EffectEditor extends VerticalLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 6883298487930272053L;

    private static final Logger log = LoggerFactory.getLogger(EffectEditor.class);
    
    private final EffectRepository effectRepo;

    /**
     * The currently edited effect
     */
    private Effect effect;

    // TODO: i18n
    private TextField name = new TextField("Название");
    private ComboBox<Element> elements = new ComboBox<Element>("Элемент");

    /* Action buttons */
    private Button save = new Button("Save", VaadinIcons.CHECK);
    private Button delete = new Button("Delete", VaadinIcons.TRASH);
    
    private CssLayout actions = new CssLayout(save, /*cancel,*/ delete);

    private Binder<Effect> binder = new BeanValidationBinder<>(Effect.class);

    @Autowired
    public EffectEditor(EffectRepository effectRepo, ElementRepository elementRepo) {
        
        log.debug("EffectEditor :: constructor");
        
        this.effectRepo = effectRepo;

        List<Element> allElements = elementRepo.findAll();
        elements.setItems(allElements);
        elements.setItemCaptionGenerator(Element::getName);
        
        addComponents(name, elements, actions);

        binder.bind(name, Effect::getName, Effect::setName);    
        binder.bind(elements, Effect::getElement, Effect::setElement);
        binder.setBean(effect);
        
        // Configure and style components
//        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(event -> {
            try {
                binder.validate();//.writeBean(effect);
                effectRepo.save(effect);
                Notification.show("Effect successfully saved");
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Effect could not be saved: " + e.getMessage());
            }
        });
        
        delete.addClickListener(e -> {
            if (effect.getId() != null) {
                effectRepo.delete(effect);
            }
        });
            
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editEffect(Effect e) {
        
        log.debug("editEffect: e = {}", e);
        
        if (e == null) {
            setVisible(false);
            return;
        }
        
        final boolean persisted = e.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            effect = effectRepo.findById(e.getId()).get();
        } else {
            effect = e;
        }

        delete.setEnabled(persisted);
        
        // Bind properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(effect);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

}
