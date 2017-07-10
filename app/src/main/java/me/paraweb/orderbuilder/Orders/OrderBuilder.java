package me.paraweb.orderbuilder.Orders;

import android.widget.EditText;
import android.widget.Spinner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import me.paraweb.orderbuilder.MainActivity;

/**
 * Created by vault13 on 02.07.17.
 * В этом классе напишешь плез методы, которые будут читать данные из интерфейса и вызывать
 * добавляшки из Order, типа Поля_с_именем_заказчика_и_проекта.getData()
 *                           Order order = new Order(твои данные)
 */

public class OrderBuilder
{
    private Order order;

    public OrderBuilder (){
        this.order = new Order("ProjectName", "client");
    }
    public void getData (String storage) //Возможно требуется переделать (ещё не занимался этим)
    {
        int i = 0;
        for (EditText editNames: MainActivity.editNames)
        {
            if (!editNames.getText().toString().isEmpty() && !MainActivity.editValues.get(i).getSelectedItem().toString().isEmpty())
            {
                OrderComponents orderComponents = new OrderComponents(editNames.getText().toString(),
                        Integer.parseInt(MainActivity.editValues.get(i).getSelectedItem().toString()));
                order.AddComponent(storage, orderComponents);
            }
            i++;
        }
    }
    public void setData (String storage)//Тоже самое, требуется переделать
{
        int i = 0;
        switch (storage)
        {
            case "Дизайн":
                if (!order.design.isEmpty())
                for (EditText editNames: MainActivity.editNames)
                {
                    editNames.setText(order.design.get(i).getComponentName());
                    MainActivity.editValues.get(i).setSelection(order.design.get(i).getHourse());
                    i++;
                }
                else
                    for (Spinner sp: MainActivity.editValues)
                        sp.setSelection(1);

                break;
            case "Вёрстка":
                if (!order.pageProofs.isEmpty())
                for (EditText editNames: MainActivity.editNames)
                {
                    editNames.setText(order.pageProofs.get(i).getComponentName());
                    MainActivity.editValues.get(i).setSelection(order.pageProofs.get(i).getHourse());
                    i++;
                }
                else
                    for (Spinner sp: MainActivity.editValues)
                        sp.setSelection(1);
                break;
            case "Программирование":
                if (!order.programming.isEmpty())
                for (EditText editNames: MainActivity.editNames)
                {
                    editNames.setText(order.programming.get(i).getComponentName());
                    MainActivity.editValues.get(i).setSelection(order.programming.get(i).getHourse());
                    i++;
                }
                else
                    for (Spinner sp: MainActivity.editValues)
                        sp.setSelection(1);
                break;
            default:
                break;
        }

    }
    public void GenXLSXFile(){

        Workbook orderXLSX = new HSSFWorkbook();
        Sheet sheet = orderXLSX.createSheet("Структура заказа");

        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Заказчик:");
        cell = row.createCell(1);
        cell.setCellValue(this.order.client);

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Заказ:");
        cell.setCellValue(this.order.projectName);

        for(int i = 0; i < this.order.design.size(); i++) {
            row = sheet.createRow(i+3);
            cell = row.createCell(0);
            cell.setCellValue(this.order.design.get(i).getComponentName());
            cell = row.createCell(1);
            cell.setCellValue(this.order.design.get(i).getHourse());
            cell = row.createCell(2);
            cell.setCellValue(this.order.getDesignRate());
            cell = row.createCell(3);
            cell.setCellValue(this.order.design.get(i).getHourse() * this.order.getDesignRate());
        }
        int index = sheet.getPhysicalNumberOfRows();
        for(int i = 0; i < this.order.programming.size(); i++) {
            row = sheet.createRow(index);
            cell = row.createCell(0);
            cell.setCellValue(this.order.programming.get(i).getComponentName());
            cell = row.createCell(1);
            cell.setCellValue(this.order.programming.get(i).getHourse());
            cell = row.createCell(2);
            cell.setCellValue(this.order.getProgrammingRate());
            cell = row.createCell(3);
            cell.setCellValue(this.order.programming.get(i).getHourse() * this.order.getProgrammingRate());

        }
        index = sheet.getPhysicalNumberOfRows();
        for(int i = 0; i < this.order.design.size(); i++) {
            row = sheet.createRow(index);
            cell = row.createCell(0);
            cell.setCellValue(this.order.design.get(i).getComponentName());
            cell = row.createCell(1);
            cell.setCellValue(this.order.design.get(i).getHourse());
            cell = row.createCell(2);
            cell.setCellValue(this.order.getDesignRate());
            cell = row.createCell(3);
            cell.setCellValue(this.order.design.get(i).getHourse() * this.order.getDesignRate());

        }


    }
    public void LoadXLSXFile(){



    }
}
