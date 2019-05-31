package hello;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.*;

@RestController
@RequestMapping(value = "embarcadores")
public class Embarcador {

    private String databaseName = "Examples";                                   //Choosing Database
    private MongoClient mongoClient = new MongoClient();                        //Creating connection to the Database
    private MongoDatabase database = mongoClient.getDatabase(databaseName);     //Connecting to the Database

    private MongoCollection<Document> collectionName = database.getCollection("embarcadores");    //Choosing the collection
    private List<String> placasEmbarcador = Arrays.asList("ABC1234", "ABC1235");                     //Example array

    @GetMapping()   //Get Request reading a collection
    public String read() {

        FindIterable<Document> findIterable = collectionName.find();    //Selecting the collection
        Iterator<Document> iterator = findIterable.iterator();          //Creating variable iterator to run through all the lines selected

        iterator.forEachRemaining(System.out::println);                 //Printing all lines selected

        logger("read"); //Logging operation;
        return "{read}";
    }

    @PostMapping() //Post Request creating a new document in the collection
    public String create() {

        Document document = new Document();     //Creating the document
        ObjectId id = new ObjectId();           //Creating a new random Id;

        /*  Imputing data in the document  */
        document.put("_id", id.toString());
        document.put("name", "Nome Original");
        document.append("address", new Document("street", "123 Fake St"));
        document.put("city", "Faketon");
        document.put("state", "MA");
        document.put("zip", 12345);
        document.put("cars", placasEmbarcador);

        collectionName.insertOne(document);     //Inserting the document in the collection

        logger("create"); //Logging operation;
        return "{create}";
    }

    @PutMapping() //Put Request updating a document in the collection
    public String update() {

        Document updateFields = new Document();         //Creating the update document
        updateFields.append("name", "Nome Alterado");   //Setting new values in the update document

        Document setQuery = new Document("$set", updateFields);     //Creating document to set the new values
        Document searchQuery = new Document("name", "Nome Original");   //Creating document to search the field to be altered

        collectionName.updateMany(searchQuery,setQuery); //Updating the document searched with the new values

        logger("update"); //Logging operation;
        return "{update}";
    }

    @DeleteMapping() //Delete Request deleting a collection
    public String delete() {

        collectionName.drop();      //Deleting the collection embarcadores
        logger("delete");   //Logging operation;
        return "{delete}";
    }

    //Logger method
    private void logger(String request) {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            fh = new FileHandler("my-log-file.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info(request);
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
