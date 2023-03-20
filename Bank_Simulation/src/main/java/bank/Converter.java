package bank;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * diese Klasse ermöglicht Persistenz der Konto im PrivateBank
 */
public class Converter implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    /**
     * die Methode erzeugt json Datei aus gegebenem Objekt
     * @param transaction gegebenes Objekt
     * @param type
     * @param jsonSerializationContext
     * @return Json Objekt
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject obj = new JsonObject();
        JsonObject instance = new JsonObject();

        if(transaction instanceof Payment) {

            obj.addProperty("CLASSNAME", "PAYMENT");
            instance.addProperty("incomingInterest", ((Payment) transaction).getIncomingInterest());
            instance.addProperty("outgoingInterest", ((Payment) transaction).getOutgoingInterest());
            instance.addProperty("date" , transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());
            instance.addProperty("deposit", transaction.calculate());
            obj.add("INSTANCE", instance);
        }
        else if (transaction instanceof IncomingTransfer){

            obj.addProperty("CLASSNAME", "IncomingTransfer");
            instance.addProperty("date" , transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());

            if(((IncomingTransfer) transaction).getSender() != null && ((IncomingTransfer) transaction).getRecipient() != null){
                instance.addProperty("sender", ((IncomingTransfer) transaction).getSender());
                instance.addProperty("recipient", ((IncomingTransfer) transaction).getRecipient());
            }

            obj.add("INSTANCE", instance);
        }
        else if (transaction instanceof OutgoingTransfer) {

            obj.addProperty("CLASSNAME", "OutgoingTransfer");
            instance.addProperty("date" , transaction.getDate());
            instance.addProperty("amount", transaction.getAmount());
            instance.addProperty("description", transaction.getDescription());

            if(((OutgoingTransfer) transaction).getSender() != null && ((OutgoingTransfer) transaction).getRecipient() != null){
                instance.addProperty("sender", ((OutgoingTransfer) transaction).getSender());
                instance.addProperty("recipient", ((OutgoingTransfer) transaction).getRecipient());
            }

            obj.add("INSTANCE", instance);
        }
        return obj;
    }

    /**
     * diese Methode erzeugt ein Konto aus der entsprechende json Datei
     * @param jsonElement json Datei
     * @param type
     * @param jsonDeserializationContext
     * @return entsprechendes Objekt
     * @throws JsonParseException
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        try {
            JsonObject classType = jsonElement.getAsJsonObject();
            JsonObject instance = classType.get("INSTANCE").getAsJsonObject();

            if (classType.get("CLASSNAME").getAsString().equals("PAYMENT")) {
                return new Payment(
                        instance.get("date").getAsString(),
                        instance.get("amount").getAsDouble(),
                        instance.get("description").getAsString(),
                        instance.get("incomingInterest").getAsDouble(),
                        instance.get("outgoingInterest").getAsDouble()
                );
            } else if (classType.get("CLASSNAME").getAsString().equals("IncomingTransfer")) {

                if(instance.get("sender") == null && instance.get("recipient") == null){
                    return new IncomingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString()
                    );
                }
                else {
                    return new IncomingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString(),
                            instance.get("sender").getAsString(),
                            instance.get("recipient").getAsString()
                    );
                }
            } else if ((classType.get("CLASSNAME").getAsString().equals("OutgoingTransfer"))) {
                if(instance.get("sender") == null && instance.get("recipient") == null){
                    return new OutgoingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString()
                    );
                }
                else {
                    return new OutgoingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString(),
                            instance.get("sender").getAsString(),
                            instance.get("recipient").getAsString()
                    );
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return null;
    }

}
