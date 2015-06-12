/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.model.dto;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;

/**
 * DTO to receive the complete information when a product release is going to be
 * installed.
 * 
 * @author Sergio Arroyo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductInstanceDto extends InstallableInstance {

    // private ProductReleaseDto product;
    private ReleaseDto product;
    //private VM vm;
    private List<Attribute> attributes;
    private String vdc;
    //private String name;
   
    /**
     */
    public ProductInstanceDto() {
    }

    /**
     * @param product
     * @param vm
     */
    public ProductInstanceDto(ReleaseDto product, VM vm) {
        this.product = product;
        setVm(vm);
    }

    /**
     * @return the product
     */
    public ReleaseDto getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(ReleaseDto product) {
        this.product = product;
    }

    /**
     * @return the vm
     */
    /*public VM getVm() {
        return vm;
    }*/

    /**
     * @param vm
     *            the vm to set
     */
    /*public void setVm(VM vm) {
        this.vm = vm;
    }*/

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }
    
    /**
     * @return the name
     */
   /* public String getName() {
        return name;
    }*/
    
    /**
     * @param name
     *            the name to set
     */
    /*public void setName(String name) {
        this.name = name;
    }*/

    /**
     * Constructs a <code>String</code> with all attributes in name = value
     * format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    
  
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ProductInstanceDto]");
        sb.append("[name = ").append(getName()).append("]");
        sb.append("[status = ").append(getStatus()).append("]");
        sb.append("[product = ").append(this.product).append("]");
        sb.append("[vm = ").append(getVm()).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("[vdc = ").append(this.vdc).append("]");
        sb.append("]");
        return sb.toString();
    }

}
