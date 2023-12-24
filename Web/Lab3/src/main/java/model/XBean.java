package model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Managed bean for handling X coordinate value in JSF application.
 * Provides functionality to get and set X value, and to validate it.
 */
@Data
@NoArgsConstructor
@Named
@ApplicationScoped
public class XBean implements Serializable {
    private Double x = 0.0;

    public void validateXBeanValue(FacesContext facesContext,
                                   UIComponent uiComponent, Object o){
        if (o == null){
            FacesMessage message = new FacesMessage("Input X!");
            throw new ValidatorException(message);
        }
    }
}
