package com.example.data;

import android.content.res.Resources;
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
import com.example.hadev.myfrontend.R;
import com.example.hadev.myfrontend.domain.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static android.content.ContentValues.TAG;

public class SimpleDBAdapter {

    public static AmazonSimpleDB awsSimpleDB;
    public static Properties properties;

    public static final String DOMAIN = "frontend";

    // 1. Get Simple DB connection.
    public static AmazonSimpleDB getAwsSimpleDB()
    {
        if (awsSimpleDB == null)
        {
            BasicAWSCredentials credentials =
                    new BasicAWSCredentials(getProperties().getProperty("accessKey"),
                        SimpleDBAdapter.getProperties().getProperty("secretKey"));
            awsSimpleDB = new AmazonSimpleDBClient(credentials);
        }
        return awsSimpleDB;
    }

    public static Properties getProperties()
    {
        if (properties == null)
        {
            properties = new Properties();
            try {
                //properties.load(SimpleDBAdapter.class.getClassLoader().getResourceAsStream("simpledb.properties"));
                properties.load(Resources.getSystem().openRawResource(R.raw.simpledb));

            } catch (Exception e) {
                Log.e(TAG, "Cannot load auth properties", e);
            }
        }
        return properties;
    }

    public static void storeNewContact(String name, String phone)
    {
        try {
            getAwsSimpleDB().createDomain(new CreateDomainRequest(DOMAIN));
            List<ReplaceableAttribute> attribute= new ArrayList<ReplaceableAttribute>(1);
            attribute.add(new ReplaceableAttribute().withName("contactName").withValue(name));
            attribute.add(new ReplaceableAttribute().withName("phoneNumber").withValue(phone));
            getAwsSimpleDB().putAttributes(new PutAttributesRequest(DOMAIN, name, attribute));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Contact[] getStoredContacts() throws Exception
    {
        SelectRequest selectRequest=  new SelectRequest("select * from " + DOMAIN).withConsistentRead(true);
        List<Item> items  = getAwsSimpleDB().select(selectRequest).getItems();

        try
        {
            List<Contact> contacts = new ArrayList<Contact>();
            int size= items.size();

            for(int i=0; i<size; i++)
            {
                Item temp1= ((Item)items.get(i));

                List<Attribute> tempAttribute = temp1.getAttributes();
                String name = "";
                String phone = "";
                for (int j=0; j < tempAttribute.size();j++)
                {
                    if (tempAttribute.get(j).getName().equals("contactName"))
                    {
                        name = tempAttribute.get(j).getValue();
                    }
                    else if (tempAttribute.get(j).getName().equals("phoneNumber"))
                    {
                        phone = tempAttribute.get(j).getValue();
                    }
                }
                contacts.add(new Contact(name, phone));
            }
            return contacts.toArray(new Contact[0]);
        }
        catch(Exception e)
        {
            throw new Exception("Could not retrieve contacts", e);
        }
    }
}

