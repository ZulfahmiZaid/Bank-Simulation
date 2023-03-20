package bank;

import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {
    Transfer testSubject_T1, testSubject_T2;

    @BeforeEach
    void init() {
        assertDoesNotThrow(()->{
            testSubject_T1 = new Transfer("01.01.2001", 200, "Test 1");
            testSubject_T2 = new Transfer("02.02.2002", 400, "Test 2", "SENDER", "RECIPIENT");
        });
    }


    @Test
    void testKonstruktor1(){
        assertNull(testSubject_T1.getSender());
        assertNull(testSubject_T1.getRecipient());
    }

    @Test
    void testKonstruktor1_Exception(){
        assertThrows(TransactionAttributeException.class, () -> {
            new Transfer("03.03.2003", -700, "Test K1 Exception");
        });
    }

    @Test
    void testKonstruktor2(){
        assertNotNull(testSubject_T2.getSender());
        assertNotNull(testSubject_T2.getRecipient());
    }

    @Test
    void testKonstruktor2_Exception(){
        assertThrows(TransactionAttributeException.class, () -> {
            new Transfer("03.03.2003", -700, "Test K1 Exception", "SENDER", "RECIPIENT");
        });
    }

    @Test
    void testCopyKonstruktor(){
        assertDoesNotThrow( () -> {
            Transfer testCopy1 = new Transfer(testSubject_T2);

            assertNotSame(testCopy1, testSubject_T2);
            assertEquals(testCopy1,testSubject_T2);

            double temp = testSubject_T2.getAmount();
            testSubject_T2.setAmount(200);

            assertEquals(200, testSubject_T2.getAmount());
            assertEquals(temp, testCopy1.getAmount());
            assertNotEquals(testSubject_T2.getAmount(), testCopy1.getAmount());
        }
        );}

    @Test
    void TestSetterGetterSender(){
        assertDoesNotThrow( () -> {
            Transfer copyTestSetter = new Transfer(testSubject_T1);

            String change = "NEW SENDER";
            String old = copyTestSetter.getSender();
            copyTestSetter.setSender(change);

            assertEquals(change,copyTestSetter.getSender());
            assertNotEquals(old, copyTestSetter.getSender());

        });
    }

    @Test
    void TestSetterGetterRecipient(){
        assertDoesNotThrow( () -> {
            Transfer copyTestSetter = new Transfer(testSubject_T2);

            String change = "NEW RECIPIENT";
            String old = copyTestSetter.getRecipient();
            copyTestSetter.setRecipient(change);

            assertEquals(change,copyTestSetter.getRecipient());
            assertNotEquals(old, copyTestSetter.getRecipient());

        });
    }
    @ParameterizedTest
    @ValueSource(doubles = {200,30.00,555.5,60.95,1000})
    void testAmount (double richtigeAmount){
        assertDoesNotThrow(()->{
            new Transfer("00.00.0000", richtigeAmount,"Test Amount");
        });
    }
    @ParameterizedTest
    @ValueSource(doubles = {-2.22,-30.58,-900,-100,-44.44})
    void testAmount_Exception (double falscheAmount){
        assertThrows(TransactionAttributeException.class, ()->{
            new Transfer("00.00.0000", falscheAmount,"Test Amount");
        });
    }
    @Test
    void testCalculate(){
        assertEquals(testSubject_T1.getAmount(),testSubject_T1.calculate());
        assertEquals(testSubject_T2.getAmount(),testSubject_T2.calculate());
    }

    @Test
    void testCalculate_IncomingTransfer(){
        assertDoesNotThrow( () ->{
            IncomingTransfer IT_testSubject = new IncomingTransfer("04.04.2022", 300,"IT TEST","SENDER","RECIPIENT");

            double temp = IT_testSubject.getAmount();
            assertEquals(temp, IT_testSubject.getAmount());
            assertEquals(temp, IT_testSubject.calculate());

            double testCalculateCall = IT_testSubject.calculate();
            assertEquals(temp, IT_testSubject.getAmount());

        });
    }


    @Test
    void testCalculate_OutgoingTransfer(){
        assertDoesNotThrow( () -> {
            OutgoingTransfer OT_testSubject = new OutgoingTransfer("04.04.2022", 300, "IT TEST", "SENDER", "RECIPIENT");

            double temp = OT_testSubject.getAmount();
            assertEquals(temp, OT_testSubject.getAmount());
            assertEquals(-1 * OT_testSubject.getAmount(),OT_testSubject.calculate());

            double testCalculateCall = OT_testSubject.calculate();
            assertEquals(temp, OT_testSubject.getAmount());

        }
        );
    }

    @Test
    void testEquals(){
        assertDoesNotThrow( () -> {
            Transfer copy_T = new Transfer(testSubject_T2);
            Payment testGetClass = new Payment("00.00.0000",0,"Test");

            //nicht das selbe Objekt aber hat die gleichen Attributewerte
            assertNotSame(copy_T, testSubject_T2);
            assertEquals(testSubject_T2.getClass(),copy_T.getClass());
            assertEquals(testSubject_T2,copy_T);

            assertNotEquals(copy_T, testGetClass);
            assertNotEquals(testSubject_T2, testSubject_T1);
        }
        );
    }

    @Test
    void testToString(){
        assertNotNull(testSubject_T1.toString());
        assertNotNull(testSubject_T2.toString());

        //assertTrue(testSubject_T1.toString().equals(____)
        assertEquals(testSubject_T1.toString(),
                "Date :" + testSubject_T1.getDate() + "\n" +
                        "Description: " + testSubject_T1.getDescription() + "\n" +
                        "Amount: " + testSubject_T1.calculate() + "\n" +
                        "Sender: " + testSubject_T1.getSender() + "\n" +
                        "Recipient: " + testSubject_T1.getRecipient() + "\n");

        assertEquals(testSubject_T2.toString(),
                "Date :" + testSubject_T2.getDate() + "\n" +
                        "Description: " + testSubject_T2.getDescription() + "\n" +
                        "Amount: " + testSubject_T2.calculate() + "\n" +
                        "Sender: " + testSubject_T2.getSender() + "\n" +
                        "Recipient: " + testSubject_T2.getRecipient() + "\n"
        );
    }

}