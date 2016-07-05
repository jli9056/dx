package com.dixin.hibernate;

import com.dixin.annotation.Name;



/**
 * AbstractAvailable entity provides the base persistence definition of the Available entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractAvailable extends BaseJDO implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Factoryorderdetail factoryorderdetail;
     private Orderdetail orderdetail;
     private Integer consumedCount;


    // Constructors

    /** default constructor */
    public AbstractAvailable() {
    }

	/** minimal constructor */
    public AbstractAvailable(Factoryorderdetail factoryorderdetail, Orderdetail orderdetail) {
        this.factoryorderdetail = factoryorderdetail;
        this.orderdetail = orderdetail;
    }
    
    /** full constructor */
    public AbstractAvailable(Factoryorderdetail factoryorderdetail, Orderdetail orderdetail, Integer consumedCount) {
        this.factoryorderdetail = factoryorderdetail;
        this.orderdetail = orderdetail;
        this.consumedCount = consumedCount;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Factoryorderdetail getFactoryorderdetail() {
        return this.factoryorderdetail;
    }
    
    public void setFactoryorderdetail(Factoryorderdetail factoryorderdetail) {
        this.factoryorderdetail = factoryorderdetail;
    }

    public Orderdetail getOrderdetail() {
        return this.orderdetail;
    }
    
    public void setOrderdetail(Orderdetail orderdetail) {
        this.orderdetail = orderdetail;
    }

    public Integer getConsumedCount() {
        return this.consumedCount;
    }
    
    public void setConsumedCount(Integer consumedCount) {
        this.consumedCount = consumedCount;
    }
   








}