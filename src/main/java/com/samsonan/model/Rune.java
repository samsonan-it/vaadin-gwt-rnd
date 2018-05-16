package com.samsonan.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Rune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(name="LATIN_NAME")
    private String latinName;

    @Column(name="POINT_ORDER")
    private String pointOrder;

    @ManyToOne
    @JoinColumn(name="EFFECT_ID")    
    private Effect effect;

    private int effectVal;
    
    public Rune() {
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

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getPointOrder() {
        return pointOrder;
    }

    public void setPointOrder(String pointOrder) {
        this.pointOrder = pointOrder;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int getEffectVal() {
        return effectVal;
    }

    public void setEffectVal(int effectVal) {
        this.effectVal = effectVal;
    }
  
    public String getEffectValStr() {
        return Integer.toString(effectVal);
    }

    public void setEffectValStr(String effectVal) {
        this.effectVal = Integer.parseInt(effectVal);
    }
    
    
}
