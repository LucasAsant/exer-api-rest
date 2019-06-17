package hello;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

@RestController
@RequestMapping(value = "embarcadores")
public class Embarcador {

    private DynamoDB dynamoDb;
    private String tableName = "Embarcadores";
    private String idEmbarc = "0001";
    private String nome = "ExemploNome";
    private String placa = "ABC1234";

    // Método de inicialização do cliente
    private void initDynamoDbClient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(
                        "AKIA4QQ35UGTKGYNXIXQ",
                        "VYIej9bC9G8nzrDIEuA9P15tTqlriwJx5Zb6t/pK"
                    )
                )
            )
            .build();
        this.dynamoDb = new DynamoDB(client);
    }

    @GetMapping()   //Get Request reading item from table
    public String read() {
        initDynamoDbClient();

        String resultado = dynamoDb.getTable(tableName)
                .getItem("id", idEmbarc, "nome", nome).toJSONPretty();

        System.out.println(resultado);
        logger("read"); //Logging operation;
        return "{read}";
    }

    @PostMapping() //Post Request creating a new item in the table
    public String create() {
        initDynamoDbClient();

        String randomId = UUID.randomUUID().toString();

        dynamoDb.getTable(tableName).putItem(
            new PutItemSpec().withItem(
                new Item().withString("id", idEmbarc)
                          .withString("placa", placa)
                          .withString("nome", nome)
            )
        );

        logger("create"); //Logging operation;
        return "{create}";
    }

    @PutMapping() //Put Request updating a document in the collection
    public String update() {
        initDynamoDbClient();

        Table table = dynamoDb.getTable(tableName);

        Map<String, String> expressionAttributeNames = new HashMap<String, String>();
        expressionAttributeNames.put("#A", "Authors");
        expressionAttributeNames.put("#P", "Price");
        expressionAttributeNames.put("#I", "ISBN");

        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":novoNome", "UpdateNome");
        expressionAttributeValues.put(":novaPlaca", "ABC0000");   //Price

        table.updateItem("id", idEmbarc,
                "add #A :novoNome set #P = #P - :novaPlaca remove #I", // UpdateExpression
                expressionAttributeNames,
                expressionAttributeValues);
        logger("update"); //Logging operation;
        return "{update}";
    }

    @DeleteMapping() //Delete Request deleting a collection
    public String delete() {
        initDynamoDbClient();

        dynamoDb.getTable(tableName).deleteItem("id", idEmbarc);
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
