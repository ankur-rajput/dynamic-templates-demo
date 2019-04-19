/*
 * Model class for populating the authorable columnControl component fields.
 */
package edu.ttn.templates.demo.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    resourceType = {ColumnControlModel.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class ColumnControlModel {

    protected static final String RESOURCE_TYPE = "dynamic-templates-demo/components/content/columncontrol";
    private static final String COLUMN_CONTROL_FIELD_NAME = "column";

    private static final Logger LOGGER = LoggerFactory.getLogger(ColumnControlModel.class);

    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Resource columnControl;

    private List<Long> columns = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (columnControl != null) {

            for (Resource value : columnControl.getChildren()) {
                Long column = value.adaptTo(ValueMap.class).get(COLUMN_CONTROL_FIELD_NAME, Long.class);
                columns.add(column);
            }

        } else {
            LOGGER.debug("Generate Column Control is null");
        }
    }

    public List<Long> getColumns() {
        return columns;

    }

}
