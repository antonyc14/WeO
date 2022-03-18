package com.example.ecommerce.Users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ecommerce.Model.ToDo;
import com.example.ecommerce.Model.ToDoAdapter;
import com.example.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

public class WeddingToDoActivity extends AppCompatActivity {

    private RecyclerView taskRecyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDo> toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_to_do);

        toDoList = new ArrayList<>();

        taskRecyclerView = findViewById(R.id.tasks_RecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toDoAdapter = new ToDoAdapter(this);
        taskRecyclerView.setAdapter(toDoAdapter);

        ToDo task1 = new ToDo();
        task1.setTask("Baju Pernikahan");
        task1.setStatus(0);
        task1.setId(1);
        toDoList.add(task1);

        ToDo task2 = new ToDo();
        task2.setTask("Flower Bouquets");
        task2.setStatus(0);
        task2.setId(2);
        toDoList.add(task2);

        ToDo task3 = new ToDo();
        task3.setTask("Undangan");
        task3.setStatus(0);
        task3.setId(3);
        toDoList.add(task3);

        ToDo task4 = new ToDo();
        task4.setTask("Venue");
        task4.setStatus(0);
        task4.setId(4);
        toDoList.add(task4);

        ToDo task5 = new ToDo();
        task5.setTask("Catering");
        task5.setStatus(0);
        task5.setId(5);
        toDoList.add(task5);

        ToDo task6 = new ToDo();
        task6.setTask("Souvenir");
        task6.setStatus(0);
        task6.setId(6);
        toDoList.add(task6);

        ToDo task7 = new ToDo();
        task7.setTask("Penata Rias");
        task7.setStatus(0);
        task7.setId(7);
        toDoList.add(task7);

        ToDo task8 = new ToDo();
        task8.setTask("Dekorasi");
        task8.setStatus(0);
        task8.setId(8);
        toDoList.add(task8);

        ToDo task9 = new ToDo();
        task9.setTask("Photographer");
        task9.setStatus(0);
        task9.setId(9);
        toDoList.add(task9);

        ToDo task10 = new ToDo();
        task10.setTask("Cincin Pernikahan");
        task10.setStatus(0);
        task10.setId(10);
        toDoList.add(task10);

        toDoAdapter.setTasks(toDoList);
    }
}