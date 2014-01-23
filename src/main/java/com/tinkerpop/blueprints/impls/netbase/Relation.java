package com.tinkerpop.blueprints.impls.netbase;

import com.sun.org.apache.bcel.internal.classfile.Unknown;
import com.sun.org.apache.xpath.internal.operations.Equals;
import com.sun.tools.corba.se.idl.constExpr.Not;
import com.tinkerpop.blueprints.Contains;
import com.tinkerpop.blueprints.Edge;
import javafx.scene.Parent;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.IdentityHashMap;

/**
 * Copyright 2013 Pannous GmbH
 * <p/>
 * User: info@pannous.com / me
 * Date: 21/01/14
 * Time: 17:24
 */
public class Relation extends Node {

    private static final IdentityHashMap<Integer, String> names = new IdentityHashMap<>();
    private static final IdentityHashMap<Integer, Relation> relations = new IdentityHashMap<>();
    private static final HashMap<String, Relation> relationByName = new HashMap<>();

    //    public static OnceVector<Relation> all = new OnceVector<Relation>();
    public static final int ANY = 333;
    public static final int ERROR = 666;

    public static int wordnet = 0;// 303; //context==Class??   TODO!
    // DANGER!!
    public static int internal = 100;
    public static int node = 101;// dont change
    public static int _abstract = 102;
    //int Class=103;
    public static int klass = 103;
    public static int clazz = 103;
    public static int concept = 103;
    public static int object = 104;
    //int Object=104;
    public static int relation = 105;
    public static int person = 106;
    public static int reification = 107;//== statement ??
    public static int pattern = 108;
    public static int statement = 109;

// wn: done via type(33) statements

    public static int _attribute = 42;
    public static int noun = 110;
    public static int verb = 111;
    public static int adjective = 112;
    public static int adverb = 113;
    public static int array = 121;
    public static int list = 122;// because T == [] or ArrayList ????
    public static int number= 123;
    public static int integer = 124; //naa, see number
    public static int _float = 125; //naa, see number
    public static int string = 126; // text
    public static int date = 127;
    public static int unit = 128;
    public static int range = 129;

    // syntactic relations:
    public static int singular = 130;
    public static int plural = 131;
    public static int presenttense = 132;
    public static int pasttense = 133;
    public static int futuretense = 134;
    public static int translation = 139;

    public static int and = 140;
    public static int or = 141;
    public static int not = 142;
    public static int equals = 150;
    public static int greater = 151;
    public static int less = 152;
    public static int between = 153;
    public static int circa = 154;
    public static int much = 155;
    public static int very = 156;
    public static int contains = 157;
    public static int startswith = 158;
    public static int endswith = 159;
    public static int _false = 200;// not '0' by design!
    public static int _true = 201;

    public static int see = 0;//50 also, 40 similar
    public static int hypernym = 1;//superclass
    public static int hyponym = 2,//subclass
            type = 3,
            instance = 4,
            entailment = 21,// implies
            part = 11,
            partof = 12,
            member = 13,
            owner = 14,
            substance = 15,
            substanceowner = 16,
            participleofverb = 71,
            pertainym = 80,
            attribute = 60,
            cause = 23,
            derived = 81,
    //	derivedfromnoun=81,//?
    synonym = 21,//32?
            domaincategory = 91,
            domainregion = 93,
            domainusage = 95,
            memberdomaincategory = 92,
            memberdomainregion = 94,
            memberdomainusage = 96,
            verbgroup = 70;
    public static int antonym = 30;


    static Relation Unknown;
    static Relation Antonym;
    static Relation Parent;
    static Relation SuperClass; // Parent
    // static Relation IsA;// Parent
    static Relation SubClass;
    static Relation Cause;
    static Relation Entailment; // Implication
    static Relation Owner; //Owner inverse Member   (Prince of Persia) <=> (Persia has Prince)
    static Relation Member;
    static Relation Part;
    static Relation PartOf;
    static Relation Substance;
    static Relation Synonym;
    static Relation Domain;
    static Relation PERTAINYM;
    static Relation Weight;
    static Relation Type;
    static Relation Instance;

    static Relation Active;
    static Relation Passive;
    static Relation Tag;
    static Relation Label;
    static Relation BackLabel;
    static Relation Comment;
    static Relation Labels;
    static Relation LabeledNode;
    static Relation Category;
    static Relation SubContext;
    static Relation SuperContext;

    // Types
    static Relation Internal;
    static Relation Node;
    static Relation Thing;
    static Relation Item;
    static Relation Abstract; // Thing
    static Relation Class;
    static Relation Object;
    static Relation Relation;
    static Relation Reification;
    static Relation Property;
    static Relation Pattern; // temporary

    static Relation Any;
    // Semantics
    static Relation Attribute; //ok, if  static Relation Attribute declared in netbase.cpp
// static Relation Is; // danger: obama is president etc

    static Relation Person;
    static Relation Adjective;
    static Relation Noun;
    static Relation Verb;
    static Relation Adverb;

    static Relation Derived;// Adjective^-1
    static Relation DerivedFromNoun;

    static Relation Number;
    static Relation String;
    static Relation Date;
    static Relation Float;
    static Relation Integer;
    static Relation Array;
    static Relation List;
    static Relation Range;
    static Relation Unit;

    static Relation True;
    static Relation False;

    static Relation Translation;
    static Relation Plural;
    static Relation And;
    static Relation Or;
    static Relation Not;

    //There are about 150 prepositions in English
    static Relation Of; // ==owner
    static Relation In; // ==location,...
    static Relation To; // ==direction,<object>
    static Relation From;
    static Relation By;
    static Relation For;
    static Relation On;


    static Relation Equals;
    static Relation Greater;
    static Relation More;
    static Relation Smaller;
    static Relation Less; //smaller
    static Relation Between;
    static Relation Circa;
    static Relation Much;
    static Relation Little;
    static Relation Very;
    static Relation Contains;
    static Relation StartsWith;
    static Relation EndsWith;
    static Relation UsageContext;


    static {
        // AFTER Variables are initialized!!!!
        initRelations();
    }



    public Relation(int id, String name) {
        super(id);
        setAutoRead(false);
        setAutoSynch(false);
        setAutoWrite(false);
//        super(id);
        this.id = id;
        this.name = name;
//        this._name = name;
        names.put(id, name);
        relations.put(id, this);
        relationByName.put(name, this);
    }

    @Override
    public String getName() {
        return name;
//        return _name;
    }

    static void initRelations() {
//        if(germanLabels){initRelationsDE();return;}
        Unknown = addRelation(see, "see");
        Antonym = addRelation(antonym, "antonym");
//	Part = addRelation(1, "part"); USA etc BUG!?!!
        Attribute = addRelation(attribute, "attribute"); // tag
        Property = Attribute;
        boolean istransitive = true;
        Cause = addRelation(cause, "cause", istransitive); //
        Derived = addRelation(derived, "derived"); //
//	DerivedFromNoun =addRelation(derivedfromnoun, "derived from noun"); //
        //        DOMAINOFSYNSETCATEGORY =
        UsageContext = addRelation(domaincategory, "usage context"); // # sheet @ maths   // think of!! OWNER -> Part
        //    DOMAINOFSYNSETREGION =
        addRelation(domainregion, "usage region"); // mate @ australia
        //    DOMAINOFSYNSETUSAGE =
        Domain = addRelation(domainusage, "usage domain"); // #bitch @ colloquialism  || fin de siecle @ French   # fuck(vulgar)
        //    ENTAILMENT =
        addRelation(entailment, "entails", istransitive); //ENTAILMENT  jump implies come down
        SuperClass = addRelation(hypernym, "superclass", istransitive); //Parent ,"Hypernym"
        Parent = SuperClass;
        //	IsA = SuperClass;

        SubClass = addRelation(hyponym, "subclass", istransitive); // hyponym
        Owner = addRelation(owner, "of", istransitive);
        Member = addRelation(member, "has");
        //	MEMBEROFTHISDOMAINCATEGORY=
        addRelation(memberdomaincategory, "contextual word"); //aviation: to overfly
        //	MEMBEROFTHISDOMAINREGION =
        addRelation(memberdomainregion, "regional word"); //-r  IN Japan :  Sushi
        //	MEMBEROFTHISDOMAINUSAGE =
        addRelation(memberdomainusage, "domain word"); // colloquialism: bitch
        PartOf = addRelation(partof, "part of", istransitive);
        Part = addRelation(part, "part", istransitive);
        //	PARTICIPLEOFVERB =
        addRelation(participleofverb, "participle");
        PERTAINYM = addRelation(pertainym, "pertainym"); //  # cellular(a) \ cell(n) 	equally(adv)-equal(adj)

        Synonym = addRelation(synonym, "synonym", istransitive); // similar?? 32??
        //    Similar = addRelation(21, "similar");//synonym ??

        //	SubstanceOwner=
        addRelation(substanceowner, "substance of", istransitive);
        Substance = addRelation(substance, "substance", istransitive);
        //	VERBGROUP=
        addRelation(verbgroup, "verb group");
        //	RELATIONSHIPCOUNT =
//	addRelation(25, "RELATIONSHIPCOUNT");
        addRelation(97, "domain");
        addRelation(98, "MEMBER");// ??


        Weight = addRelation(31, "weight");
        //	 Synonym = Relation(32, "synonym");// -> 21????? See see | tag
        Type = addRelation(type, "type");// istransitive?? // (instance->class) !=SuperClass
        Instance = addRelation(instance, "instance", istransitive);
        Active = addRelation(35, "active");
        Passive = addRelation(36, "passive");
        Tag = addRelation(37, "tag"); // different to 'unknown' !!
        Label = addRelation(38, "label");
        BackLabel = addRelation(39, "label of");
        addRelation(40, "similar");// hypernym?? no synonym
        addRelation(50, "also");// hypernym??
        //	Labels = addRelation(40, "Label");//??
        //	LabeledNode = addRelation(41, "LabeledNode");// ?? ugly!!
        Category = addRelation(43, "category"); // tag
        SubContext = addRelation(44, "subcontext"); // tag
        SuperContext = addRelation(45, "supercontext"); //
        Comment = addRelation(46, "comment");

        // Node Types!
    /*
	31 Weight
	32 Unknown
	33 Type
	34 Instance
	35 Active
	36 Passive
	37 Tag
	38 Label
	39 BackLabel
	41 LabeledNode
	42 Attribute
	 */
        //Context= addRelation(99, "context");
        Internal = addRelation(internal, "internal"); //ok
        Node = addRelation(node, "node");
        Abstract = addRelation(_abstract, "abstract");
        Class = addRelation(clazz, "class");
        Object = addRelation(object, "object");
        Relation = addRelation(relation, "relation");
        Pattern = addRelation(pattern, "pattern");
        Reification = addRelation(reification, "reification");

        // Thing   = addRelation(101, "thing");
        // Item    = addRelation(101, "item");
        Person = addRelation(person, "person");
        Adjective = addRelation(adjective, "adjective");
        Noun = addRelation(noun, "noun");
        Verb = addRelation(verb, "verb");
        Adverb = addRelation(adverb, "adverb");
        Number = addRelation(number, "number");
        Unit = addRelation(unit, "unit");
        Array = addRelation(array, "array");
        List = addRelation(list, "list");

        Plural = addRelation(plural, "plural");
        Translation = addRelation(translation, "translation");

        And = addRelation(and, "and");
        Or = addRelation(or, "or");
        Not = addRelation(not, "not");
        Any = addRelation(666, "*");

        Equals = addRelation(equals, "=");
        Greater = addRelation(greater, ">");
        More = Greater;
        Less = addRelation(less, "<"); //smaller
        Smaller = Less;
        Between = addRelation(between, "Between");
        Circa = addRelation(circa, "Circa");
        Much = addRelation(much, "much");
        Little = addRelation(much, "little");
        Very = addRelation(very, "very");
        Contains = addRelation(contains, "Contains");
        StartsWith = addRelation(startswith, "starts with");
        EndsWith = addRelation(endswith, "ends with");

        String = addRelation(string, "String");
        Date = addRelation(date, "Date");
        Float = addRelation(_float, "Float");
        Integer = addRelation(integer, "Integer");
        Array = addRelation(array, "Integer");
        Range = addRelation(range, "Range");

        True = addRelation(_true, "True");
        False = addRelation(_false, "False");// todo
    }

    private static Relation addRelation(int id, String name) {
        return new Relation(id, name);
    }
    private static Relation addRelation(int id, String name, boolean istransitive) {
        return addRelation(id, name);
    }



    public static String name(int kind) {
        return names.get(kind);
    }
    public static Relation byName(String kind) {
        return relationByName.get(kind);
    }
    public static  Relation byId(int kind) {
        return relations.get(kind);
    }

//
}
