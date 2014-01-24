package com.pannous.netbase.blueprints;

import com.sun.jna.Union;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 23/01/14
 * Time: 11:23
 */
public class Value extends Union {

    public Value(){
//        setType(Double.class);
        setType(double.class);
    }

    public String text; // wiki abstracts etc
    //    char* name;
    //    int integer;
    //    float floatValue;
    public long datetime; // milliseconds? Date*? HOW really?
    //    long longValue;// 8 bytes

    public double number; // 8 bytes OK
    // why not just save ints as string?
    // 1) faster comparison   bottleneck factor n==nr digits or is atoi part of cpu?
    // 2) memory. really?? only if value field is used >80% of the time!

//    public Node node; // THE ONE in abstracts type --- cycle !---
//    public Statement statement;
}
