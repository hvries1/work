package com.example.data;

import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.example.hadev.myfrontend.domain.Contact;

import java.util.ArrayList;
import java.util.List;

import static com.example.hadev.myfrontend.main.MainActivity.TAG;

public class SimpleDBAdapter {

    public static AmazonSimpleDB awsSimpleDB;

    public static final String DOMAIN = "frontend";

    private static Credentials credentials;

    public static void init(String auth) {
        if (credentials == null) {
            try {
                credentials = new Credentials(auth, "<HV>");
                Log.i(TAG, credentials.toString());
            } catch (Exception e) {
                Log.e(TAG, "Cannot load credentials", e);
            }
        }
    }

    // 1. Get Simple DB connection.
    public static AmazonSimpleDB getAwsSimpleDB() {
        if (awsSimpleDB == null) {
            BasicAWSCredentials aws =
                    new BasicAWSCredentials(credentials.getAuth(), credentials.getAuth2());
            awsSimpleDB = new AmazonSimpleDBClient(aws);
        }
        return awsSimpleDB;
    }

    public static void storeNewContact(String name, String phone) {
        try {
            getAwsSimpleDB().createDomain(new CreateDomainRequest(DOMAIN));
            List<ReplaceableAttribute> attribute= new ArrayList<ReplaceableAttribute>(1);
            attribute.add(new ReplaceableAttribute().withName("contactName").withValue(name));
            attribute.add(new ReplaceableAttribute().withName("phoneNumber").withValue(phone));
            getAwsSimpleDB().putAttributes(new PutAttributesRequest(DOMAIN, name, attribute));
        } catch (Exception e) {
            Log.e(TAG, "Could not store contact", e);
        }
    }

    public static Contact[] getStoredContacts() {
        try {
            SelectRequest selectRequest=  new SelectRequest("select * from " + DOMAIN).withConsistentRead(true);
            List<Item> items  = getAwsSimpleDB().select(selectRequest).getItems();

            List<Contact> contacts = new ArrayList<Contact>();
            int size= items.size();

            for(int i=0; i<size; i++) {
                Item temp1= ((Item)items.get(i));

                List<Attribute> tempAttribute = temp1.getAttributes();
                String name = "";
                String phone = "";
                for (int j=0; j < tempAttribute.size();j++) {
                    if (tempAttribute.get(j).getName().equals("contactName")) {
                        name = tempAttribute.get(j).getValue();
                    }
                    else if (tempAttribute.get(j).getName().equals("phoneNumber")) {
                        phone = tempAttribute.get(j).getValue();
                    }
                }
                contacts.add(new Contact(name, phone));
            }
            return contacts.toArray(new Contact[0]);
        } catch(Exception e) {
            Log.e(TAG, "Could not retrieve contacts", e);
        }
        return null;
    }
}

