package ge.invest.entities;

import java.io.Serializable;

/**
 */
public class Inventar implements Serializable {

    private int id;
    private String barcode;
    private String responsiblePerson;
    private String item;
    private String description;
    private Double unitPrice;
    private Integer count;
    private Double totalCost;
    private String status_old;
    private String status_new;
    private String department;
    private String city;
    private String stelaji;
    private String note;
    private String insertDate;
    private String oldPerson;

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getOldPerson() {
        return oldPerson;
    }

    public void setOldPerson(String oldPerson) {
        this.oldPerson = oldPerson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus_old() {
        return status_old;
    }

    public void setStatus_old(String status_old) {
        this.status_old = status_old;
    }

    public String getStatus_new() {
        return status_new;
    }

    public void setStatus_new(String status_new) {
        this.status_new = status_new;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStelaji() {
        return stelaji;
    }

    public void setStelaji(String stelaji) {
        this.stelaji = stelaji;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
