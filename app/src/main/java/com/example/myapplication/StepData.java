package com.example.myapplication;

public class StepData {
    private String step;
    private String text;

    public StepData(String step, String text) {
        this.step = step;
        this.text = text;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
