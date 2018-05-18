package com.samsonan.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;

/**
 * Example of using Viritin components to work with the form.
 */
@SpringComponent
@UIScope
public class EffectsForm extends AbstractForm<Effect> {

    private static final long serialVersionUID = -7143759072207190487L;

    private static final Logger log = LoggerFactory.getLogger(EffectsForm.class);
    
    private final static String ALLOWED_MIME = "image/jpeg";
    
    // TODO: i18n
    private MTextField name = new MTextField("Название");
    private ComboBox<Element> elements = new ComboBox<Element>("Элемент");
    private Image image = new Image("Uploaded Image");
    
    public EffectsForm(ElementRepository elementRepo) {
        
        super(Effect.class);
        
        log.debug("EffectForm :: constructor");

        image.setWidth("200px");
        image.setHeight("200px");
        
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
        
        log.debug("EffectForm :: createContent");

        ImageUploader receiver = new ImageUploader();

        Upload upload = new Upload("Upload it here", receiver);
        upload.addSucceededListener(receiver);
        upload.setImmediateMode(false);        
        
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        elements,
                        upload,
                        image
                ).withWidth(""),
                getToolbar()
        ).withWidth("");

    }

    public class ImageUploader implements Upload.Receiver, Upload.SucceededListener {

        private static final long serialVersionUID = 8787130901695842244L;

        // tmp file
        File file;
        
        @Override
        public void uploadSucceeded(SucceededEvent event) {
            image.setSource(new FileResource(file));
            
            try {
                getEntity().setImageContent(Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                // TODO
                log.error("Cannot readcontent of a file {}", file.getAbsolutePath(), e);
            }
        }

        @Override
        public OutputStream receiveUpload(String fileName, String mimeType) {
            try {
                if (!ALLOWED_MIME.equals(mimeType)) {
                    Notification.show("Only " + ALLOWED_MIME + " files are allowed. Your type is " + mimeType, Notification.Type.ERROR_MESSAGE);
                    return null;
                }
                
                file = new File(fileName);
                log.debug("ImageUploader :: file received. location: {}; mine:{}", file.getAbsolutePath(), mimeType);
                return new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                // TODO
                log.error("Cannot upload file {}", fileName, e);
                Notification.show("Cannot upload file " + fileName, Notification.Type.ERROR_MESSAGE);
                return null;
            }
        }

    }

    
    

}
