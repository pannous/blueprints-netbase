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
    public static int number=123;
    public static int array=121;
    public static int date=127;

    public static int singletonId = 99;
    public static int abstractId = 102;



    public static int wordnet = 0;// 303; //context==Class??   TODO!
// DANGER!!
    public static int _internal = 100;
    public static int _node = 101;// dont change
    public static int _abstract = 102;
//int Class=103;
    public static int _klass = 103;
    public static int _clazz = 103;
    public static int _concept = 103;
    public static int _object = 104;
//int Object=104;
    public static int _relation = 105;
    public static int _person = 106;
    public static int _reification=107;//== _statement ??
    public static int _pattern = 108;
    public static int _statement = 109;

// wn: done via type(33) statements

    public static int attribute = 42;
    public static int noun = 110;
    public static int verb = 111;
    public static int adjective = 112;
    public static int adverb = 113;
    public static int _array = 121;
    public static int numberId = 123;
    public static int _string = 124; // text
    public static int _float = 125; //naa, see number
    public static int _integer = 126; //naa, see number
    public static int _date = 127;
    public static int unit = 128;
    public static int _range = 129;

// syntactic relations:
    public static int singular = 130;
    public static int _plural = 131;
    public static int present_tense = 132;
    public static int past_tense = 133;
    public static int future_tense = 134;
    public static int translation = 139;

    public static int _And = 140;
    public static int _Or = 141;
    public static int _Not = 142;
    public static int _Equals = 150;
    public static int _Greater = 151;
    public static int _Less = 152;
    public static int _Between = 153;
    public static int _Circa = 154;
    public static int _Much = 155;
    public static int _Very = 156;
    public static int _Contains = 157;
    public static int _StartsWith = 158;
    public static int _EndsWith = 159;
    public static int _false = 200;// not '0' by design!
    public static int _true = 201;

    public static int _see=0,//50 also, 40 similar
            _Hypernym=1,//SuperClass
            _hyponym=2,//SubClass
            _Type=3,
            _instance=4,
            _ENTAILMENT=21,// implies
            _Part=11,
            _PartOf=12,
            _Member=13,
            _Owner=14,
            _Substance=15,
            _SubstanceOwner=16,
            _PARTICIPLE_OF_VERB=71,
            _PERTAINYM=80,
            _antonym=30,
            _attribute=60,
            _cause=23,
            _derived=81,
    //	_derived_from_noun=81,//?
    _synonym=21,//32?
            _DOMAIN_CATEGORY=91,
            _DOMAIN_REGION=93,
            _DOMAIN_USAGE=95,
            _MEMBER_DOMAIN_CATEGORY=92,
            _MEMBER_DOMAIN_REGION=94,
            _MEMBER_DOMAIN_USAGE=96,
            _VERB_GROUP=70;


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
