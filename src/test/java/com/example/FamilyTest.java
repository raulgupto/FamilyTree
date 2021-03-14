package com.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Testing correct creation of Family of Anga and Shan")
class FamilyTest {

    Family family;
    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeAll
    void initFamily(){
        this.family = new Family();
        assertAll(
                // Check if Basic Family Setup is correct :  few test cases only
                ()-> assertEquals("Anga", family.members.get("Shan")!=null ? family.members.get("Shan").getSpouse().getName(): ""),
                ()-> assertEquals("Shan", family.members.get("Anga") !=null ? family.members.get("Anga").getSpouse().getName(): ""),
                ()-> assertTrue(family.members !=null && family.members.containsKey("Chit")),
                ()-> assertTrue(family.members !=null && family.members.containsKey("Ish")),
                ()-> assertTrue(family.members !=null && family.members.containsKey("Vich")),
                ()-> assertTrue(family.members !=null && family.members.containsKey("Aras")),
                ()-> assertTrue(family.members !=null && family.members.containsKey("Satya")),
                () -> {
                    assert family.members != null;
                    assertEquals(family.members.get("Amba").getSpouse().getName(), "Chit");
                },
                () -> {
                    assert family.members != null;
                    assertEquals(family.members.get("Jaya").getSpouse().getName(), "Dritha");
                }
        );

    }
    @AfterAll
    void terminate(){
        testReporter.publishEntry(" Running" + "Tests Completed!");
    }

    @BeforeEach
    void init(TestReporter testReporter, TestInfo testInfo){
        this.testInfo = testInfo;
        this.testReporter = testReporter;
    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Laki" })
    @DisplayName("Paternal Uncle Test")
    @Tag("Paternal")
    void findPaternalUncle(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> paternalUncles = family.findPaternalUncle(p);

        if(null == p.getMother() || null == p.getMother().getSpouse().getMother()){
            assertTrue(paternalUncles == null || paternalUncles.isEmpty());

        }else{
            List<People> validUncles = family.getSon(p.getMother().getSpouse().getMother()).stream()
                    .filter(uncle ->  !uncle.getName().equals(p.getMother().getSpouse().getName()))
                    .collect(Collectors.toList());
            assertTrue(validUncles.size() == paternalUncles.size() && validUncles.containsAll(paternalUncles) && paternalUncles.containsAll(validUncles));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha", "Laki","Kriya", "Lavnya","Yodhan" })
    @DisplayName("Maternal Uncle Test")
    @Tag("Maternal")
    void findMaternalUncle(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> maternalUncles = family.findMaternalUncle(p);

        if(p.getMother() == null || p.getMother().getMother() == null ){
            assertTrue(maternalUncles == null || maternalUncles.isEmpty());
        }else{
            List<People> validMaternalUncles = family.findBrotherInLaw(p.getMother().getSpouse());
            assertTrue( validMaternalUncles.containsAll(maternalUncles));
        }


    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha" })
    @Tag("Paternal")
    @DisplayName("Paternal Aunt Test")
    void findPaternalAunt(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);

        List<People> paternalAunt = family.findPaternalAunt(p);

        if(null == p.getMother() || null == p.getMother().getSpouse().getMother()){
            assertTrue(paternalAunt == null || paternalAunt.isEmpty());

        }else{
            List<People> validAunts = family.getDaughter( p.getMother().getSpouse().getMother());
            assertTrue(validAunts.containsAll(paternalAunt) );
        }



    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @DisplayName("Maternal Aunt Test")
    @Tag("Maternal")
    void findMaternalAunt(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> maternalAunts = family.findMaternalAunt(p);

        if(p.getMother() == null || p.getMother().getMother() == null){
            assertNull(maternalAunts);
        }else{
            List<People> validMAunts = p.getMother().getMother().getDaughters().stream().filter(mAunt -> !mAunt.getName().equals(p.getMother().getName()))
                    .collect(Collectors.toList());
            assertTrue(validMAunts.size() == maternalAunts.size() && validMAunts.containsAll(maternalAunts));
        }

    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @DisplayName("Sister in Law Test")
    @Tag("InLaws")
    void findSisterInLaw(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);

        List<People> validSisInLaw =  new ArrayList<>();
        //Spouse’s sisters
        if(p.getSpouse()!=null && p.getSpouse().getMother()!=null){
            validSisInLaw.addAll(p.getSpouse().getMother().getDaughters().stream().filter(people -> !people.getName().equals(p.getSpouse().getName())).collect(Collectors.toList()));
        }
        if(null != p.getMother()){
            List<People> brothers = p.getMother().getSons();

            //Wives of siblings
            validSisInLaw.addAll(brothers.stream().map(People::getSpouse)
                    .filter(spouse -> null != spouse
                            && !spouse.getName().equals(p.getSpouse() == null ? "" : p.getSpouse().getName())
                            && !spouse.getName().equals(p.getName())
                    ).collect(Collectors.toList()));
        }
        List<People> sisterInLawActual = family.findSisterInLaw(p);
        assertTrue(sisterInLawActual.size() == validSisInLaw.size() && validSisInLaw.containsAll(sisterInLawActual));


    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @DisplayName("Brother In Law Test")
    @Tag("InLaws")
    void findBrotherInLaw(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> validBrotherInLaw =  new ArrayList<>();

        People spouse = p.getSpouse();
        if(spouse != null && spouse.getMother() != null){
            validBrotherInLaw.addAll(spouse.getMother().getSons().stream()
                    .filter(br -> !br.getName().equals(spouse.getName()) && !br.getName().equals(p.getName()))
                    .collect(Collectors.toList()));
        }
        if(null != p.getMother()){
            List<People> sisters = p.getMother().getDaughters();
            validBrotherInLaw.addAll(sisters.stream().map(People::getSpouse)
                    .filter(husband -> husband!=null &&
                            !husband.getName().equals(spouse == null ? "" : spouse.getName())
                            && !husband.getName().equals(p.getName())
                    ).collect(Collectors.toList()));
        }
        List<People> actualBrotherInLaw = family.findBrotherInLaw(p);
        assertTrue(actualBrotherInLaw.size() == validBrotherInLaw.size() && validBrotherInLaw.containsAll(actualBrotherInLaw));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @Tag("Children")
    void getSon(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> sons = family.getSon(p);
        if(p.getSpouse() == null){
            assertTrue(sons == null || sons.size()==0);
        }else{
            if(p.getGender().equalsIgnoreCase("FEMALE")){
                assertTrue(sons.size() == p.getSons().size() && sons.containsAll(p.getSons()));
            }else{
                assertTrue(sons.size() == p.getSpouse().getSons().size() && sons.containsAll(p.getSpouse().getSons()));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @DisplayName("Find Daughter Test")
    @Tag("Children")
    void getDaughter(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> daughter = family.getDaughter(p);
        if(p.getSpouse() == null){
            assertTrue(daughter == null || daughter.size()==0);
        }else{
            if(p.getGender().equalsIgnoreCase("FEMALE")){
                assertTrue(daughter.size() == p.getDaughters().size() && daughter.containsAll(p.getDaughters()));
            }else{
                assertTrue(daughter.size() == p.getSpouse().getDaughters().size() && daughter.containsAll(p.getSpouse().getDaughters()));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Dritha","Vritha","Tritha","Kriya"})
    @DisplayName("Find Siblings Test")
    @Tag("Siblings")
    void getSiblings(String name) {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
        People p = family.members.get(name);
        List<People> siblings = family.getSiblings(p);
        if(null == p.getMother()){
            assertEquals(siblings.size(), 0);
        }else{
            List<People> validSiblings =new ArrayList<>();
            validSiblings.addAll(p.getMother().getSons());
            validSiblings.addAll(p.getMother().getDaughters());
            List<People> list = new ArrayList<>();
            for (People people : validSiblings) {
                if (!p.getName().equals(people.getName())) {
                    list.add(people);
                }
            }
            validSiblings = list;
            assertTrue(validSiblings.size() == siblings.size() && validSiblings.containsAll(siblings ));
        }

    }

    @Test
    @Disabled
    @DisplayName("Verify String in File is not null")
    void handleTest() {
        testReporter.publishEntry(" Running" + testInfo.getDisplayName());
      /*  assertNotNull("Input String", ()-> {
            return "Input String is null";
        });*/
    }
}