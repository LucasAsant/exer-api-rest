package hello;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.*;

@RestController
@RequestMapping(value = "embarcadores")
public class Embarcador {

    public Embarcador() {
    }

    private DynamoDB dynamoDb;
    private String tableName = "Embarcadores";
    private Integer idEmbarc = 0000;
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
                .getItem("id", idEmbarc, "placa", placa).toJSONPretty();

        System.out.println(resultado);
        logger("read"); //Logging operation;
        return "{read}";
    }

    @PostMapping() //Post Request creating a new item in the table
    public String create() {
        initDynamoDbClient();

        dynamoDb.getTable(tableName).putItem(
            new PutItemSpec().withItem(
                new Item().withInt("id", this.idEmbarc++)
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

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(
            "id", idEmbarc,
            "placa", placa)                    //puxa os campos e valores que deseja atualizar
            .withUpdateExpression("set nome =:n")           //cria apelido para campos do array de informações
            .withValueMap(
                new ValueMap().withString(":n", "Novo nome" )   // adiciona valores atualizados
            )
            .withReturnValues(ReturnValue.UPDATED_NEW);

        table.updateItem(updateItemSpec);
        logger("update");   //Logging operation;
        return "{update}";
    }

    @DeleteMapping() //Delete Request deleting a collection
    public String delete() {
        initDynamoDbClient();

        dynamoDb.getTable(tableName).deleteItem("id", this.idEmbarc, "placa", placa);
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

    public void createTableEmbarcadores(){
        initDynamoDbClient();
        String tableName = "Embarcadores";

        try {
            System.out.println("Attempting to create table; please wait...");

            Table table = dynamoDb.createTable(tableName,
                Arrays.asList(
                    new KeySchemaElement("id", KeyType.HASH),     // Partition key
                    new KeySchemaElement("placa", KeyType.RANGE)),  // Sort key

                Arrays.asList(
                    new AttributeDefinition("id", ScalarAttributeType.N),
                    new AttributeDefinition("placa", ScalarAttributeType.S)),

                new ProvisionedThroughput(1L, 1L));
            table.waitForActive();

            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
    }

    public boolean tableExists() {
        try {
            initDynamoDbClient();
            dynamoDb.getTable(this.tableName).describe();
            System.out.println("Table Exists");
            return true;
        } catch (ResourceInUseException | NullPointerException e) {
            System.out.println("Table 'Embarcadores' does not exist.");
        }
        createTableEmbarcadores();
        return false;
    }
}
