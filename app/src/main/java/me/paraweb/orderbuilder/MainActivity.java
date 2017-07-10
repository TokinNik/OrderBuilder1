package me.paraweb.orderbuilder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import me.paraweb.orderbuilder.Network.Connection;
import me.paraweb.orderbuilder.Orders.Order;

import me.paraweb.orderbuilder.Orders.OrderBuilder;


public class MainActivity extends AppCompatActivity
{
    private Spinner spinnerRate;
    private Spinner spinnerPage;
    public static ArrayList<TableRow> tableRows = new ArrayList<TableRow>();//массивы контейнеров интерфейса
    public static ArrayList<EditText> editNames = new ArrayList<EditText>();
    public static ArrayList<Spinner> editValues = new ArrayList<Spinner>();
    public static ArrayList<Integer> designValues = new ArrayList<Integer>();//масивы часов для разных разделов
    public static ArrayList<Integer> pageProofsValues = new ArrayList<Integer>();
    public static ArrayList<Integer> programmingValues = new ArrayList<Integer>();
    public static String currentPage;//Текущий отображаемый раздел
    final public static String[] itemsTemplate = {"Пустой проект", "Стандартный шаблон"};//Список шаблонов. Надо будет потом сделать их загрузку
    public int localRateDesign;//Т.к. можно локально изменить рейт, здесь хранятся значения для каждого раздела (в виде номена позиции в списке, а не реальное значение)
    public int localRatePageProofs;
    public int localRateProgramming;
    private TableRow.LayoutParams paramsRow = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
    private TableRow.LayoutParams paramsName = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 8);
    private TableRow.LayoutParams paramsValue = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
    private TextView textViewCost;//Поле "Итого"

    private OrderBuilder orderBuilder;
    private Order order = new Order("projectName", "client");


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Не использовать
        //Connection connection = new Connection();

        spinnerPage = (Spinner)findViewById(R.id.spinnerPage);
        spinnerRate = (Spinner)findViewById(R.id.spinnerRate);
        final String[] rates = getResources().getStringArray(R.array.rates);//Ставим стандартные рейты
        spinnerPage.setSelection(0);
        int i = -1;
        for (String x:rates)
        {
            i++;
            if (Integer.parseInt(x) == order.getDesignRate())
                localRateDesign = i;
            if (Integer.parseInt(x) == order.getPageProofsRate())
                localRatePageProofs = i;
            if (Integer.parseInt(x) == order.getProgrammingRate())
                localRateProgramming = i;
        }
        spinnerRate.setSelection(localRateDesign);

        currentPage = "Дизайн";//начальный раздел

        spinnerRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()//При изменении рейта идёт пересчёт итоговой стоимости.
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                textViewCost.setText(String.valueOf(sumHours()*Integer.parseInt(spinnerRate.getSelectedItem().toString())));
                switch (spinnerPage.getSelectedItem().toString())// Новое значение записывается в соответствующую переменную
                {
                    case "Дизайн":
                        localRateDesign = spinnerRate.getSelectedItemPosition();
                        break;
                    case "Вёрстка":
                        localRatePageProofs = spinnerRate.getSelectedItemPosition();
                        break;
                    case "Программирование":
                        localRateProgramming = spinnerRate.getSelectedItemPosition();
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        textViewCost = (TextView)findViewById(R.id.textViewCost);

        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()//При переключении раздела
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch (spinnerPage.getSelectedItem().toString())//Смотрим где сейчас
                {
                    case "Дизайн":
                        int x = 0;
                        switch (currentPage)//Потом где были
                        {
                            case "Вёрстка":
                                pageProofsValues.clear();
                                for (Spinner edit1: editValues)//Сохраняем текущие значения для данного раздела (например здесь для "Вёрстка")
                                    pageProofsValues.add(edit1.getSelectedItemPosition());
                                break;
                            case "Программирование":
                                programmingValues.clear();
                                for (Spinner edit: editValues)
                                    programmingValues.add(edit.getSelectedItemPosition());
                                break;
                            default:
                                break;
                        }
                        for (Spinner ed: editValues)//Загружаем сохранённые ранее данные для раздела, в который переходим
                        {
                            ed.setSelection(designValues.get(x));
                            x++;
                        }
                        currentPage = "Дизайн";//Ставим текущий раздел
                        spinnerRate.setSelection(localRateDesign);//И рейт для него
                        break;//И дальш тоже самоу для других пунктов
                    case "Вёрстка":
                        x = 0;
                        switch (currentPage)
                        {
                            case "Дизайн":
                                designValues.clear();
                                for (Spinner edit1: editValues)
                                    designValues.add(edit1.getSelectedItemPosition());
                                break;
                            case "Программирование":
                                programmingValues.clear();
                                for (Spinner edit: editValues)
                                    programmingValues.add(edit.getSelectedItemPosition());
                                break;
                            default:
                                break;
                        }
                        for (Spinner ed: editValues)
                        {
                            ed.setSelection(pageProofsValues.get(x));
                            x++;
                        }
                        currentPage = "Вёрстка";
                        spinnerRate.setSelection(localRatePageProofs);
                        break;
                    case "Программирование":
                        x = 0;
                        switch (currentPage)
                        {
                            case "Дизайн":
                                designValues.clear();
                                for (Spinner edit1: editValues)
                                    designValues.add(edit1.getSelectedItemPosition());
                                break;
                            case "Вёрстка":
                                pageProofsValues.clear();
                                for (Spinner edit: editValues)
                                    pageProofsValues.add(edit.getSelectedItemPosition());
                                break;
                            default:
                                break;
                        }
                        for (Spinner ed: editValues)
                        {
                            ed.setSelection(programmingValues.get(x));
                            x++;
                        }
                        currentPage = "Программирование";
                        spinnerRate.setSelection(localRateProgramming);
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void onClickAddItem(View view)//Метод добавления новой строки "Название -- Часы" (Срабатывае по кнопке  "+")
    {
        designValues.add(0);//Заносим 0 в массивы (Далее пользовотель уже будет менять значения)
        pageProofsValues.add(0);
        programmingValues.add(0);

        TableRow rowButton = (TableRow)findViewById(R.id.tableRowForButton);//Берём или создаём нужные элементы
        LinearLayout itemTable = (LinearLayout)findViewById(R.id.itemTable);
        TableRow row = new TableRow(this);
        tableRows.add(row);
        TableRow rowB = new TableRow(this);
        EditText editName = new EditText(this);
        Spinner editValue = new Spinner(this);

        itemTable.removeView(rowButton);//Удаляем строку с кнопкой "+"

        row.setLayoutParams(paramsRow);//Задаём параметры разметки и пр. для елементов новой строки
        row.setGravity(Gravity.TOP);
        rowB.setLayoutParams(paramsRow);
        rowB.setGravity(Gravity.TOP);
        itemTable.addView(row);

        editName.setLayoutParams(paramsName);
        editValue.setLayoutParams(paramsValue);

        editName.setHint(R.string.page);
        editName.setInputType(InputType.TYPE_CLASS_TEXT);

        SpinnerAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item ,getResources().getStringArray(R.array.hours));
        editValue.setAdapter(adapter);//Ставим обработчик на спиннер часов, чтобы он пересчитывал значение "Итого"
        editValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                textViewCost.setText(String.valueOf(sumHours()*Integer.parseInt(spinnerRate.getSelectedItem().toString())));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        row.addView(editName);//добавляем новую строку на экран
        row.addView(editValue);

        Button button = new Button(this);//Создаём кнопку "+"
        button.setLayoutParams(paramsValue);
        button.setText(R.string.plus);
        button.setBackground(getResources().getDrawable(R.color.Dark_Blue));
        button.setTextSize(24);
        button.setTextColor(getResources().getColor(R.color.White));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddItem(v);
            }
        });

        rowB.setId(R.id.tableRowForButton);
        itemTable.addView(rowB);//Добавляем строку с кнопкой на экран
        rowB.addView(button);

        editNames.add(editName);//заносим поля в массивы
        editValues.add(editValue);
    }

    public int sumHours ()//метод для подсчёта "Итого"
    {
        int s = 0;
            for (Spinner sp:editValues)
            {
                s += Integer.parseInt(sp.getSelectedItem().toString());
            }
            return s;
    }

    public void onClickNewProject(final MenuItem item)//Метод - обработчик пункта меню "Новый проект"
    {
        boolean canNew = true;
        for (EditText ed : editNames)
            if (!ed.getText().toString().isEmpty())//Проверяем, что у нас нет данных, которые можно потерять
            {
                canNew = false;
                break;
            }
        if (!canNew) //Если есть, то показываем предупреждение
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.createNewProjectQ));
            builder.setMessage(getString(R.string.errorDataLost));
            builder.setCancelable(false);
            builder.setNegativeButton(getString(R.string.next), new DialogInterface.OnClickListener()//Кнопка "Далее"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    changeTempDialog();//Запускаем основное окно (выбор шаблона для нового проекта(при этом все данные пропадут))
                }
            });
            builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener()//Кнопка "Отмена"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setNeutralButton(getString(R.string.saveAndNext), new DialogInterface.OnClickListener()//Кнопка "Сохранить и продолжить"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    changeTempDialog();
                    onClickSave(item);//Сначало окно сохранения, потом окно выбора шаблона
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else//Иначе переходим на окно выбора шаблона
            changeTempDialog();
    }

    public void onClickSave(final MenuItem item)//Метод - обработчик пункта меню "Сохранить проект"
    {
        boolean canSave = false;
        for (EditText ed : editNames)
            if (!ed.getText().toString().isEmpty())//Проверяем, что у нас есть, что сохранять
            {
                canSave = true;
                break;
            }
        if(canSave)//Если есть, то переходим на окно сохранения
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.saveProjectQ));
            builder.setMessage(getString(R.string.enterProjectName));
            builder.setCancelable(false);
            final EditText input = new EditText(this);
            input.setLayoutParams(paramsName);
            input.setGravity(Gravity.LEFT);

            builder.setNegativeButton(getString(R.string.saveOnServer), new DialogInterface.OnClickListener()//Кнопка "Сохранить на сервере"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (input.getText().toString().equals(""))//Проверка на пустое имя
                    {
                        showMessage(getString(R.string.enterProjectName));
                        onClickSave(item);
                    }
                    else
                        dialogInterface.cancel();
                }
            });
            builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener()//Кнопка "Отмена"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setNeutralButton(getString(R.string.saveInExcel), new DialogInterface.OnClickListener()//Кнопка "Сохранить в фотмате Excel"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (input.getText().toString().equals(""))//Проверка на пустое имя
                    {
                        showMessage(getString(R.string.enterProjectName));
                        onClickSave(item);
                    }
                    else
                        dialogInterface.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.setView(input);
            alert.show();
        }
        else//Иначе выводим сообщение
            showMessage("Нет данных для сохранения");
    }

    public void onClickLoad(final MenuItem item)//Метод - обработчик пункта меню "Загрузить проект"
    {
        boolean canLoad = true;
        for (EditText ed : editNames)
            if (!ed.getText().toString().isEmpty())//Проверяем, что у нас нет данных, которые можно потерять
            {
                canLoad = false;
                break;
            }
        if (!canLoad) //Если есть, то показываем предупреждение
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.loadProjectQ));
            builder.setMessage(getString(R.string.errorDataLost));
            builder.setCancelable(false);
            builder.setNegativeButton(getString(R.string.next), new DialogInterface.OnClickListener()//Кнопка "Далее"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener()//Кнопка "Отмена"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setNeutralButton(getString(R.string.saveAndNext), new DialogInterface.OnClickListener()//Кнопка "Сохранить и продолжить"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onClickSave(item);
                    dialogInterface.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else//Иначе
        {
            //Ещё нет
        }
    }

    public void onClickChangeTemp(final MenuItem item)//Метод - обработчик пункта меню "Изменить шаблон"
    {
        boolean canChange = true;
        for (EditText ed : editNames)
            if (!ed.getText().toString().isEmpty())//Проверяем, что у нас нет данных, которые можно потерять
            {
                canChange = false;
                break;
            }
        if (!canChange)//Если есть, то показываем предупреждение
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.loadProjectTemplateQ));
            builder.setMessage(getString(R.string.errorDataLost));
            builder.setCancelable(false);
            builder.setNegativeButton(getString(R.string.next), new DialogInterface.OnClickListener()//Кнопка "Далее"ыыыы
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                    changeTempDialog();
                }
            });
            builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener()//Кнопка "Отмена"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                }
            });
            builder.setNeutralButton(getString(R.string.saveAndNext), new DialogInterface.OnClickListener()//Кнопка "Сохранить и продолжить"
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                    changeTempDialog();
                    onClickSave(item);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else//Иначе переходим на основное окно выбора шаблона
            changeTempDialog();
    }
    private void changeTempDialog ()//Метод - обработчик пункта меню "Изменить шаблон", вызывается в других методах
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.choiseProjectTemplate));
        builder.setItems(itemsTemplate, new DialogInterface.OnClickListener()//Список шаблонов
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                deleteAllItems();//При выборе шаблона все данные стираются
                dialogInterface.cancel();//Далее должен идти метод установки шаблона (его ещё нет)
            }
        });
        builder.setCancelable(false);
        builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener()//Кнопка "Отмена"
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAllItems()//Метод для удаления всех строк с экрана(очистка данных). Приводит к шаблону "Пустой проект"
    {
        LinearLayout itemTable = (LinearLayout)findViewById(R.id.itemTable);
        itemTable.removeViews(0, itemTable.getChildCount()-1);//Удаляем все, кроме строки с кнопкой "+"
        editValues.clear();
        editNames.clear();
        designValues.clear();
        pageProofsValues.clear();
        programmingValues.clear();
    }
    private void showMessage(String textInMessage)//Вспомогательный метод для вывода тостов
    {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
    }
}