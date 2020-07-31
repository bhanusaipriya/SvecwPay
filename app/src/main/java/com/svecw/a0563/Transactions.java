package com.svecw.a0563;

import android.view.SurfaceControl;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.svecw.a0563.Inner;

public class Transactions {
    Outer outer = new Outer();
    public Map<String,Outer> mapList = new HashMap<>();
    public Transactions() {
    }

    public Transactions(Outer outer, Map<String, Outer> mapList) {
        this.outer = outer;
        this.mapList = mapList;
    }

    public Outer getOuter() {
        return outer;
    }

    public void setOuter(Outer outer) {
        this.outer = outer;
    }

    public Map<String, Outer> getMapList() {
        return mapList;
    }

    public void setMapList(Map<String, Outer> mapList) {
        this.mapList = mapList;
    }
}


