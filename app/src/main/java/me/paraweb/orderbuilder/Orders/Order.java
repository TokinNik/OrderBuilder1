package me.paraweb.orderbuilder.Orders;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vault13 on 02.07.17.
 *
 *  Сюда будем собирать заказы. Нужно будет распарсить данные из интерфейса и
 *  сохранить их в соответствующие поля. Отсюда я буду парсить данные на запрос и так далее
 *
 */

public class Order implements Serializable {

    public String projectName; //Что делаем
    public String client; //Кому делаем

    //Списки того, что надо(та таблица короче говоря)
    public static ArrayList<OrderComponents> design;
    public static ArrayList<OrderComponents> programming;
    public static ArrayList<OrderComponents> pageProofs;

    //Параметры заказа
    private int designRate;
    private int programmingRate;
    private int pageProofsRate;

    private int totalPrice;
    private int designPrice;
    private int programmingPrice;
    private int pageProofsPrice;

    private int executionTime;
    private int designTime;
    private int programmingTime;
    private int pageProofsTime;

    private double discount;

     public Order(String projectName, String client) {
         this.projectName = projectName;
         this.client = client;

         this.designRate = 1800;
         this.programmingRate = 1200;
         this.pageProofsRate = 900;

         this.totalPrice = 0;
         this.designPrice = 0;
         this.programmingPrice = 0;
         this.pageProofsPrice = 0;
         this.executionTime = 0;
         this.programmingTime = 0;
         this.designTime = 0;
         this.pageProofsTime = 0;
         this.discount = 0;
         this.design = new ArrayList<OrderComponents>();
         this.pageProofs = new ArrayList<OrderComponents>();
         this.programming = new ArrayList<OrderComponents>();
    }

    //region Get

    public int getDesignRate() {
        return designRate;
    }

    public int getProgrammingRate() {
        return programmingRate;
    }

    public int getPageProofsRate() {
        return pageProofsRate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getProgrammingPrice() {
        return programmingPrice;
    }

    public int getDesignPrice() {
        return designPrice;
    }

    public int getPageProofsPrice() {
        return pageProofsPrice;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public double getDiscount() {
        return discount;
    }

    //endregion

    //region Set

    public void setDesignRate(int designRate) {
        this.designRate = designRate;
    }

    public void setProgrammingRate(int programmingRate) {
        this.programmingRate = programmingRate;
    }

    public void setPageProofsRate(int pageProofsRate) {
        this.pageProofsRate = pageProofsRate;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    //endregion

    public void AddComponent (String storage, OrderComponents componet) {

        switch (storage) {
            case "Дизайн":
                this.design.add(componet);
                designCalcInc(componet.getHourse());
                break;
            case "Программирование":
                this.programming.add(componet);
                programmingCalcInc(componet.getHourse());
                break;
            case "Вёрстка":
                this.pageProofs.add(componet);
                pageProofsCalcInc(componet.getHourse());
                break;
        }
        TotalCalc();
    }

    public void RemoveComponent(String storage, String value){
        switch (storage) {
            case "Дизайн":
                for (int i = 0; i < this.design.size(); i++){
                    if (this.design.get(i).getComponentName().equals(value)) {
                        designCalcDec(this.design.get(i).getHourse());
                        this.design.remove(i);
                    }
                }
                break;
            case "Программирование":
                for (int i = 0; i < this.programming.size(); i++){
                    if (this.programming.get(i).getComponentName().equals(value)) {
                        programmingCalcDec(this.programming.get(i).getHourse());
                        this.programming.remove(i);
                    }
                }
                break;
            case "Вёрстка":
                for (int i = 0; i < this.pageProofs.size(); i++){
                    if (this.pageProofs.get(i).getComponentName().equals(value)) {
                        pageProofsCalcDec(this.pageProofs.get(i).getHourse());
                        this.pageProofs.remove(i);
                    }
                }
                break;
        }
        TotalCalc();
    }

    //region Calculate

    public void designCalcInc(int value){
        this.designTime += value;
        this.designPrice += value * this.designRate;

    }
    public void programmingCalcInc(int value){
        this.programmingTime += value;
        this.programmingPrice += value * this.programmingRate;
    }
    public void pageProofsCalcInc(int value){
        this.pageProofsTime += value;
        this.pageProofsPrice += value * this.pageProofsRate;
    }

    public void designCalcDec(int value){
        this.designTime -= value;
        this.designPrice -= value * this.designRate;
    }
    public void programmingCalcDec(int value){
        this.programmingTime -= value;
        this.programmingPrice -= value * this.programmingRate;
    }
    public void pageProofsCalcDec(int value){
        this.pageProofsTime -= value;
        this.pageProofsPrice -= value * this.pageProofsRate;
    }


    private void TotalCalc() {
        this.executionTime = this.designTime + this.programmingTime + this.pageProofsTime;
        this.totalPrice = this.programmingPrice + this.designPrice + this.pageProofsPrice;
    }

    //endregion


}
