package com.samsonan.ui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.samsonan.model.Effect;
import com.samsonan.model.Element;
import com.samsonan.repository.ElementRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

/**
 * Example of using Viritin components to work with the form.
 */
@SpringComponent
@UIScope
public class EffectForm extends AbstractForm<Effect> {

    private static final long serialVersionUID = -7143759072207190487L;

    private static final Logger log = LoggerFactory.getLogger(EffectForm.class);
    
    // TODO: i18n
    private MTextField name = new MTextField("Название");
    private ComboBox<Element> elements = new ComboBox<Element>("Элемент");

    public EffectForm(ElementRepository elementRepo) {
        
        super(Effect.class);
        
        log.debug("EffectForm :: constructor");

        List<Element> allElements = elementRepo.findAll();
        elements.setItems(allElements);
        elements.setItemCaptionGenerator(Element::getName);
            
        setVisible(false);
    }
    
    @Override
    protected void bind() {

        getBinder().bind(name, Effect::getName, Effect::setName);
        getBinder().bind(elements, Effect::getElement, Effect::setElement);

        super.bind();
    }
    
    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        elements
                ).withWidth(""),
                getToolbar()
        ).withWidth("");

    }
    
    
    

}
