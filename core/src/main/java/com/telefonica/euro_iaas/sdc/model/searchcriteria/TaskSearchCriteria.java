package com.telefonica.euro_iaas.sdc.model.searchcriteria;

import java.util.Date;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.Task.TaskStates;

public class TaskSearchCriteria extends AbstractSearchCriteria {

    private String resource;
    private List<TaskStates> states;
    private String owner;
    private Date fromDate;
    private Date toDate;
    private String vdc;

    /**
     *
     */
    public TaskSearchCriteria() {
    }

    /**
     * @param page
     * @param pageSize
     * @param orderBy
     * @param orderType
     */
    public TaskSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType) {
        super(page, pageSize, orderBy, orderType);
    }

    /**
     * @param page
     * @param pageSize
     */
    public TaskSearchCriteria(Integer page, Integer pageSize) {
        super(page, pageSize);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param orderBy
     * @param orderType
     */
    public TaskSearchCriteria(String orderBy, String orderType) {
        super(orderBy, orderType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the product
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource
     * @param states
     * @param owner
     * @param fromDate
     * @param toDate
     */
    public TaskSearchCriteria(String resource, List<TaskStates> states, String owner, Date fromDate, Date toDate,
            String vdc) {
        this.resource = resource;
        this.states = states;
        this.owner = owner;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.vdc = vdc;
    }

    /**
     * @param resource
     *            the product to set
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * @return the states
     */
    public List<TaskStates> getStates() {
        return states;
    }

    /**
     * @param states
     *            the states to set
     */
    public void setStates(List<TaskStates> states) {
        this.states = states;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner
     *            the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate
     *            the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

}
