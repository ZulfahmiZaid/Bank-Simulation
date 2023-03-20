package bank;

import bank.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {

    PrivateBank testSubject_Bank1;
    Payment bobbyDeposit, bobbyPayout, peterDeposit, peterPayout;
    IncomingTransfer bobbyReceive, peterReceive;
    OutgoingTransfer bobbySend, peterSend;
    List<Transaction> bobbyTransaction, peterTransaction;

    @BeforeEach
    void init (){
        assertDoesNotThrow(()->{
            testSubject_Bank1 = new PrivateBank("FirstBank", 0.5, 0.5);

            //Bobby
            bobbyDeposit = new Payment("01.01.2022", 200,"Paycheck", 0.1,0.1);
            bobbyPayout = new Payment("01.01.2022", -50,"Shopping", 0.1,0.1);
            bobbyReceive = new IncomingTransfer("03.01.2022",100,"Happy Birthday","Mom","Bobby");
            bobbySend = new OutgoingTransfer("02.01.2022", 200,"Bill","Bobby","KFC");

            bobbyTransaction = new ArrayList<>();
            bobbyTransaction.add(bobbyDeposit);
            bobbyTransaction.add(bobbyPayout);
            bobbyTransaction.add(bobbySend);
            bobbyTransaction.add(bobbyReceive);

            testSubject_Bank1.createAccount("Bobby",bobbyTransaction);

            //Peter
            peterDeposit = new Payment("01.01.2022", 400,"Paycheck", 0.1,0.1);
            peterPayout = new Payment("01.01.2022", 500,"Dinner", 0.1,0.1);
            peterReceive = new IncomingTransfer("03.01.2022",700,"Bonus","Bossman","Peter");
            peterSend = new OutgoingTransfer("02.01.2022", 300,"Bill","Peter","MCD");

            peterTransaction = new ArrayList<>();
            peterTransaction.add(peterDeposit);
            peterTransaction.add(peterPayout);
            peterTransaction.add(peterSend);
            peterTransaction.add(peterReceive);

        });
    }

    @AfterEach
    void clearTestPath(){
       File toClear = new File("src/main/Data/Accounts");
       File[] insideClear = toClear.listFiles();

        assert insideClear != null;
        for (File remove : insideClear) {
            assertTrue(remove.delete());
        }
        assertTrue(toClear.delete());
    }

   @Test
    void testKonstruktor1(){
        assertNotNull(testSubject_Bank1);
        assertEquals("src/main/Data/Accounts", testSubject_Bank1.getDirectoryName());
        assertNotEquals(0.0, testSubject_Bank1.getIncomingInterest());
        assertNotEquals(0.0, testSubject_Bank1.getOutgoingInterest());

        assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyDeposit));
        assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyPayout));
        assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyReceive));
        assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbySend));
   }

   @Test
   void testKonstruktor2 (){

        assertDoesNotThrow(() -> {
            PrivateBank testSubject_Bank2 = new PrivateBank("SecondBank", 0.7, 0.7, "src/main/Data/TestPath");

            assertNotNull(testSubject_Bank2);
            assertEquals("src/main/Data/TestPath", testSubject_Bank2.getDirectoryName());
            assertNotEquals(0.0, testSubject_Bank2.getIncomingInterest());
            assertNotEquals(0.0, testSubject_Bank2.getOutgoingInterest());

            assertTrue(new File(testSubject_Bank2.getDirectoryName()).delete());
        });

   }

   @Test
    void testCopyKonstruktor(){
        assertDoesNotThrow( () -> {
            PrivateBank copy = new PrivateBank(testSubject_Bank1);

            assertNotSame(copy, testSubject_Bank1);
            assertEquals(copy, testSubject_Bank1);

            assertTrue(copy.containsTransaction("Bobby", bobbyDeposit));
            assertTrue(copy.containsTransaction("Bobby", bobbyPayout));
            assertTrue(copy.containsTransaction("Bobby", bobbyReceive));
            assertTrue(copy.containsTransaction("Bobby", bobbySend));

            assertDoesNotThrow(()->{
               copy.removeTransaction("Bobby", bobbyReceive);
            });

            assertFalse(copy.containsTransaction("Bobby", bobbyReceive));
            assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyReceive));
        });
   }

    @Test
    void testSetDirectory () {
        assertDoesNotThrow(()-> {
            PrivateBank otherBankData = new PrivateBank("Other Bank", 0.6, 0.6, "src/main/Data/OtherBankData");
            otherBankData.createAccount("Peter", peterTransaction);

            testSubject_Bank1.setDirectoryName(otherBankData.getDirectoryName());
            assertEquals(otherBankData.getDirectoryName(), testSubject_Bank1.getDirectoryName());

            assertTrue(testSubject_Bank1.containsTransaction("Peter", peterPayout));

            assertTrue(new File(testSubject_Bank1.getDirectoryName(), "Bobby.json").exists());
            assertTrue(new File(testSubject_Bank1.getDirectoryName(), "Peter.json").exists());

            File toClear = new File(testSubject_Bank1.getDirectoryName());
            File[] insideClear = toClear.listFiles();

            assert insideClear != null;
            for (File remove : insideClear) {
                assertTrue(remove.delete());
            }
            assertTrue(toClear.delete());
        });
    }

    @Test
    void testSetter_II_falsch(){
        double temp = testSubject_Bank1.getIncomingInterest();
        assertEquals(testSubject_Bank1.getIncomingInterest(),temp);

        testSubject_Bank1.setIncomingInterest(30000);
        assertEquals(temp,testSubject_Bank1.getIncomingInterest());

        testSubject_Bank1.setIncomingInterest(-40000);
        assertEquals(temp,testSubject_Bank1.getIncomingInterest());
    }

    @Test
    void testSetter_OI_falsch(){
        double temp = testSubject_Bank1.getOutgoingInterest();
        assertEquals(testSubject_Bank1.getOutgoingInterest(),temp);

        testSubject_Bank1.setOutgoingInterest(30000);
        assertEquals(temp,testSubject_Bank1.getOutgoingInterest());

        testSubject_Bank1.setIncomingInterest(-40000);
        assertEquals(temp,testSubject_Bank1.getOutgoingInterest());
    }


   @Test
    void testcreateAccount1(){
        assertThrows(AccountAlreadyExistsException.class, () ->{
            testSubject_Bank1.createAccount("Bobby");
        });
        assertDoesNotThrow(() -> {
            testSubject_Bank1.createAccount("John");
        });
   }

   @Test
    void testcreateAccount2(){
       assertThrows(AccountAlreadyExistsException.class, () ->{
           List<Transaction> testadd = new ArrayList<>();
           testadd.add(bobbyReceive);
           testSubject_Bank1.createAccount("Bobby",testadd);
       });
       assertDoesNotThrow(()->{
           testSubject_Bank1.createAccount("Peter",peterTransaction);
       });
   }

   @Test
    void testAddTransasction(){
        assertThrows(AccountDoesNotExistException.class, ()->{
           testSubject_Bank1.addTransaction("Raphael",bobbyDeposit);
        });
        assertThrows(TransactionAlreadyExistException.class, ()->{
            testSubject_Bank1.addTransaction("Bobby",bobbyPayout);
        });
        assertDoesNotThrow(()->{
           testSubject_Bank1.addTransaction("Bobby", peterPayout);
        });
   }

   @Test
    void testRemoveTransaction(){
        assertThrows(AccountDoesNotExistException.class, () ->{
            testSubject_Bank1.removeTransaction("Raphael", peterSend);
        });
        assertThrows(TransactionDoesNotExistException.class, () -> {
            testSubject_Bank1.removeTransaction("Bobby",peterDeposit);
        });
        assertDoesNotThrow(()->{
            testSubject_Bank1.removeTransaction("Bobby",bobbyReceive);
        });
   }

   @Test
    void testContainsTransaction(){
       assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyDeposit));
       assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyPayout));
       assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbyReceive));
       assertTrue(testSubject_Bank1.containsTransaction("Bobby",bobbySend));

       assertDoesNotThrow( () -> {
           testSubject_Bank1.createAccount("Peter", peterTransaction);

           assertTrue(testSubject_Bank1.containsTransaction("Peter", peterDeposit));
           assertTrue(testSubject_Bank1.containsTransaction("Peter", peterPayout));
           assertTrue(testSubject_Bank1.containsTransaction("Peter", peterSend));
           assertTrue(testSubject_Bank1.containsTransaction("Peter", peterReceive));

       });
    }

   @Test
    void testGetAccountBalance(){
        assertEquals(-75,testSubject_Bank1.getAccountBalance("Bobby"));
   }

   @Test
    void testGetTransactions(){
        List<Transaction> copyBobby = new ArrayList<>(testSubject_Bank1.getTransactions("Bobby"));

        assertNull(testSubject_Bank1.getTransactions("Raphael"));
        assertEquals(copyBobby.size(),testSubject_Bank1.getTransactions("Bobby").size());

        assertTrue(copyBobby.contains(bobbyPayout));
        assertTrue(copyBobby.contains(bobbyDeposit));
        assertTrue(copyBobby.contains(bobbySend));
        assertTrue(copyBobby.contains(bobbyReceive));
   }

   @Test
    void getTransactionsSorted(){
       List<Transaction> copyBobbyAsc = new ArrayList<>(testSubject_Bank1.getTransactionsSorted("Bobby", true));
       List<Transaction> copyBobbyDesc = new ArrayList<>(testSubject_Bank1.getTransactionsSorted("Bobby", false));

       for(int index = 0; index < copyBobbyAsc.size()-1; index++){
           assertTrue(copyBobbyAsc.get(index).calculate() <= copyBobbyAsc.get(index + 1).calculate());
       }

       for(int index = 0; index < copyBobbyDesc.size()-1; index++){
           assertTrue(copyBobbyDesc.get(index).calculate() >= copyBobbyDesc.get(index + 1).calculate());
       }
   }

   @Test
    void getTransactionsByType(){
       List<Transaction> copyBobbyPositive = new ArrayList<>(testSubject_Bank1.getTransactionsByType("Bobby", true));
       List<Transaction> copyBobbyNegative = new ArrayList<>(testSubject_Bank1.getTransactionsByType("Bobby", false));

       for(int index = 0; index < copyBobbyPositive.size()-1; index++){
           assertTrue(copyBobbyPositive.get(index).calculate() >= 0);
       }

       for(int index = 0; index < copyBobbyNegative.size()-1; index++){
           assertTrue(copyBobbyNegative.get(index).calculate() < 0);
       }
   }
   @Test
   void testToString(){
       assertNotNull(testSubject_Bank1.toString());
       assertEquals(testSubject_Bank1.toString(),
               "Name :" + testSubject_Bank1.getName() + "\n" +
                       "IncomingInterest: " + testSubject_Bank1.getIncomingInterest() + "\n" +
                       "OutgoingInterest: " + testSubject_Bank1.getOutgoingInterest() + "\n");
   }

    @Test
    void testEquals(){
        assertDoesNotThrow( () -> {
            PrivateBank copyPB = new PrivateBank(testSubject_Bank1);
            Transfer example = new Transfer("00.00.0000",100,"Test");

            assertNotSame(copyPB,testSubject_Bank1);
            assertEquals(copyPB,testSubject_Bank1);
            assertNotEquals(testSubject_Bank1,example);

        });
    }

    @Test
    void testReadAcc(){
        assertDoesNotThrow(() -> {
                PrivateBank testBank_readKonstruktor = new PrivateBank("Test Bank", 0.4, 0.4, testSubject_Bank1.getDirectoryName());

                assertTrue(testBank_readKonstruktor.containsTransaction("Bobby", bobbyDeposit));
                assertTrue(testBank_readKonstruktor.containsTransaction("Bobby", bobbyPayout));
                assertTrue(testBank_readKonstruktor.containsTransaction("Bobby", bobbySend));
                assertTrue(testBank_readKonstruktor.containsTransaction("Bobby", bobbyReceive));
            });
    }

    @Test
    void testWriteAcc(){
        assertDoesNotThrow( () -> {
            File newData = new File(testSubject_Bank1.getDirectoryName());
            File[] inside = newData.listFiles();
            boolean exist = false;

            assert inside != null;
            for (File load : inside)
                assertNotEquals("Peter.json", load.getName());

            testSubject_Bank1.createAccount("Peter", peterTransaction);

            inside = newData.listFiles();
            assert inside != null;
            for (File load : inside) {
                if (load.getName().equals("Peter.json"))
                    exist = true;
            }
            assertTrue(exist);

        });

    }
}