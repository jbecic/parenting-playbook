package org.launchcode.liftoffproject.models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Tag extends AbstractEntity{

    @NotBlank(message = "Tag is necessary.")
    @Size(max = 50, message = "Tag cannot exceed 50 characters.")
    private String tagName;

    private Boolean checked;

    @ManyToMany(mappedBy = "tags")
    private List<Intervention> interventions;

    public Tag(String tagName) {
        this.tagName = tagName;
        this.checked = false;
    }

    public Tag() {}

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<Intervention> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<Intervention> interventions) {
        this.interventions = interventions;
    }

    @Override
    public String toString() {
        return tagName;
    }
}
