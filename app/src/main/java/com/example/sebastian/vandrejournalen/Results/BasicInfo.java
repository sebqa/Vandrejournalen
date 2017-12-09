package com.example.sebastian.vandrejournalen.Results;

/**
 * Created by Sebastian on 09-12-2017.
 */

public class BasicInfo {
    String mensDag;
    String cyklus;
    boolean bSikker;
    String grav;
    String hojde;
    String BMI;
    boolean hep;
    boolean blodTaget;
    boolean rhesus;
    boolean irreg;
    boolean barnRhes;
    boolean antiStof;
    boolean antiD;
    String antiDDate;
    String antiDIni;

    public String getAntiDDate() {
        return antiDDate;
    }

    public void setAntiDDate(String antiDDate) {
        this.antiDDate = antiDDate;
    }

    public boolean isUrin() {
        return urin;
    }

    public void setUrin(boolean urin) {
        this.urin = urin;
    }

    public String getUrinDate() {
        return urinDate;
    }

    public void setUrinDate(String urinDate) {
        this.urinDate = urinDate;
    }

    boolean urin;
    String urinDate;
    String urinIni;

    public String getMensDag() {
        return mensDag;
    }

    public void setMensDag(String mensDag) {
        this.mensDag = mensDag;
    }

    public String getCyklus() {
        return cyklus;
    }

    public void setCyklus(String cyklus) {
        this.cyklus = cyklus;
    }

    public boolean isbSikker() {
        return bSikker;
    }

    public void setbSikker(boolean bSikker) {
        this.bSikker = bSikker;
    }

    public String getGrav() {
        return grav;
    }

    public void setGrav(String grav) {
        this.grav = grav;
    }

    public String getHojde() {
        return hojde;
    }

    public void setHojde(String hojde) {
        this.hojde = hojde;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public boolean isHep() {
        return hep;
    }

    public void setHep(boolean hep) {
        this.hep = hep;
    }

    public boolean isBlodTaget() {
        return blodTaget;
    }

    public void setBlodTaget(boolean blodTaget) {
        this.blodTaget = blodTaget;
    }

    public boolean isRhesus() {
        return rhesus;
    }

    public void setRhesus(boolean rhesus) {
        this.rhesus = rhesus;
    }

    public boolean isIrreg() {
        return irreg;
    }

    public void setIrreg(boolean irreg) {
        this.irreg = irreg;
    }

    public boolean isBarnRhes() {
        return barnRhes;
    }

    public void setBarnRhes(boolean barnRhes) {
        this.barnRhes = barnRhes;
    }

    public boolean isAntiStof() {
        return antiStof;
    }

    public void setAntiStof(boolean antiStof) {
        this.antiStof = antiStof;
    }

    public boolean isAntiD() {
        return antiD;
    }

    public void setAntiD(boolean antiD) {
        this.antiD = antiD;
    }

    public String getAntiDIni() {
        return antiDIni;
    }

    public void setAntiDIni(String antiDIni) {
        this.antiDIni = antiDIni;
    }

    public String getUrinIni() {
        return urinIni;
    }

    public void setUrinIni(String urinIni) {
        this.urinIni = urinIni;
    }

    public BasicInfo() {
    }


}
