package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.Constants.ADD_CHILD;
import static com.example.Constants.BROTHER_IN_LAW;
import static com.example.Constants.CHILD_ADDITION_FAILED;
import static com.example.Constants.CHILD_ADDITION_SUCCEEDED;
import static com.example.Constants.DAUGHTER;
import static com.example.Constants.FEMALE;
import static com.example.Constants.MALE;
import static com.example.Constants.MATERNAL_AUNT;
import static com.example.Constants.MATERNAL_UNCLE;
import static com.example.Constants.NONE;
import static com.example.Constants.PATERNAL_AUNT;
import static com.example.Constants.PATERNAL_UNCLE;
import static com.example.Constants.PERSON_NOT_FOUND;
import static com.example.Constants.SIBLING;
import static com.example.Constants.SISTER_IN_LAW;
import static com.example.Constants.SON;

class Family{
    HashMap<String, People> members;

    Family(){
        init();
        intitDefaultChildren();
    }



    private void init() {
        members = new HashMap<>();
        People anga  = new People("Anga", null, FEMALE, null );
        People shan =  new People("Shan", null, MALE, anga);
        anga.setSpouse(shan);

        members.put(anga.getName(), anga);
        members.put(shan.getName(), shan);
    }

    private void intitDefaultChildren() {
        //Level 1
        insertChild("Chit", MALE, "Anga");
        insertChild("Ish", MALE, "Anga");
        insertChild("Vich", MALE, "Anga");
        insertChild("Aras", MALE, "Anga");
        insertChild("Satya", FEMALE, "Anga");

        insertSpouse("Amba", "Chit");
        insertSpouse("Lika", "Vich");
        insertSpouse("Chitra", "Aras");
        insertSpouse("Vyan", "Satya");

        //Level 2
        insertChild("Dritha", FEMALE, "Amba");
        insertChild("Vritha", MALE, "Amba");
        insertChild("Tritha", FEMALE, "Amba");

        insertChild("Vila", FEMALE, "Lika");
        insertChild("Chika", FEMALE, "Lika");

        insertChild("Jnki", FEMALE, "Chitra");
        insertChild("Ahit", MALE, "Chitra");

        insertChild("Asva", MALE, "Satya");
        insertChild("Vyas", MALE, "Satya");
        insertChild("Atya", FEMALE, "Satya");

        insertSpouse("Jaya", "Dritha");
        insertSpouse("Arit", "Jnki");
        insertSpouse("Satvy", "Asva");
        insertSpouse("Krpi", "Vyas");

        // Level 3
        insertChild("Yodhan", MALE, "Dritha");

        insertChild("Laki", MALE, "Jnki");
        insertChild("Lavnya", FEMALE, "Jnki");

        insertChild("Vasa", MALE, "Satvy");

        insertChild("Kriya", MALE, "Krpi");
        insertChild("Krithi", FEMALE, "Krpi");

    }
    private int insertChild(String childName, String gender, String mother) {
        People mum = members.get(mother);

        if(mum != null){
            if(mum.getGender().equals(MALE)){
                return -1;
            }
            People child = new People(childName, mum, gender, null);
            members.put(child.getName(), child);
            if(child.getGender().equals(MALE)){
                mum.getSons().add(child);
            }else{
                mum.getDaughters().add(child);
            }
            return 0;
        }
        return 1;


    }

    private boolean insertSpouse(String newSpouse, String name) {
        People p = members.get(name);
        People spouse = members.get(newSpouse);
        if(p != null){
            if (null != spouse) {
                p.setSpouse(spouse);
            } else {
                People sp = new People(newSpouse, null, getOpposite(p.getGender()), p);
                p.setSpouse(sp);
                members.put(sp.getName(), sp);
            }
            return true;
        }
        return false;

    }

    private String getOpposite(String gender) {
        return FEMALE.equalsIgnoreCase(gender) ? MALE : FEMALE;
    }
    
    private People getHusband(People p){
        if(p.getGender().equalsIgnoreCase(FEMALE)){
            return p.getSpouse();
        }
        else return null;
    }
    private People getWife(People p){
        if(p.getGender().equals(MALE)){
            return p.getSpouse();
        }
        else return null;
    }

    public List<People> findPaternalUncle(People p){
        People father = p.getMother().getSpouse();
        List<People> patUncles;
        if(father != null && father.getMother()!= null){
            patUncles = father.getMother().getSons();
            return patUncles.stream().filter(pUncle -> !pUncle.getName().equals(father.getName())).collect(Collectors.toList());
        }
        return null;
    }

    public List<People>  findMaternalUncle(People p){
        People mother = p.getMother();
        List<People> matUncles;

        if(mother.getMother()!= null){
            matUncles = mother.getMother().getSons();
            /*
            Question did not mention about illegal relationship so just to make sure there are no such cases
            Since names are unique no uncles will be missed from this filter
             */
            return matUncles.stream().filter(matUncle -> !matUncle.getName().equals(mother.getSpouse().getName())).collect(Collectors.toList());
        }
        return null;
    }

    public List<People>  findPaternalAunt(People p){
        People father = p.getMother().getSpouse();
        List<People> patAunts;
        if(father != null && father.getMother()!= null){
            patAunts = father.getMother().getDaughters();
             /*
            Question did not mention about illegal relationship so just to make sure there are no such cases
            Since names are unique no aunts will be missed from this filter
             */
            return patAunts.stream().filter(patAunt -> !patAunt.getName().equals(p.getMother().getName())).collect(Collectors.toList());
        }
        return null;
    }
    public List<People>  findMaternalAunt(People p){
        People mother = p.getMother();
        List<People> matAunts;

        if(mother.getMother()!= null){
            matAunts = mother.getMother().getDaughters();

            return matAunts.stream().filter(matAunt -> !matAunt.getName().equals(mother.getName())).collect(Collectors.toList());
        }
        return null;
    }
    public List<People> findSisterInLaw(People p){
        List<People> result =  new ArrayList<>();
        //Spouse’s sisters
        if(p.getSpouse()!=null){
            result.addAll(p.getSpouse().getMother().getDaughters().stream().filter(people -> !people.getName().equals(p.getSpouse().getName())).collect(Collectors.toList()));
        }
        List<People> brothers = p.getMother().getSons();

        //Wives of siblings
        result.addAll(brothers.stream().map(People::getSpouse)
                .filter(spouse -> null != spouse
                        && !spouse.getName().equals(p.getSpouse() == null ? "" : p.getSpouse().getName())
                        && !spouse.getName().equals(p.getName())
                ).collect(Collectors.toList()));
        return result;

    }
    public List<People> findBrotherInLaw(People p){
        People spouse = p.getSpouse();
        List<People> result =  new ArrayList<>();

        //Spouse’s brothers and also adding un necessary filter for p.getName == br.getName for illegal relationship handling
        if(spouse != null){
            result.addAll(spouse.getMother().getSons().stream()
                    .filter(br -> !br.getName().equals(spouse.getName()) && !br.getName().equals(p.getName()))
                    .collect(Collectors.toList()));
        }

        //Husbands of siblings
        List<People> sisters = p.getMother().getDaughters();

        result.addAll(sisters.stream().map(People::getSpouse)
                .filter(husband -> husband!=null &&
                        !husband.getName().equals(spouse == null ? "" : spouse.getName())
                        && !husband.getName().equals(p.getName())
                ).collect(Collectors.toList()));

        return result;

    }
    public List<People> getSon(People p){
        if(p.getGender().equalsIgnoreCase(FEMALE)){
            return p.getSons();
        }else{
            return p.getSpouse() == null? null: p.getSpouse().getSons();
        }
    }
    public List<People> getDaughter(People p){
        if(p.getGender().equalsIgnoreCase(FEMALE)){
            return p.getDaughters();
        }else{
            return p.getSpouse() == null? null: p.getSpouse().getDaughters();
        }
    }
    public List<People> getSiblings(People p){
        List<People> result = new ArrayList<>();
        result.addAll(p.getMother().getSons());
        result.addAll(p.getMother().getDaughters());

        return result.stream().filter(people -> !p.getName().equals(people.getName())).collect(Collectors.toList());
    }

    public void handleTest(String s) throws Exception {
        assert s!=null && s.equals("");
        String arr[]  = s.split(" ");

        if(arr.length<3){
            throw new Exception("Invalid Test Case");
        }
        String command  = arr[0];

        if(command.equalsIgnoreCase(ADD_CHILD)){
            handleModify(arr);
        }else{
            handleVerify(arr);
        }
    }

    private void handleVerify(String[] arr) {
        String name = arr[1];
        String relationShip = arr[2];
        if(members.get(name) == null ){
            System.out.println(PERSON_NOT_FOUND);
        }else{
            List<People> related = handleRelationShip(name, relationShip);
            if(related == null || related.isEmpty()){
                System.out.println(NONE);
            }else{
                printRelated(related);
            }
        }
    }

    private void printRelated(List<People> related) {
        StringBuilder res = new StringBuilder();
        for(People p : related){
            res.append(p.getName()).append(" ");
        }
        res.substring(0, res.length()-1);
        System.out.println(res);
    }

    private List<People> handleRelationShip(String name, String relationShip) {
        People p = members.get(name);
        List<People> res ;
        switch (relationShip){
            case PATERNAL_UNCLE:
                res = findPaternalUncle(p);
                break;
            case MATERNAL_UNCLE:
                res = findMaternalUncle(p);
                break;
            case PATERNAL_AUNT:
                res = findPaternalAunt(p);
                break;
            case MATERNAL_AUNT:
                res = findMaternalAunt(p);
                break;
            case SISTER_IN_LAW:
                res = findSisterInLaw(p);
                break;
            case BROTHER_IN_LAW:
                res = findBrotherInLaw(p);
                break;
            case SON:
                res = getSon(p);
                break;
            case DAUGHTER:
                res = getDaughter(p);
                break;
            case SIBLING:
                res = getSiblings(p);
                break;

            default:
                res = new ArrayList<>();

        }

        return res;
    }

    private void handleModify(String[] arr) {
        String motherName = arr[1];
        String childName = arr[2];
        String gender = arr[3];
        int insert = insertChild(childName, gender, motherName);
        if(insert == 1){
            System.out.println(PERSON_NOT_FOUND);
        }else if( insert == 0){
            System.out.println(CHILD_ADDITION_SUCCEEDED);
        }else{
            System.out.println(CHILD_ADDITION_FAILED);
        }
    }

}
