package com.example.threadingexample.retrofit_example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultObject {
    boolean success;
    String message;
    List<Markets> result;

}
