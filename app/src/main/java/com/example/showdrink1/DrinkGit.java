package com.example.showdrink1;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//DESATIVADO
public class DrinkGit {
    @SerializedName("idDrink")
    @Expose
    private Integer idDrink;

    @SerializedName("strDrink")
    @Expose
    private String strDrink;

    @SerializedName("strCategory")
    @Expose
    private String strCategory;

    @SerializedName("strInstruction")
    @Expose
    private String strInstruction;

    @SerializedName("strDrinkThumb")
    @Expose
    private String strDrinkThumb;

    public DrinkGit(Integer vidDrink, String vstrDrink, String vstrCategory, String vstrInstruction, String vstrDrinkThumb) {
        this.idDrink = vidDrink;
        this.strDrink = vstrDrink;
        this.strCategory = vstrCategory;
        this.strInstruction = vstrInstruction;
        this.strDrinkThumb = vstrDrinkThumb;

    }

    public DrinkGit() {

    }

    public Integer getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(Integer idDrink) {
        this.idDrink = idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrInstruction() {
        return strInstruction;
    }

    public void setStrInstruction(String strInstruction) {
        this.strInstruction = strInstruction;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    @Override
    public String toString(){
    return "idDrink:" + getIdDrink() +
                "\n strDrink" + getStrDrink() +
            "\n strCategory" + getStrCategory() +
            "\n strInstruction" + getStrInstruction()+
            "\n strDrinkThumb" + getStrDrinkThumb();

    }

}
