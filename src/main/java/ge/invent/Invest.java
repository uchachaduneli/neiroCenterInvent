package ge.invent;

import ge.invent.utils.Messages;
import ge.invent.utils.Util;
import ge.invest.entities.Inventar;
import ge.invest.processing.DbProcessing;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@ViewScoped
@ManagedBean(name = "investBean")
public final class Invest implements Serializable {

    private Inventar details = new Inventar();
    private List<Inventar> investDetails = new ArrayList<Inventar>();
    private List<Inventar> changedPersons = new ArrayList<Inventar>();
    private List<Inventar> filtered;
    private Inventar selectedInvent = new Inventar();
    private String searchKey;
    private Double totalCost;

    public Invest() {
        getAllInvestDetails();
    }

    //select metods
    public void getAllInvestDetails() {
        DbProcessing.getInnvestDetails(investDetails);
        filtered = investDetails;
        filterListener();
    }

    public void getChangedPersonsList() {
        changedPersons = DbProcessing.getPersonChanges(selectedInvent.getId());
    }

    public void startNewSeazon() {
        if (DbProcessing.startNewSeason() > 0) {
            Util.executeScript("location.reload();");
        } else {
            Messages.warn(Util.ka("operacia ar sruldeba daukavSirdiT administrators"));
        }
    }

    public void filterListener() {
        totalCost = 0.0;
        for (Inventar inventar : filtered) {
            totalCost += inventar.getUnitPrice();
        }
        RequestContext.getCurrentInstance().update("sumCostOutputId");
    }

    public void saveAdnUpdateElement() {
        try {
            RequestContext req = RequestContext.getCurrentInstance();

            if (details.getId() == 0) {
                if (!(details.getResponsiblePerson().equals("")) && details.getResponsiblePerson() != null) {
                    boolean result = DbProcessing.insertInvetDetailes(details);
                    if (result) {
                        investDetails = new ArrayList<>();
                        getAllInvestDetails();
                        req.update("invenDataTableId");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Lang.ka("operacia dasrulda warmatebiT"), ""));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Lang.ka("operacia ar sruldeba"), ""));
                    }
                    RequestContext.getCurrentInstance().execute("insertDLGwidg.hide()");
                }
            } else {
                boolean result = DbProcessing.updateInvetDetailes(selectedInvent);
                if (result) {
                    investDetails = new ArrayList<Inventar>();
                    getAllInvestDetails();
                    req.update("invenDataTableId");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Lang.ka("operacia dasrulda warmatebiT"), ""));

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Lang.ka("operacia ar sruldeba"), ""));
                }
                RequestContext.getCurrentInstance().execute("insertDLGwidg.hide()");
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Lang.ka("operacia ar sruldeba"), ""));

        }
    }

    //redaqtireba
    public void removeInvestElement() {
        try {
            RequestContext req = RequestContext.getCurrentInstance();
            boolean result = DbProcessing.removeIvent(selectedInvent);
            if (result) {
                investDetails = new ArrayList<>();
                getAllInvestDetails();
                req.execute("_tblList.filter()");
                req.update("invenDataTableId");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, Lang.ka("monacemebis waSla dasrulda warmatebiT"), ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Lang.ka("monacemebis WaSla  ar sruldeba"), ""));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, Lang.ka("monacemebis WaSla  ar sruldeba"), ""));
        }
    }

    //serchmetods
    public void searchForBarcode() {
        RequestContext req = RequestContext.getCurrentInstance();
        investDetails.clear();
        boolean isData = false;
        getAllInvestDetails();
        for (Inventar details : investDetails) {
            if (searchKey.equals(details.getBarcode())) {
                isData = true;
                investDetails.clear();
                details.setStatus_new("+");
                DbProcessing.updateInvetDetailes(details);
                investDetails.add(details);
            } else if (searchKey == null || searchKey.equals("")) {
                isData = true;
                break;
            }
        }
        if (!isData) {
            investDetails.clear();
            if (investDetails.isEmpty()) {
                details = new Inventar();
                details.setBarcode(searchKey);
                details.setCount(1);
                details.setStatus_new("+");
                RequestContext.getCurrentInstance().execute("insertDLGwidg.show()");
                req.update("insertDlgFormId");
            }
        }
        req.update("invenDataTableId");
    }

    public void selectIvent() {
        RequestContext req = RequestContext.getCurrentInstance();
        details = selectedInvent;
        req.update("insertDlgFormId");
    }

    public void investDetails() {
        RequestContext req = RequestContext.getCurrentInstance();
        details = new Inventar();
        if (investDetails.isEmpty()) {
            if (getSearchKey() != null) {
                details.setBarcode(getSearchKey());
            }
        }
        if (selectedInvent != null) {
            details.setResponsiblePerson(selectedInvent.getResponsiblePerson());
        }
        selectedInvent = new Inventar();
        req.update("insertDlgFormId");
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
        }
    }

    public List<Inventar> getChangedPersons() {
        return changedPersons;
    }

    public void setChangedPersons(List<Inventar> changedPersons) {
        this.changedPersons = changedPersons;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Inventar> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Inventar> filtered) {
        this.filtered = filtered;
    }

    public Inventar getDetails() {
        return details;
    }

    public void setDetails(Inventar details) {
        this.details = details;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Inventar getSelectedInvent() {
        return selectedInvent;
    }

    public void setSelectedInvent(Inventar selectedInvent) {
        this.selectedInvent = selectedInvent;
    }

    public List<Inventar> getInvestDetails() {
        return investDetails;
    }

    public void setInvestDetails(List<Inventar> investDetails) {
        this.investDetails = investDetails;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
