package com.home.glx.uwallet.datas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChoiceDatas {

    private List<ChoiceDatas> sex;
    private List<ChoiceDatas> medicalCardType;
    private List<ChoiceDatas> county;
    private List<ChoiceDatas> merchantState;
    private List<ChoiceDatas> cardType;
    private List<ChoiceDatas> mainBusiness;
    private List<ChoiceDatas> phoneCode;
    private List<ChoiceDatas> paymentItems;
    private List<ChoiceDatas> directDebitTerms;
    private List<ChoiceDatas> privacyAgreement;
    private List<ChoiceDatas> termAndConditions;
    private List<ChoiceDatas> customerApplicationFormULaypay;
    private List<ChoiceDatas> merchantCategories;

    private List<ChoiceDatas> privacyAgreementNew;
    private List<ChoiceDatas> payLaterApplicationForm;
    private List<ChoiceDatas> payLaterTermsConditions;
    private List<ChoiceDatas> payNowApplication;
    private List<ChoiceDatas> payNowTermsConditions;
    @SerializedName("bnpl-tac")
    private List<ChoiceDatas> bnplTac;

    public List<ChoiceDatas> getBnplTac() {
        return bnplTac;
    }

    public void setBnplTac(List<ChoiceDatas> bnplTac) {
        this.bnplTac = bnplTac;
    }

    public List<ChoiceDatas> getMerchantCategories() {
        return merchantCategories;
    }

    public void setMerchantCategories(List<ChoiceDatas> merchantCategories) {
        this.merchantCategories = merchantCategories;
    }

    public List<ChoiceDatas> getCustomerApplicationFormULaypay() {
        return customerApplicationFormULaypay;
    }

    public void setCustomerApplicationFormULaypay(List<ChoiceDatas> customerApplicationFormULaypay) {
        this.customerApplicationFormULaypay = customerApplicationFormULaypay;
    }

    public List<ChoiceDatas> getPrivacyAgreement() {
        return privacyAgreement;
    }

    public void setPrivacyAgreement(List<ChoiceDatas> privacyAgreement) {
        this.privacyAgreement = privacyAgreement;
    }

    public List<ChoiceDatas> getTermAndConditions() {
        return termAndConditions;
    }

    public void setTermAndConditions(List<ChoiceDatas> termAndConditions) {
        this.termAndConditions = termAndConditions;
    }

    public List<ChoiceDatas> getDirectDebitTerms() {
        return directDebitTerms;
    }

    public void setDirectDebitTerms(List<ChoiceDatas> directDebitTerms) {
        this.directDebitTerms = directDebitTerms;
    }

    public List<ChoiceDatas> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<ChoiceDatas> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public List<ChoiceDatas> getSex() {
        return sex;
    }

    public void setSex(List<ChoiceDatas> sex) {
        this.sex = sex;
    }

    public List<ChoiceDatas> getMedicalCardType() {
        return medicalCardType;
    }

    public void setMedicalCardType(List<ChoiceDatas> medicalCardType) {
        this.medicalCardType = medicalCardType;
    }

    public List<ChoiceDatas> getCounty() {
        return county;
    }

    public void setCounty(List<ChoiceDatas> county) {
        this.county = county;
    }

    public List<ChoiceDatas> getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(List<ChoiceDatas> merchantState) {
        this.merchantState = merchantState;
    }

    public List<ChoiceDatas> getCardType() {
        return cardType;
    }

    public void setCardType(List<ChoiceDatas> cardType) {
        this.cardType = cardType;
    }

    public List<ChoiceDatas> getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(List<ChoiceDatas> mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public List<ChoiceDatas> getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(List<ChoiceDatas> phoneCode) {
        this.phoneCode = phoneCode;
    }

    public List<ChoiceDatas> getPrivacyAgreementNew() {
        return privacyAgreementNew;
    }

    public void setPrivacyAgreementNew(List<ChoiceDatas> privacyAgreementNew) {
        this.privacyAgreementNew = privacyAgreementNew;
    }

    public List<ChoiceDatas> getPayLaterApplicationForm() {
        return payLaterApplicationForm;
    }

    public void setPayLaterApplicationForm(List<ChoiceDatas> payLaterApplicationForm) {
        this.payLaterApplicationForm = payLaterApplicationForm;
    }

    public List<ChoiceDatas> getPayLaterTermsConditions() {
        return payLaterTermsConditions;
    }

    public void setPayLaterTermsConditions(List<ChoiceDatas> payLaterTermsConditions) {
        this.payLaterTermsConditions = payLaterTermsConditions;
    }

    public List<ChoiceDatas> getPayNowApplication() {
        return payNowApplication;
    }

    public void setPayNowApplication(List<ChoiceDatas> payNowApplication) {
        this.payNowApplication = payNowApplication;
    }

    public List<ChoiceDatas> getPayNowTermsConditions() {
        return payNowTermsConditions;
    }

    public void setPayNowTermsConditions(List<ChoiceDatas> payNowTermsConditions) {
        this.payNowTermsConditions = payNowTermsConditions;
    }
}
