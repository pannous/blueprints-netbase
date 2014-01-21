package com.tinkerpop.blueprints.impls.netbase;

import com.tinkerpop.blueprints.Edge;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 21/01/14
 * Time: 17:24
 */
public class Relation extends Node{
//    public static OnceVector<Relation> all = new OnceVector<Relation>();
    public static String instance="Instanz";
    public static String member="Hat";
    public static final int ANY = 666;

    public Relation(Edge edge) {
        super(edge);
    }


    public Relation(int id, String name) {
        super(name);
        this.id = id;
        this.name = name;
//        Lexicon.nodes.put(id, this);
//        Context.abstracts.put(name.toLowerCase(), this);
//        all.add(this);
//        kind = netbase.model.Type.Relation;
    }

    public static final Relation Unknown = new Relation(0,"see");
    public static final Relation Antonym = new Relation(1, "antonym");
    public static final Relation SuperClass = new Relation(10, "superclass");//Parent ,"Hypernym"
    public static final Relation SubClass = new Relation(11, "subclass");// hyponym
    public static final Relation Owner = new Relation(12, "of");
    public static final Relation Member = new Relation(13, "has");
    public static final Relation Synonym = new Relation(21, "synonym");
    public static final Relation Weight = new Relation(31, "weight");
    // 0 - 31 matching Wordnet!

    //    public static final Relation Synonym = new Relation(32, "Synonym");// See see | tag
    public static final Relation Type = new Relation(33, "kind");
    public static final Relation Instance = new Relation(34, "instance");
    public static final Relation Active = new Relation(35, "active");
    public static final Relation Passive = new Relation(36, "passive");
    public static final Relation Tag = new Relation(37, "tag");// different to 'unknown' !!
    public static final Relation Label = new Relation(38, "label");
    public static final Relation BackLabel = new Relation(39, "BackLabel");
    //    public static final Relation Labels = new Relation(40, "Labels");//??
    public static final Relation LabeledNode = new Relation(41, "LabeledNode");// ugly!!
    public static final Relation Attribute = new Relation(42, "attribute");// tag
    public static final Relation Category = new Relation(43, "category");// tag
    public static final Relation SubContext = new Relation(44, "subcontext");// tag
    public static final Relation SuperContext = new Relation(45, "supercontext");//

//
}
