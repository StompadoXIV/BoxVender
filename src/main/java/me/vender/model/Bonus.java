package me.vender.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Bonus {

    private String permission, group;
    private double percentage;

}