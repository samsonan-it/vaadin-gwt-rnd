package com.samsonan.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Objects;

@Entity
public class Element {

    @Id
    private Long id;

    private String name;

    public Element() {
    }
    
    public Element(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Element [id=" + id + ", name=" + name + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Element other = (Element) obj;
        return Objects.equal(this.id, other.id)
            && Objects.equal(this.name, other.name);
    }
    
        
    
    
}
