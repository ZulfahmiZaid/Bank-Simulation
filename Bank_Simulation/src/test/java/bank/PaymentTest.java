package bank;

import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment
            testSubject_P1,
            testSubject_P2;

    @BeforeEach
    void init (){
        assertDoesNotThrow(()-> {
            testSubject_P1 = new Payment("01.12.2022", 200, "Test 1");
            testSubject_P2 = new Payment("02.12.2022", 400, "Test 2", 0.5, 0.6);
        });
    }

    @Test
    void testKonstruktor1(){
        assertEquals("01.12.2022", testSubject_P1.getDate());
        assertEquals(200, testSubject_P1.getAmount());
        assertEquals("Test 1", testSubject_P1.getDescription());

        assertEquals(0.0, testSubject_P1.getIncomingInterest());
        assertEquals(0.0, testSubject_P1.getOutgoingInterest());
    }

    @Test
    void testKonstruktor2(){
        assertEquals("02.12.2022", testSubject_P2.getDate());
        assertEquals(400, testSubject_P2.getAmount());
        assertEquals("Test 2", testSubject_P2.getDescription());
        assertEquals(0.5, testSubject_P2.getIncomingInterest());
        assertEquals(0.6, testSubject_P2.getOutgoingInterest());
    }

    @Test
    void testKonstruktor2_Exception_Incoming(){
        //falsche Wertebereich von IncomingInterest
        assertThrows(TransactionAttributeException.class, () -> {
            new Payment("02.12.2022", 400, "Test 2", 400, 0.2);
        });
    }

    @Test
    void testKonstruktor2_Exception_Outgoing(){
        //falsche Wertebereich von OutgoingInterest
        assertThrows(TransactionAttributeException.class, () -> {
            new Payment("02.12.2022", 400, "Test 2", 0.2, 400);
        });
    }

    @Test
    void testCopyKonstruktor(){
        assertDoesNotThrow( () -> {
            Payment testCopy1 = new Payment(testSubject_P2);

            assertNotSame(testCopy1, testSubject_P2);
            assertEquals(testSubject_P2.getDate(), testCopy1.getDate());
            assertEquals(testSubject_P2.getAmount(), testCopy1.getAmount());
            assertEquals(testSubject_P2.getDescription(), testCopy1.getDescription());
            assertEquals(testSubject_P2.getIncomingInterest(), testCopy1.getIncomingInterest());
            assertEquals(testSubject_P2.getOutgoingInterest(), testCopy1.getOutgoingInterest());

            double temp = testSubject_P2.getAmount();
            testSubject_P2.setAmount(200);

            assertEquals(200, testSubject_P2.getAmount());
            assertEquals(temp, testCopy1.getAmount());
            assertNotEquals(testSubject_P2.getAmount(), testCopy1.getAmount());
        }
        );
    }
    @ParameterizedTest
    @ValueSource(doubles = {1.0,0.0,0.4,0.5,0.28654})
    void testIncomingInterest (double richtig){
        assertDoesNotThrow(()->{
            new Payment("00.00.0000", 300,"Test Amount",richtig,0.3);
        });
    }
    @ParameterizedTest
    @ValueSource(doubles = {4000,300,20,10,500})
    void testIncomingInterest_falsch (double falsch){
        assertThrows(TransactionAttributeException.class, ()->{
            new Payment("00.00.0000", 300,"Test Amount",falsch,0.3);
        });
    }
    @ParameterizedTest
    @ValueSource(doubles = {1.0,0.0,0.4,0.5,0.28654})
    void testOutgoingInterest (double richtig){
        assertDoesNotThrow(()->{
            new Payment("00.00.0000", 300,"Test Amount",0.3,richtig);
        });
    }
    @ParameterizedTest
    @ValueSource(doubles = {4000,300,20,10,500})
    void testOutgoingInterest_falsch (double falsch){
        assertThrows(TransactionAttributeException.class, ()->{
            new Payment("00.00.0000", 300,"Test Amount",0.3,falsch);
        });
    }

    @Test
    void testCalculate(){
        assertDoesNotThrow( () ->{
         Payment testSubject_P3 = new Payment("02.04.2022",-300,"Test Auszahlung", 0.3,0.6);

         double einzahlung = testSubject_P2.getAmount();
         double auszahlung = testSubject_P3.getAmount();

         //Default Werte, beide Zinsen sind auf 0.0 gesetzt
         assertEquals(testSubject_P1.getAmount(),testSubject_P1.calculate());

         //calculate() != amount
         assertNotEquals(einzahlung, testSubject_P2.calculate());
         assertNotEquals(auszahlung, testSubject_P3.calculate());

         //Rückrechnung
         assertEquals(einzahlung, testSubject_P2.calculate() + (einzahlung * testSubject_P2.getIncomingInterest()));
         assertEquals(auszahlung, testSubject_P3.calculate() - (auszahlung * testSubject_P3.getOutgoingInterest()));

         //calculate ändert den Wert von amount nicht
        double temp_P2 = testSubject_P2.calculate();
        double temp_P3 = testSubject_P3.calculate();

        assertEquals(einzahlung, testSubject_P2.getAmount());
        assertEquals(auszahlung,testSubject_P3.getAmount());

        }
        );    }

    @Test
    void testIncomingInterest(){
        //wenn nicht durch der erste Konstruktor gesetzt wurde
        assertEquals(0, testSubject_P1.getIncomingInterest());

        testKonstruktor2_Exception_Incoming();
        assertTrue(testSubject_P1.getIncomingInterest() >= 0.0 && testSubject_P1.getIncomingInterest() <= 1);
        assertTrue(testSubject_P2.getIncomingInterest() >= 0.0 && testSubject_P2.getIncomingInterest() <= 1);
    }

    @Test
    void testOutgoingInterest(){
        //wenn nicht durch der erste Konstruktor gesetzt wurde
        assertEquals(0, testSubject_P1.getOutgoingInterest());

        testKonstruktor2_Exception_Outgoing();
        assertTrue(testSubject_P1.getOutgoingInterest() >= 0.0 && testSubject_P1.getOutgoingInterest() <= 1);
        assertTrue(testSubject_P2.getOutgoingInterest() >= 0.0 && testSubject_P2.getOutgoingInterest() <= 1);
    }

    @Test
    void testEquals(){
        assertDoesNotThrow( () -> {
            Payment copy_P1 = new Payment(testSubject_P1);
            Transfer testGetClass = new Transfer("00.00.0000", 0, "TEST");

            //nicht das selbe Objekt aber hat die gleichen Attributewerte
            assertNotSame(copy_P1, testSubject_P1);
            assertEquals(testSubject_P1.getClass(),copy_P1.getClass());
            assertEquals(testSubject_P1,copy_P1);

            assertEquals(testSubject_P1.getClass(), testSubject_P2.getClass());
            assertNotEquals(testSubject_P1,testGetClass);
            assertNotEquals(testSubject_P2,copy_P1);
        });
    }

    @Test
    void testToString(){
        //assertTrue(testSubject_P1.toString instanceof String)
        assertNotNull(testSubject_P1.toString());
        assertNotNull(testSubject_P2.toString());

        //assertTrue(testSubject_P1.toString().equals(____)
        assertEquals(testSubject_P1.toString(),
                "Date :" + testSubject_P1.getDate() + "\n" +
                        "Description: " + testSubject_P1.getDescription() + "\n" +
                        "Amount: " + testSubject_P1.calculate() + "\n" +
                        "Incoming Interest: " + testSubject_P1.getIncomingInterest() + "%" + "\n" +
                        "Outgoing Interest: " + testSubject_P1.getOutgoingInterest() + "%" + "\n");

        assertEquals(testSubject_P2.toString(),
                "Date :" + testSubject_P2.getDate() + "\n" +
                        "Description: " + testSubject_P2.getDescription() + "\n" +
                        "Amount: " + testSubject_P2.calculate() + "\n" +
                        "Incoming Interest: " + testSubject_P2.getIncomingInterest() + "%" + "\n" +
                        "Outgoing Interest: " + testSubject_P2.getOutgoingInterest() + "%" + "\n"
        );
    }
}