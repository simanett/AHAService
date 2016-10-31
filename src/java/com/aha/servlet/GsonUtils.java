/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aha.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
    
    public static Gson getGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
                .create();
    }
}
