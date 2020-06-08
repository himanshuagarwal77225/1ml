/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.medical.product.Ui;

import com.medical.product.models.Sedulas;

import java.util.ArrayList;

// Reminder class
public class Reminder {
   String id,medican_name,food;
   boolean Activie;
   ArrayList<Sedulas> sedulas;

    public Reminder(String id, String medican_name, String food, boolean activie, ArrayList<Sedulas> sedulas) {
        this.id = id;
        this.medican_name = medican_name;
        this.food = food;
        Activie = activie;
        this.sedulas = sedulas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMedican_name() {
        return medican_name;
    }

    public void setMedican_name(String medican_name) {
        this.medican_name = medican_name;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public boolean isActivie() {
        return Activie;
    }

    public void setActivie(boolean activie) {
        Activie = activie;
    }

    public ArrayList<Sedulas> getSedulas() {
        return sedulas;
    }

    public void setSedulas(ArrayList<Sedulas> sedulas) {
        this.sedulas = sedulas;
    }
}
